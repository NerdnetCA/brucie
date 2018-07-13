package ca.nerdnet.brucie.colin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class TriangleStripTest extends Scene implements BrucieListener {
    private static final String TAG = "TRIANGLESTRIPTEST";

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;
    private float[] vertices;
    private short[] indices;
    private Mesh mMesh;
    private Renderable mRenderable;
    private ModelBatch modelBatch;
    private PerspectiveCamera mCamera;


    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        if(mMesh != null) mMesh.dispose();
        if(modelBatch != null) modelBatch.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
    }

    @Override
    public void show() {
        super.show();
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        myUiStage = new UiStage();

        mCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        mCamera.position.set(4f,4f,4f);
        mCamera.lookAt(0,0,0);
        mCamera.near = 0.2f;
        mCamera.far = 300f;
        mCamera.update();


        vertices = new float[] {
                0,0,0, 0,0,
                0,0,1,
                1,0,0,
                1,0,1
        };
        indices = new short[] {
                0,1,2,3
        };

        mMesh = new Mesh(true,4*3,4,
                new VertexAttribute(VertexAttributes.Usage.Position,3,"a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords")

        );
        mMesh.setVertices(vertices);

        mRenderable = new Renderable();
        mRenderable.meshPart.mesh = mMesh;
        mRenderable.meshPart.offset = 0;
        mRenderable.meshPart.size = 4;
        mRenderable.meshPart.primitiveType = GL20.GL_TRIANGLE_STRIP;

        Actor a = makeBackButton();
        myUiStage.addActor(a);

        modelBatch = new ModelBatch();

        setFadeIn();
    }

    @Override
    public void render(float delta) {
        // Clear the 'screen'.  Remember to clear the depth buffer when rendering 3d
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(mCamera);

        modelBatch.render(mRenderable);
        modelBatch.end();

        myUiStage.act(delta);
        myUiStage.draw();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
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
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("back".equals(action)) {
            myGame.queueScene("BEGIN");
            setFadeOut();
            return true;
        }
        return false;
    }
}
