package ca.nerdnet.brucie.core.voxal;

public class ChunkData {
    public final short[] voxels;
    public final int width;
    public final int height;
    public final int depth;
    private final int widthTimesDepth;

    public ChunkData (int width, int height, int depth) {
        this.voxels = new short[width * height * depth];
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.widthTimesDepth = width * depth;
    }

    public short get (int x, int y, int z) {
        if (x < 0 || x >= width) return 0;
        if (y < 0 || y >= height) return 0;
        if (z < 0 || z >= depth) return 0;
        return getFast(x, y, z);
    }

    public short getFast (int x, int y, int z) {
        return voxels[x + z * width + y * widthTimesDepth];
    }

    public void set (int x, int y, int z, short voxel) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        if (z < 0 || z >= depth) return;
        setFast(x, y, z, voxel);
    }

    public void setFast (int x, int y, int z, short voxel) {
        voxels[x + z * width + y * widthTimesDepth] = voxel;
    }


}
