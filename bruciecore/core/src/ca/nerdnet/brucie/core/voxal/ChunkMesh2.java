package ca.nerdnet.brucie.core.voxal;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector3;

public class ChunkMesh2 {
    private static final int VERTEX_SIZE = 8 ;
    private static final short TILES_DIM = 16;

    private final int width, height, depth;

    private final int topOffset;
    private final int bottomOffset;
    private final int leftOffset;
    private final int rightOffset;
    private final int frontOffset;
    private final int backOffset;

    public final Vector3 offset = new Vector3();

    private float[] mVertices;
    private short[] mIndices;

    private int mNumIndices=0;
    private Mesh mMesh;
    private int mNumVertices;

    public ChunkMesh2(int width, int height, int depth){
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.topOffset = width * depth;
        this.bottomOffset = -width * depth;
        this.leftOffset = -1;
        this.rightOffset = 1;
        this.frontOffset = -width;
        this.backOffset = width;

    }

    public Mesh getMesh(short[] voxels) {
        Mesh rv = null;

        mNumVertices = calculateVertices(voxels);
        rv = new Mesh (
                true,
                //mNumVertices,
                width * height * depth * 6 * 4 / 2,
                //mNumIndices,
                width * height * depth * 6 * 6 / 2,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Normal,
                        3, "a_normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,
                        2,"a_texCoord")
        );
        rv.setVertices(mVertices);
        rv.setIndices(mIndices);
        return rv;
    }

    public int getMaxVerts() {
        return width * height * depth * 6 * 4 / 2;
    }

    public void calculateIndices() {
        int len = width * height * depth * 6 * 6 / 2;
        mIndices = new short[len];
        short j = 0;
        int i;
        for (i = 0; i < len; i += 6, j += 4) {
            mIndices[i + 0] = (short)(j + 2);
            mIndices[i + 1] = (short)(j + 1);
            mIndices[i + 2] = (short)(j + 0);
            mIndices[i + 3] = (short)(j + 0);
            mIndices[i + 4] = (short)(j + 3);
            mIndices[i + 5] = (short)(j + 2);
        }
    }

    public int calculateVertices (short[] voxels) {
        if(mVertices == null) {
            mVertices = new float[getMaxVerts()*VERTEX_SIZE];
        }
        if(mIndices == null) {
            calculateIndices();
        }
        mNumIndices = 0;
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

    public int createTop (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM ;
        v0 = ((float)texY) / TILES_DIM ;
        u1 = ((float)(texX+1)) / TILES_DIM;
        v1 = ((float)(texY+1)) / TILES_DIM;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        mNumIndices += 6;

        return vertexOffset;
    }

    public int createBottom (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM ;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM;
        v0 = ((float)texY) / TILES_DIM;
        u1 = ((float)(texX+1)) / TILES_DIM ;
        v1 = ((float)(texY+1)) / TILES_DIM ;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = u0;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        mNumIndices += 6;

        return vertexOffset;
    }

    public int createLeft (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM ;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM ;
        v0 = ((float)texY) / TILES_DIM ;
        u1 = ((float)(texX+1)) / TILES_DIM ;
        v1 = ((float)(texY+1)) / TILES_DIM ;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        mNumIndices += 6;

        return vertexOffset;
    }

    public int createRight (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM ;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM ;
        v0 = ((float)texY) / TILES_DIM ;
        u1 = ((float)(texX+1)) / TILES_DIM ;
        v1 = ((float)(texY+1)) / TILES_DIM ;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        mNumIndices += 6;

        return vertexOffset;
    }

    public int createFront (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM ;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM ;
        v0 = ((float)texY) / TILES_DIM ;
        u1 = ((float)(texX+1)) / TILES_DIM ;
        v1 = ((float)(texY+1)) / TILES_DIM ;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        mNumIndices += 6;

        return vertexOffset;
    }

    public int createBack (Vector3 offset, int x, int y, int z, float[] vertices, int vertexOffset, short texNum) {
        float u0,v0,u1,v1;
        int texX = texNum % TILES_DIM ;
        int texY = texNum / TILES_DIM ;

        u0 = ((float)texX) / TILES_DIM ;
        v0 = ((float)texY) / TILES_DIM ;
        u1 = ((float)(texX+1)) / TILES_DIM ;
        v1 = ((float)(texY+1)) / TILES_DIM ;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = u0;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v0;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;

        mNumIndices += 6;

        return vertexOffset;
    }

}
