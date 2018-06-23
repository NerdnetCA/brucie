package ca.nerdnet.brucie.test.testa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class GroundMesh extends Scene implements BrucieListener, RenderableProvider {
    private static final String TAG = "BASIC3D";
    private static final float FOV = 67;
    private static final float CAM_X = 10f;
    private static final float CAM_Y = 10f;
    private static final float CAM_Z = 10f;

    private static final int MESH_X = 9;
    private static final int MESH_Z = 9;
    private static final int VERTEX_SIZE = 6;


    private boolean done=false;
    private Skin mySkin;
    private UiStage myUiStage;
    private Material groundMaterial;
    private Mesh groundMesh;
    private ModelBatch modelBatch;
    private PerspectiveCamera camera;
    private float[] cachedVertices;
    private short[] cachedIndices;
    private int meshSize;

    @Override
    public void dispose() {
        if(myUiStage != null) myUiStage.dispose();
        if(groundMesh != null) groundMesh.dispose();
        if(modelBatch != null) modelBatch.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
    }

    @Override
    public void show() {
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        myUiStage = new UiStage();

        Actor a = makeBackButton();
        myUiStage.addActor(a);

        groundMaterial = new Material(ColorAttribute.createDiffuse(Color.BLUE));

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(CAM_X,CAM_Y,CAM_Z);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 50f;
        camera.update();

        modelBatch = new ModelBatch();

        cachedVertices = new float[
                MESH_Z *
                MESH_X *
                VERTEX_SIZE
            ];
        cachedIndices = new short[
                (MESH_X -1) *
                (MESH_Z-1) *
                6
            ];

        groundMesh = buildMesh();

    }


    private Mesh buildMesh() {
        Mesh mesh;

        int numverts=0;
        int numindices=0;
        for(int mz=0; mz<MESH_Z; mz++) {
            for(int mx=0; mx<MESH_X; mx++) {
                cachedVertices[numverts++] = mx;
                cachedVertices[numverts++] = 0f;
                cachedVertices[numverts++] = mz;
                cachedVertices[numverts++] = 0f;
                cachedVertices[numverts++] = 1f;
                cachedVertices[numverts++] = 0f;
            }
        }
        for(int mz=0; mz<MESH_Z-1; mz++) {
            for(int mx=0; mx<MESH_X-1; mx++) {
                short vb = (short)((mz * MESH_X) + mx);
                cachedIndices[numindices++] = vb;
                cachedIndices[numindices++] = (short)(vb+MESH_X);
                cachedIndices[numindices++] = (short)(vb+MESH_X+1);
                cachedIndices[numindices++] = (short)(vb+MESH_X+1);
                cachedIndices[numindices++] = (short)(vb+1);
                cachedIndices[numindices++] = vb;
            }
        }

        mesh = new Mesh( true,
                numverts,
                numindices,
                VertexAttribute.Position(),VertexAttribute.Normal()
                );
        mesh.setVertices(cachedVertices);
        mesh.setIndices(cachedIndices);

        meshSize = numindices;

        return mesh;
    }


    @Override
    public void render(float delta) {
        Gdx.gl20.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(this);
        modelBatch.end();

        myUiStage.act(delta);
        myUiStage.draw();


    }

    @Override
    public void resize(int width, int height) {
        myUiStage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private Table makeBackButton(){
        Table t = new Table(mySkin);
        t.pad(20);
        t.setFillParent(true);
        t.align(Align.bottomRight);

        Button backButton = new Button(mySkin, "default");
        backButton.add(new Label("Back", mySkin));
        backButton.addListener(new ButtonEventAdapter(this,"back"));
        t.add(backButton);
        t.layout();
        return t;
    }

    @Override
    public boolean isDone() { return done; }
    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("back".equals(action)) {
            myGame.queueScene("BEGIN");
            done=true;
            return true;
        }
        return false;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        Renderable renderable = pool.obtain();
        renderable.material = groundMaterial;
        //renderable.worldTransform.set(mySpin);
        renderable.meshPart.mesh = groundMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = meshSize;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);

    }
}
