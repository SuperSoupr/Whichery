package com.supersouper.whichery.common.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.common.entity.EntityMandrakeRoot;

public class BlockCropMandrake extends BlockWhicheryCrop {

    public BlockCropMandrake(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.MANDRAKE_SEED.get();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(world, x, y, z, blockBroken, meta);
        if (!world.isRemote && meta >= maxStage) {
            EntityMandrakeRoot mandrakeRoot = new EntityMandrakeRoot(world);
            mandrakeRoot.setLocationAndAngles(x + 0.5, y, z + 0.5, 0.0F, 0.0F);
            world.spawnEntityInWorld(mandrakeRoot);
        }
    }
}
