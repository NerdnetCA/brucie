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

        compiler.compile(chunkData);

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

        public int compile(ChunkData chunkData) {
            this.width = chunkData.width;
            this.height = chunkData.height;
            this.depth = chunkData.depth;

            int topStep = width * depth;
            int bottomStep = -width * depth;
            int leftStep = -1;
            int rightStep = 1;
            int frontStep = -width;
            int backStep = width;

            calculateIndices();
            vertices = new float[width * height * depth * 6 * 4 / 2 * VERTEX_SIZE];
            int i = 0;
            int vOff = 0;
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    for (int x = 0; x < width; x++, i++) {

                        short voxel = chunkData.get(x,y,z);

                        /*
                        If voxel is solid, skip
                         */


                        /* If voxel is air, we need to check
                        all 6 of its neighbours. For each that
                        is solid, we need to draw a face for it.
                         */



                    }
                }
            }

            numVertices = vOff/VERTEX_SIZE;

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
