package com.supersouper.whichery.api.rituals;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.utils.WhicheryUtils;

public class RunningRitual {

    public final TileEntity leader;
    private Ritual ritual;
    private EntityPlayer starter;
    private UUID starterUUID;
    private RitualEffect effect;
    private RitualAnimation animation;
    private Long startedAt = null;
    private int timePassedInPreviousSave = 0;
    private byte rotation;
    private boolean[] seenStages = new boolean[0];

    public RunningRitual(TileEntity leader, Ritual ritual, EntityPlayer starter, byte rotation) {
        this(leader);
        this.ritual = ritual;
        this.starter = starter;
        this.starterUUID = starter.getUniqueID();
        this.rotation = rotation;
        this.seenStages = new boolean[ritual.stages.length];
        constructAnimationAndEffect();
    }

    public RunningRitual(TileEntity leader) {
        this.leader = leader;
    }

    private void constructAnimationAndEffect() {
        try {
            animation = ritual.animationClass.getConstructor(TileEntity.class, RunningRitual.class)
                .newInstance(leader, this);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException
            | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            effect = ritual.effectClass.getConstructor(TileEntity.class, RunningRitual.class)
                .newInstance(leader, this);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException
            | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // Warning, this will return (max stages + 1) when the ritual is supposed to be over.
    private int getStage() {
        int timePassed = getTimePassed();
        int curStageStart = 0;
        int stage = ritual.stages.length;

        for (int i = 0; i < ritual.stages.length; i++) {
            if (timePassed <= curStageStart) {
                stage = i;
                break;
            }
            curStageStart += ritual.stages[i];
        }

        return stage;
    }

    public void tick() {
        if (startedAt == null) {
            startedAt = leader.getWorldObj()
                .getTotalWorldTime();
        }
        int timePassed = getTimePassed();
        int curStageStart = 0;
        int stage = ritual.stages.length;

        for (int i = 0; i < ritual.stages.length; i++) {
            if (timePassed <= (curStageStart + ritual.stages[i])) {
                stage = i;
                break;
            }
            curStageStart += ritual.stages[i];
        }

        leader.getWorldObj()
            .markBlockForUpdate(leader.xCoord, leader.yCoord, leader.zCoord);

        for (int i = 0; i <= Math.min(stage, seenStages.length - 1); i++) {
            if (!seenStages[i]) {
                effect.transitionToStage(i);
                animation.transitionToStage(i);
                seenStages[i] = true;
            }
        }

        effect.onTick();
        animation.onTick();

        if (stage >= ritual.stages.length) {
            ((IRitualLeader) leader).endRitual();
        }
    }

    public void end() {
        int stage = Math.min(getStage(), ritual.stages.length - 1);
        effect.end(stage);
        animation.end(stage);
    }

    public int getTimePassed() {
        return Math.toIntExact(
            (leader.getWorldObj()
                .getTotalWorldTime() + timePassedInPreviousSave) - (startedAt != null ? startedAt
                    : leader.getWorldObj()
                        .getTotalWorldTime()));
    }

    public EntityPlayer getStarter() {
        if (starter == null) {
            starter = leader.getWorldObj()
                .func_152378_a(starterUUID);
        }
        return starter;
    }

    public Ritual getRitual() {
        return ritual;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("ritual", ritual.getName());
        tag.setString("starterUUID", starterUUID.toString());
        tag.setInteger("timePassedInPreviousSave", getTimePassed());
        tag.setTag("animation", animation.writeToNBT(new NBTTagCompound()));
        tag.setTag("effect", effect.writeToNBT(new NBTTagCompound()));
        tag.setByte("rotation", rotation);
        tag.setByteArray("seenStages", WhicheryUtils.booleanArrayToByteArray(seenStages));
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        ritual = RitualRegistry.getRitual(tag.getString("ritual"));
        timePassedInPreviousSave = tag.getInteger("timePassedInPreviousSave");
        starterUUID = UUID.fromString(tag.getString("starterUUID"));

        constructAnimationAndEffect();

        animation.readFromNBT(tag.getCompoundTag("animation"));
        effect.readFromNBT(tag.getCompoundTag("effect"));
        rotation = tag.getByte("rotation");
        if (tag.hasKey("seenStages")) {
            seenStages = WhicheryUtils.byteArrayToBooleanArray(tag.getByteArray("seenStages"));
        }
    }
}
