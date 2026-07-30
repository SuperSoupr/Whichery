package com.supersouper.whichery.common.blocks.trees;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockWhicheryPlanks extends Block {

    String treeName;

    public BlockWhicheryPlanks(String treeName) {
        super(Material.wood);
        this.treeName = treeName;
        setBlockName(treeName + "_planks");
        setBlockTextureName("whichery:trees/" + treeName + "_planks");
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
    }
}
