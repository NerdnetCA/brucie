package ca.nerdnet.brucie.core.voxal;

import com.badlogic.gdx.graphics.Mesh;

public abstract class AbstractChunkMesher implements IChunkMesher {

    protected Mesh pMesh;
    protected boolean pValid;
    protected int pNumVert;
    protected int pNumInd;

    @Override
    public void build() {

    }

    @Override
    public boolean isValid() {
        return pValid;
    }

    @Override
    public Mesh getMesh() {
        return pMesh;
    }

    @Override
    public int getNumVertices() {
        return pNumVert;
    }

    @Override
    public int getNumIndices() {
        return pNumInd;
    }

    @Override
    public void dispose() {
        if(pMesh != null) pMesh.dispose();
    }
}
