package ca.nerdnet.brucie.test.testa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class Basic3d extends Scene implements BrucieListener {
    private static final String TAG = "BASIC3D";

    private boolean done=false;
    private Skin mySkin;
    private UiStage myUiStage;
    private PerspectiveCamera camera;
    private ShaderProgram shader;
    private Mesh myMesh;

    @Override
    public void dispose() {
        if(myUiStage != null) myUiStage.dispose();
        if(myMesh != null) myMesh.dispose();
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

        shader = new ShaderProgram(
                Gdx.files.internal("testa/default_vshad.glsl").readString(),
                Gdx.files.internal("testa/default_fshad.glsl").readString()
        );
        shader.pedantic = false;
        myMesh = new Mesh(true, 3, 3,
                new VertexAttribute(
                        VertexAttributes.Usage.Position,
                        3,
                        "a_Position")
        );
        short[] indices = {0,1,2};
        float[] vertices = {
                0f,0f,0f,
                1f,0f,0f,
                1f,0f,1f
        };
        myMesh.setVertices(vertices);
        myMesh.setIndices(indices);

        /*MeshBuilder mb = new MeshBuilder();
        VertexAttributes vats = new VertexAttributes(new VertexAttribute(
                VertexAttributes.Usage.Position,
                3,
                "a_Position"
        ));
        mb.begin(vats, GL20.GL_TRIANGLES);
        mb.capsule(2f,4f,10);
        myMesh = mb.end();*/

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f,10f,10f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 50f;
        camera.update();

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shader.begin();
        shader.setUniformMatrix("u_matViewProj",camera.combined);
        myMesh.render(shader, GL20.GL_TRIANGLES);
        shader.end();
        Gdx.gl20.glFlush();

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
}
