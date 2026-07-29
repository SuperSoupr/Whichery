package com.supersouper.whichery.api.rituals;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class RunningRitual {

    public final TileEntity leader;
    private Ritual ritual;
    private EntityPlayer starter;
    private UUID starterUUID;
    private RitualEffect effect;
    private RitualAnimation animation;
    Long startedAt = null;
    int timePassedInPreviousSave = 0;

    public RunningRitual(TileEntity leader, Ritual ritual, EntityPlayer starter) {
        this(leader);
        this.ritual = ritual;
        this.starter = starter;
        this.starterUUID = starter.getUniqueID();
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
            if (timePassed <= curStageStart) {
                stage = i;
                break;
            }
            curStageStart += ritual.stages[i];
        }

        if (timePassed == curStageStart) {
            effect.transitionToStage(stage);
            animation.transitionToStage(stage);
        }

        effect.onTick();
        animation.onTick();

        if (stage >= ritual.stages.length) {
            ((IRitualLeader) leader).endRitual();
        }
    }

    public void end() {
        effect.end(getStage());
        animation.end(getStage());
    }

    public int getTimePassed() {
        return Math.toIntExact(
            (leader.getWorldObj()
                .getTotalWorldTime() + timePassedInPreviousSave) - startedAt);
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
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        ritual = RitualRegistry.getRitual(tag.getString("ritual"));
        timePassedInPreviousSave = tag.getInteger("timePassedInPreviousSave");
        starterUUID = UUID.fromString(tag.getString("starterUUID"));

        constructAnimationAndEffect();

        animation.readFromNBT(tag.getCompoundTag("animation"));
        effect.readFromNBT(tag.getCompoundTag("effect"));
    }
}
