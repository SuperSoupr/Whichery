package com.supersouper.whichery.api.rituals;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.utils.WhicheryUtils;

public class RunningRitual {

    /** NBT tag id for a compound, as used by {@link NBTTagCompound#getTagList}. */
    private static final int TAG_COMPOUND = 10;

    public final TileEntity leader;
    private Ritual ritual;
    private EntityPlayer starter;
    private UUID starterUUID;
    private RitualEffect[] effects = new RitualEffect[0];
    private RitualAnimation[] animations = new RitualAnimation[0];
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
        constructEffectsAndAnimations();
    }

    public RunningRitual(TileEntity leader) {
        this.leader = leader;
    }

    private void constructEffectsAndAnimations() {
        effects = new RitualEffect[ritual.effectClasses.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = instantiate(ritual.effectClasses[i]);
        }

        animations = new RitualAnimation[ritual.animationClasses.length];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = instantiate(ritual.animationClasses[i]);
        }
    }

    private <T> T instantiate(Class<? extends T> cls) {
        try {
            return cls.getConstructor(TileEntity.class, RunningRitual.class)
                .newInstance(leader, this);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException
            | NoSuchMethodException e) {
            throw new RuntimeException(
                cls.getName() + " must declare a public (TileEntity, RunningRitual) constructor",
                e);
        }
    }

    // Warning, this will return (max stages + 1) when the ritual is supposed to be over.
    private int getStage() {
        int timePassed = getTimePassed();
        int curStageStart = 0;
        int stage = ritual.stages.length;

        for (int i = 0; i < ritual.stages.length; i++) {
            if (timePassed < (curStageStart + ritual.stages[i])) {
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
        int stage = getStage();

        for (int i = 0; i <= Math.min(stage, seenStages.length - 1); i++) {
            if (!seenStages[i]) {
                for (RitualEffect effect : effects) {
                    effect.transitionToStage(i);
                }
                for (RitualAnimation animation : animations) {
                    animation.transitionToStage(i);
                }
                seenStages[i] = true;
            }
        }

        for (RitualEffect effect : effects) {
            effect.onTick();
        }
        for (RitualAnimation animation : animations) {
            animation.onTick();
        }

        if (stage >= ritual.stages.length) {
            ((IRitualLeader) leader).endRitual();
        }
    }

    public void end() {
        int stage = Math.min(getStage(), ritual.stages.length - 1);

        for (RitualEffect effect : effects) {
            effect.end(stage);
        }
        for (RitualAnimation animation : animations) {
            animation.end(stage);
        }
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

    public RitualEffect[] getEffects() {
        return effects;
    }

    public RitualAnimation[] getAnimations() {
        return animations;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("ritual", ritual.getName());
        tag.setString("starterUUID", starterUUID.toString());
        tag.setInteger("timePassedInPreviousSave", getTimePassed());

        NBTTagList effectTags = new NBTTagList();
        for (RitualEffect effect : effects) {
            effectTags.appendTag(effect.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("effects", effectTags);

        NBTTagList animationTags = new NBTTagList();
        for (RitualAnimation animation : animations) {
            animationTags.appendTag(animation.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("animations", animationTags);

        tag.setByte("rotation", rotation);
        tag.setByteArray("seenStages", WhicheryUtils.booleanArrayToByteArray(seenStages));
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        ritual = RitualRegistry.getRitual(tag.getString("ritual"));
        timePassedInPreviousSave = tag.getInteger("timePassedInPreviousSave");
        starterUUID = UUID.fromString(tag.getString("starterUUID"));

        constructEffectsAndAnimations();

        // A ritual's effect/animation list can change between saves, so only restore the overlap: any
        // saved state past the end is dropped, and any new component simply starts fresh.
        NBTTagList effectTags = tag.getTagList("effects", TAG_COMPOUND);
        for (int i = 0; i < Math.min(effectTags.tagCount(), effects.length); i++) {
            effects[i].readFromNBT(effectTags.getCompoundTagAt(i));
        }

        NBTTagList animationTags = tag.getTagList("animations", TAG_COMPOUND);
        for (int i = 0; i < Math.min(animationTags.tagCount(), animations.length); i++) {
            animations[i].readFromNBT(animationTags.getCompoundTagAt(i));
        }

        rotation = tag.getByte("rotation");
        seenStages = WhicheryUtils.byteArrayToBooleanArray(tag.getByteArray("seenStages"));
    }
}
