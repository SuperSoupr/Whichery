package com.supersouper.whichery.common.entity;

import com.supersouper.whichery.common.entity.ai.EntityAIMandrakePanic;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMandrake extends EntityCreature {

    public EntityMandrake(World world) {
        super(world);
        this.setSize(0.5F, 0.9F);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMandrakePanic(this, 1.0D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    @Override
    protected String getLivingSound() {
        return "whichery:mandrake.idle";
    }

    @Override
    protected String getHurtSound() {
        return "whichery:mandrake.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "whichery:mandrake.death";
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    public int getTalkInterval() {
        return 40;
    }

    @Override
    protected void func_145780_a(int x, int y, int z, Block block) {
        this.playSound("dig.grass", 0.15F, 1.0F);
    }
}
