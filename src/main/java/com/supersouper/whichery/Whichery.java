package com.supersouper.whichery;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Whichery.MODID, version = Tags.VERSION, name = Whichery.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class Whichery {

    public static final String MODID = "whichery";
    public static final String MODNAME = "Whichery";

    @SidedProxy(
        clientSide = "com.supersouper.whichery.ClientProxy",
        serverSide = "com.supersouper.whichery.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    public static CreativeTabs whicheryTab = new CreativeTabs(MODID) {

        @Override
        public Item getTabIconItem() {
            return this.getIconItemStack()
                .getItem();
        }

        public static final ItemStack ICON_ITEM = new ItemStack(ModItems.CHALK.get());

        @Override
        public ItemStack getIconItemStack() {
            return ICON_ITEM;
        }
    };
}
