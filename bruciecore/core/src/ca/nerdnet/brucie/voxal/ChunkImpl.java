package ca.nerdnet.brucie.voxal;

public class ChunkImpl implements IChunk {
    private static final int CHUNK_DIMX = 32;
    private static final int CHUNK_DIMZ = 32;
    private static final int CHUNK_DIMY = 64;

    private short[] rawData;

    public ChunkImpl() {
        rawData = new short[CHUNK_DIMX * CHUNK_DIMY * CHUNK_DIMZ];
    }

    @Override
    public int getWidth() {
        return CHUNK_DIMX;
    }

    @Override
    public int getDepth() {
        return CHUNK_DIMZ;
    }

    @Override
    public int getHeight() {
        return CHUNK_DIMY;
    }

    @Override
    public short get (int x, int y, int z) {
        if (x < 0 || x >= CHUNK_DIMX) return 0;
        if (y < 0 || y >= CHUNK_DIMY) return 0;
        if (z < 0 || z >= CHUNK_DIMZ) return 0;
        return getFast(x, y, z);
    }

    public short getFast(int x, int y, int z) {
        return rawData[y*CHUNK_DIMX*CHUNK_DIMZ + z*CHUNK_DIMX + x];
    }

    @Override
    public void set (int x, int y, int z, short value) {
        if (x < 0 || x >= CHUNK_DIMX) return;
        if (y < 0 || y >= CHUNK_DIMY) return;
        if (z < 0 || z >= CHUNK_DIMZ) return;
        setFast(x, y, z, value);
    }


    public void setFast(int x, int y, int z, short value) {
        rawData[y*CHUNK_DIMX*CHUNK_DIMZ + z*CHUNK_DIMX + x] = value;
    }


}
