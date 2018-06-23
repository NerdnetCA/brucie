package ca.nerdnet.brucie.test.testa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class Basic3d extends Scene implements BrucieListener, RenderableProvider {
    private static final String TAG = "BASIC3D";

    private boolean done=false;
    private Skin mySkin;
    private UiStage myUiStage;
    private PerspectiveCamera camera;
    private ShaderProgram shader;
    private Mesh myMesh;
    private Material greenMaterial;
    private ModelBatch modelBatch;
    private Matrix4 mySpin;
    private Vector3 vUp;
    private Environment environment;
    private DirectionalLight myLight;

    @Override
    public void dispose() {
        if(shader != null) shader.dispose();
        if(modelBatch != null) modelBatch.dispose();
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

        greenMaterial = new Material(ColorAttribute.createDiffuse(Color.RED));

        /*shader = new ShaderProgram(
                Gdx.files.internal("testa/default_vshad.glsl").readString(),
                Gdx.files.internal("testa/default_fshad.glsl").readString()
        );
        shader.pedantic = false;*/
        myMesh = new Mesh(true, 4, 6,
                VertexAttribute.Position()
        );
        short[] indices = {0,1,2,2,3,0};
        float[] vertices = {
                0f,0.2f,0f,
                1f,0f,0f,
                1f,0f,-1f,
                0f,0f,-1f

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
        camera.position.set(2f,2f,2f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 50f;
        camera.update();

        myLight = new DirectionalLight();
        myLight.set(0.6f,0.6f,0.6f,-0.2f,-1f,-0.4f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, .1f));
        environment.add(myLight);

        modelBatch = new ModelBatch();

        mySpin = new Matrix4();
        vUp = new Vector3(0,1,0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mySpin.rotate(vUp, 25*delta);

        modelBatch.begin(camera);
        modelBatch.render(this,environment);
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

    private Table makeBackButton() {
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
        renderable.material = greenMaterial;
        renderable.worldTransform.set(mySpin);
        renderable.meshPart.mesh = myMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = 6;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);
    }
}
