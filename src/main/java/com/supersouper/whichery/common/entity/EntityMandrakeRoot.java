package com.supersouper.whichery.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.common.entity.ai.EntityAIMandrakeRootPanic;

public class EntityMandrakeRoot extends EntityCreature {

    int timeSinceScream = 100;

    public EntityMandrakeRoot(World world) {
        super(world);
        this.setSize(0.5F, 0.9F);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMandrakeRootPanic(this, 2D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.25D);
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
    public int getTalkInterval() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void func_145780_a(int x, int y, int z, Block block) {
        this.playSound("dig.grass", 0.15F, 1.0F);
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.isEntityAlive() && timeSinceScream++ >= 100) {
            timeSinceScream = 0;
            playLivingSound();
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!worldObj.isRemote) {
            this.dropItem(ModItems.MANDRAKE_ROOT.get(), 1);
        }
    }
}
