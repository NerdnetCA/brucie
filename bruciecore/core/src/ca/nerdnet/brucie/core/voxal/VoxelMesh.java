package ca.nerdnet.brucie.core.voxal;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

public class VoxelMesh extends Mesh {

    private int mNumVertices;
    private int mNumIndices;

    public VoxelMesh(float[] vertices, short[] indices) {
        super(
                false,
                vertices.length,
                indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Normal,
                        3, "a_normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,
                        2,"a_texCoord")
        );
        setVertices(vertices);
        setIndices(indices);
    }

    public VoxelMesh(float[] vertices, short[] indices, int numVertices, int numIndices) {
        super(
                false,
                numVertices,
                numIndices,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Normal,
                        3, "a_normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates,
                        2,"a_texCoord")
        );
        setVertices(vertices,0,numVertices);
        setIndices(indices,0,numIndices);
    }

    @Override
    public Mesh setVertices(float[] vertices) {
        mNumVertices=vertices.length;
        return super.setVertices(vertices);
    }
    @Override
    public Mesh setVertices(float[] vertices, int offset, int count) {
        mNumVertices=count;
        return super.setVertices(vertices,offset,count);
    }

    @Override
    public Mesh setIndices(short[] indices) {
        mNumIndices=indices.length;
        return super.setIndices(indices);
    }
    @Override
    public Mesh setIndices(short[] indices, int offset, int count) {
        mNumIndices=count;
        return super.setIndices(indices);
    }
}
