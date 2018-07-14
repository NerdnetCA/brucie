package ca.nerdnet.brucie.core.voxal;

import com.badlogic.gdx.utils.Pool;

public class BasicChunkMesher implements IChunkMesher {

    private Pool<MeshCompiler> meshCompilerPool;

    public BasicChunkMesher() {
        meshCompilerPool = new Pool<MeshCompiler>() {
            @Override
            protected MeshCompiler newObject() {
                MeshCompiler compiler = new MeshCompiler();
                return compiler;
            }
        };
    }

    @Override
    public VoxelMesh buildMesh(ChunkData chunkData) {

        MeshCompiler compiler = meshCompilerPool.obtain();

        compiler.compile(
                chunkData.width,
                chunkData.height,
                chunkData.depth,
                chunkData.voxels);

        VoxelMesh mesh = new VoxelMesh(compiler.getVertices(),compiler.getIndices(),
                compiler.getNumVertices(),
                compiler.getNumIndices());

        meshCompilerPool.free(compiler);

        return mesh;
    }

    private void build(short[] voxels) {

    }

    @Override
    public void dispose() {

    }


    private static class MeshCompiler implements Pool.Poolable {
        private static final int VERTEX_SIZE = 8 ;

        private int width, height, depth;

        private int topStep;
        private int bottomStep;
        private int leftStep;
        private int rightStep;
        private int frontStep;
        private int backStep;


        private int numVertices;
        private int numIndices;
        private float[] vertices;
        private short[] indices;

        public float[] getVertices() {
            return vertices;
        }
        public short[] getIndices() {
            return indices;
        }
        public int getNumIndices() {
            return numIndices;
        }

        public int getNumVertices() {
            return numVertices;
        }

        @Override
        public void reset() {
            numIndices = 0;
            numVertices = 0;
            vertices = null;
            indices = null;
        }

        public void compile(int width, int height, int depth, short[] voxeldata) {
            this.width = width;
            this.height = height;
            this.depth = depth;

            topStep = width * depth;
            bottomStep = -width * depth;
            leftStep = -1;
            rightStep = 1;
            frontStep = -width;
            backStep = width;

            calculateIndices();
            vertices = new float[width * height * depth * 6 * 4 / 2 * VERTEX_SIZE];
            numIndices = 0;
            int i = 0;
            int vOff = 0;
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    for (int x = 0; x < width; x++, i++) {
                        short voxel = voxels[i];
                        if (voxel == 0) continue;

                        if (y < height - 1) {
                            if (voxels[i + topOffset] == 0)
                                vOff = createTop(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createTop(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }
                        if (y > 0) {
                            if (voxels[i + bottomOffset] == 0)
                                vOff = createBottom(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createBottom(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }
                        if (x > 0) {
                            if (voxels[i + leftOffset] == 0)
                                vOff = createLeft(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createLeft(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }

                        if (x < width - 1) {
                            if (voxels[i + rightOffset] == 0)
                                vOff = createRight(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createRight(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }
                        if (z > 0) {
                            if (voxels[i + frontOffset] == 0)
                                vOff = createFront(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createFront(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }
                        if (z < depth - 1) {
                            if (voxels[i + backOffset] == 0)
                                vOff = createBack(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        } else {
                            vOff = createBack(offset, x, y, z, mVertices, vOff, (short) (voxels[i]-1));
                        }
                    }
                }
            }

            return vOff / VERTEX_SIZE;


        }

        public void calculateIndices() {
            int len = width * height * depth * 6 * 6 / 2;
            indices = new short[len];
            short j = 0;
            int i;
            for (i = 0; i < len; i += 6, j += 4) {
                indices[i + 0] = (short)(j + 2);
                indices[i + 1] = (short)(j + 1);
                indices[i + 2] = (short)(j + 0);
                indices[i + 3] = (short)(j + 0);
                indices[i + 4] = (short)(j + 3);
                indices[i + 5] = (short)(j + 2);
            }
        }

    }
}
