package ca.nerdnet.brucie.core.voxal;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Disposable;

interface IChunkMesher extends Disposable {

    void build();

    boolean isValid();

    Mesh getMesh();
    int getNumVertices();
    int getNumIndices();


}
