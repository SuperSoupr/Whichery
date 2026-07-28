package com.supersouper.whichery.common.event;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import com.supersouper.whichery.common.entity.extendedproperties.VampirismProperty;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public class VampirismEvents {

    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer player) {
            player.registerExtendedProperties(VampirismProperty.KEY, new VampirismProperty());
        }
    }

    @SubscribeEvent
    public static void onClonePlayer(PlayerEvent.Clone e) {
        if (e.wasDeath) {
            NBTTagCompound compound = new NBTTagCompound();
            VampirismProperty.get(e.original)
                .saveNBTData(compound);
            VampirismProperty.get(e.entityPlayer)
                .loadNBTData(compound);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if (player.worldObj.isRemote || player.ticksExisted % 20 != 0) return;

        VampirismProperty props = VampirismProperty.get(player);
        if (props == null || !props.isVampire()) return;

        if (player.worldObj.isDaytime()
            && player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ)) {
            player.setFire(8);
        }

        FoodStats food = player.getFoodStats();
        int missing = 20 - food.getFoodLevel();
        if (missing > 0) {
            int canRestore = Math.min(missing, props.getBlood() / 10);
            if (canRestore > 0) {
                food.addStats(canRestore, 1);
                props.drainBlood(canRestore * 10, player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) return;

        VampirismProperty.syncToClient(player);
    }

    public static void bitePressed(EntityPlayerMP attacker, SyncedKeybind keybind, boolean keyDown) {
        if (!keyDown) return;

        VampirismProperty props = VampirismProperty.get(attacker);
        if (props == null || !props.isVampire()) return;

        if (attacker.worldObj.isRemote) return;

        EntityLivingBase target = findLookedAtTarget(attacker);
        if (target == null) return;

        int bloodAmount = 50;
        if (target instanceof EntityMob) {
            bloodAmount = 200;
            if (target instanceof EntityCaveSpider) {
                attacker.addPotionEffect(new PotionEffect(Potion.poison.id, 400, 2));
            } else if (target instanceof EntityBlaze) {
                attacker.setFire(10);
            }
        } else if (target instanceof EntityVillager) {
            bloodAmount = 1000;
        } else if (target instanceof EntityPlayer victim) {
            VampirismProperty victimProps = VampirismProperty.get(victim);
            if (victimProps.isVampire()) {
                bloodAmount = 0;
                attacker.addPotionEffect(new PotionEffect(Potion.wither.id, 300, 1));
                victimProps.drainBlood(100, victim);
            } else {
                bloodAmount = 1000;
                // TODO: gate this behind vampire level
                victimProps.gainVampirism(victim);
            }
        }

        target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 500, 1));
        target.addPotionEffect(new PotionEffect(Potion.weakness.id, 500, 2));

        VampirismProperty attackerProps = VampirismProperty.get(attacker);
        attackerProps.addBlood(bloodAmount, attacker);
    }

    private static EntityLivingBase findLookedAtTarget(EntityPlayerMP attacker) {
        Vec3 eyePos = Vec3.createVectorHelper(attacker.posX, attacker.posY + attacker.getEyeHeight(), attacker.posZ);
        Vec3 lookVec = attacker.getLookVec();
        Vec3 rayEnd = eyePos.addVector(lookVec.xCoord * 3, lookVec.yCoord * 3, lookVec.zCoord * 3);

        EntityLivingBase closestHit = null;
        double closestDist = 9;

        List<Entity> candidates = attacker.worldObj.getEntitiesWithinAABBExcludingEntity(
            attacker,
            attacker.boundingBox.addCoord(lookVec.xCoord * 2.0, lookVec.yCoord * 2.0, lookVec.zCoord * 2.0));

        for (Entity candidate : candidates) {
            if (!(candidate instanceof EntityLivingBase living)) continue;
            if (living.isPotionActive(Potion.weakness.id) || living.isEntityUndead()) continue;

            AxisAlignedBB expandedBB = candidate.boundingBox.expand(0.3, 0.3, 0.3);
            MovingObjectPosition intercept = expandedBB.calculateIntercept(eyePos, rayEnd);

            if (intercept != null) {
                double dist = eyePos.distanceTo(intercept.hitVec);
                if (dist < closestDist) {
                    closestDist = dist;
                    closestHit = living;
                }
            }
        }

        return closestHit;
    }
}
