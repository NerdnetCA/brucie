package ca.nerdnet.brucie.unablox.terrain;

public class TerrainChunk {
    private static final String TAG = "TERRAINCHUNK";

    private static final int TERRAIN_CHUNK_SIZE = 128;

    private final TerrainData mTerrainData;

    public TerrainChunk() {
        mTerrainData = new TerrainData(TERRAIN_CHUNK_SIZE);
    }



}
