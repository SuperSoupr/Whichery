package com.supersouper.whichery.common.compat.waila;

import static java.util.Collections.emptyList;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.supersouper.whichery.common.entity.demon.IPossessedEntity;
import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;

public class PossessedCreatureEntityProvider implements IWailaEntityProvider {

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        Entity entity = accessor.getEntity();

        if (entity instanceof IPossessedEntity possessedEntity) {
            EntityPlayer player = accessor.getPlayer();
            DemonologyProperty demonologyProperty = DemonologyProperty.get(player);

            if (!player.capabilities.isCreativeMode
                && (demonologyProperty == null || !demonologyProperty.isCanSeeDemonsPossessingHosts())) {
                return possessedEntity.createUnpossessedDummyHostEntity();
            }
        }

        return entity;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
        IWailaConfigHandler config) {
        return emptyList();
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
        IWailaConfigHandler config) {
        return emptyList();
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
        IWailaConfigHandler config) {
        return emptyList();
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        return null;
    }

}
