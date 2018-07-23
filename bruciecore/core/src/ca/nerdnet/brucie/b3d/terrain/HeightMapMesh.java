package ca.nerdnet.brucie.b3d.terrain;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;

public class HeightMapMesh extends Mesh {
    public HeightMapMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }
}
