package com.supersouper.whichery.common.event;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.common.entity.ai.EntityAIUnrestAroundDemon;
import com.supersouper.whichery.common.entity.demon.EntityPossessedChicken;
import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public class DemonologyEvents {

    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer player) {
            player.registerExtendedProperties(DemonologyProperty.KEY, new DemonologyProperty());
        }
    }

    @SubscribeEvent
    public static void onClonePlayer(PlayerEvent.Clone e) {
        if (e.wasDeath) {
            NBTTagCompound compound = new NBTTagCompound();
            DemonologyProperty.get(e.original)
                .saveNBTData(compound);
            DemonologyProperty.get(e.entityPlayer)
                .loadNBTData(compound);
            DemonologyProperty.syncToClient(e.entityPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP player)) return;

        DemonologyProperty.syncToClient(player);
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        final World world = event.world;

        if (world.isRemote) {
            return;
        }

        if (event.entity instanceof EntityChicken chicken && !(event.entity instanceof EntityPossessedChicken))
            if (chicken.ticksExisted == 0 && world.rand.nextInt(100) == 0) {

                EntityPossessedChicken possessedChicken = EntityPossessedChicken.createPossessedChicken(chicken);

                world.spawnEntityInWorld(possessedChicken);

                // So we replace the "normal" chicken
                event.setCanceled(true);
            } else {
                chicken.tasks.addTask(1, new EntityAIUnrestAroundDemon(chicken, 1.4f));
            }
    }
}
