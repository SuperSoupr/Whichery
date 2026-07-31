package com.supersouper.whichery.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChalkRuneSmall extends Block implements ITileEntityProvider {

    public BlockChalkRuneSmall() {
        super(Material.ground);
        setBlockName("chalk_block");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof ChalkTileEntity cte) {
            if (cte.onRightClicked(player)) return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, subX, subY, subZ);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChalkSmallTileEntity(world);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkSmallTileEntity cste)) return super.removedByPlayer(world, player, x, y, z);

        MovingObjectPosition hit;
        if (world.isRemote) {
            hit = Minecraft.getMinecraft().objectMouseOver;
        } else {
            hit = WhicheryUtils.rayTraceLook((EntityPlayerMP) player);
        }

        int pos = clickPosToOrdinal((float) (hit.hitVec.xCoord - x), (float) (hit.hitVec.zCoord - z));
        if (cste.getType(pos) != null) {
            cste.setType(pos, null);
            cste.markDirty();
            for (int i = 0; i < 4; i++) {
                if (cste.getType(i) != null) {
                    return false;
                }
            }
        }
        return super.removedByPlayer(world, player, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkSmallTileEntity cste)) return true;
        MovingObjectPosition hit = Minecraft.getMinecraft().objectMouseOver;
        int pos = clickPosToOrdinal((float) (hit.hitVec.xCoord - x), (float) (hit.hitVec.zCoord - z));

        byte count = 3;

        for (int i1 = 0; i1 < count; ++i1) {
            for (int j1 = 0; j1 < count; ++j1) {
                for (int k1 = 0; k1 < count; ++k1) {
                    double d0 = (double) x + (((double) i1) / (double) count / 2) + positions[pos][0];
                    double d1 = (double) y + (((double) i1) / (double) count / 2);
                    double d2 = (double) z + (((double) i1) / (double) count / 2) + positions[pos][1];
                    effectRenderer.addEffect(
                        (new EntityDiggingFX(
                            world,
                            d0,
                            d1 - 0.3,
                            d2,
                            d0 - (double) (x + positions[pos][0]) - 0.25D,
                            d1 - (double) y - 0.25D,
                            d2 - (double) (z + positions[pos][1]) - 0.25D,
                            this,
                            meta)).applyColourMultiplier(x, y, z));
                }
            }
        }

        return true;
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    // for (Map.Entry<String, Integer> type : RitualRegistry.CHALK_TYPES.entrySet()) {
    // ItemStack result = new ItemStack(ModBlocks.CHALK_RUNE_BLOCK.get());
    // NBTTagCompound tag = new NBTTagCompound();
    // tag.setString("type", type.getKey());
    // result.setTagCompound(tag);
    // list.add(result);
    // }
    // }
    //
    // @Override
    // public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    // ItemStack result = super.getPickBlock(target, world, x, y, z);
    // ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
    // if (te != null) {
    // NBTTagCompound tag = new NBTTagCompound();
    // tag.setString("type", te.getType());
    // result.setTagCompound(tag);
    // }
    // return result;
    // }

    public static class ItemBlockChalkRuneSmall extends ItemBlock {

        public ItemBlockChalkRuneSmall(Block block) {
            super(block);
        }

        // @Override
        // public String getUnlocalizedName(final ItemStack stack) {
        // NBTTagCompound tag = stack.getTagCompound();
        // if (tag != null) {
        // return this.getUnlocalizedName() + "." + tag.getString("type");
        // }
        // return this.getUnlocalizedName();
        // }
        //
        // @Override
        //
        // public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        // float hitX, float hitY, float hitZ, int metadata) {
        // if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
        // ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        // if (te != null) {
        // te.setType(ItemChalk.getChalkType(stack));
        // te.markDirty();
        // }
        // return true;
        // }
        // return false;
        // }
    }

    public static final float[][] positions = new float[][] { new float[] { 0, 0 }, new float[] { 0, 0.5f },
        new float[] { 0.5f, 0 }, new float[] { 0.5f, 0.5f } };

    public static int clickPosToOrdinal(float clickX, float clickZ) {
        if (clickX <= 0.5f) {
            if (clickZ <= 0.5f) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (clickZ <= 0.5f) {
                return 2;
            } else {
                return 3;
            }
        }
    }
}
