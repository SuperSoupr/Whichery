package com.supersouper.whichery.common.dimensions.spirit;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderSpiritRealm implements IChunkProvider {

    private final World world;

    public ChunkProviderSpiritRealm(World world) {
        this.world = world;
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        Block[] blocks = new Block[65536];
        byte[] metadata = new byte[65536];

        Random chunkRand = new Random(chunkX * 341873128712L + chunkZ * 132897987541L);

        int woolColor = chunkRand.nextInt(16);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 64; y++) {
                    int index = (x * 16 + z) * 256 + y;

                    if (y == 0) {
                        blocks[index] = Blocks.bedrock;
                    } else {
                        blocks[index] = Blocks.wool;
                        metadata[index] = (byte) woolColor;
                    }
                }
            }
        }

        Chunk chunk = new Chunk(world, blocks, metadata, chunkX, chunkZ);
        chunk.generateSkylightMap();
        return chunk;
    }

    /// Populates the given chunk. Note that MC populates the +X/+Z corner of it, not the chunk itself.
    /// All positions should be offset by +8,+8 to account for this.
    /// The 4 chunks around the corner are properly generated and may be accessed. Modifying chunks outside of these 4
    /// will cause cascading worldgen.
    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {}

    /// Checks what creatures can spawn on the given block
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z) {
        BiomeGenBase biome = this.world.getBiomeGenForCoords(x, z);
        return biome.getSpawnableList(creatureType);
    }

    /// Scans the chunk for existing structures
    @Override
    public void recreateStructures(int chunkX, int chunkZ) {}

    // <editor-fold desc="Stubs" defaultstate="collapsed">

    @Override
    public Chunk loadChunk(int x, int z) {
        return provideChunk(x, z);
    }

    @Override
    public String makeString() {
        return "EndOfTime";
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return true;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return false;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public ChunkPosition func_147416_a(World world, String structureName, int x, int y, int z) {
        // findClosestStructure(String type, int x, int y, int z)
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void saveExtraData() {

    }

    // </editor-fold>
}
