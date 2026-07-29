package com.supersouper.whichery.common.blocks.trees;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWhicheryLog extends BlockLog {

    private IIcon side;
    private IIcon top;
    private final String treeName;

    public BlockWhicheryLog(String treeName) {
        super();
        this.treeName = treeName;
        setBlockName(treeName + "_log");
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int meta) {
        return top;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int metaID) {
        return side;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        top = reg.registerIcon("whichery:trees/" + treeName + "_log_top");
        side = reg.registerIcon("whichery:trees/" + treeName + "_log_side");
    }
}
