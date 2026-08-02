package com.supersouper.whichery.common.dimensions.spirit;

import net.minecraft.world.biome.BiomeGenBase;

import com.supersouper.whichery.common.config.OtherConfig;

public class BiomeGenSpiritRealm extends BiomeGenBase {

    public BiomeGenSpiritRealm() {
        super(OtherConfig.spiritRealmBiomeID);

        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();

        this.enableRain = false;

        this.setBiomeName("Spirit Realm");
    }
}
