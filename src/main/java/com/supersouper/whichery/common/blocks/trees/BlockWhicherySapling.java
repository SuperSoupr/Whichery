package com.supersouper.whichery.common.blocks.trees;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.terraingen.TerrainGen;

import com.supersouper.whichery.Whichery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockWhicherySapling extends BlockSapling {

    public BlockWhicherySapling(String treeName) {
        super();
        setBlockName(treeName + "_sapling");
        setBlockTextureName("whichery:trees/" + treeName + "_sapling");
        setStepSound(soundTypeGrass);
        setCreativeTab(Whichery.whicheryTab);
    }

    /**
     * Wrapper for {@link #func_149878_d(World, int, int, int, Random)}. Superclass automatically posts event and
     * removes the sapling to prepare for generation - you should return whether generation was successful. If not,
     * superclass will replace the sapling.
     */
    public abstract boolean growTree(World world, int x, int y, int z, Random rand);

    @Override
    public final void func_149878_d(World world, int x, int y, int z, Random rand) {
        if (!TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;

        if (!world.getBlock(x, y - 1, z)
            .canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this)) {
            return;
        }

        world.setBlock(x, y, z, net.minecraft.init.Blocks.air, 0, 4);

        if (!growTree(world, x, y, z, rand)) {
            world.setBlock(x, y, z, this, 0, 4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> metaList) {
        metaList.add(new ItemStack(this));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(this.getTextureName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}
