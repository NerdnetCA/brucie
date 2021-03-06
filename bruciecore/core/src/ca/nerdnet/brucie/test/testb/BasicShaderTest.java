package ca.nerdnet.brucie.test.testb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import ca.nerdnet.brucie.core.util.CoreShader;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class BasicShaderTest extends Scene implements BrucieListener {
    private static final String TAG = "BASICSHADERTEST";

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;
    private CoreShader shader;
    private Renderable renderable;


    private Model model;
    private RenderContext renderContext;
    private PerspectiveCamera mCamera;

    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        if(shader != null) shader.dispose();
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

        //---

        MeshBuilder m = new MeshBuilder();
        m.begin(Usage.Position | Usage.Normal |Usage.TextureCoordinates,
                GL20.GL_TRIANGLES);
        m.sphere(2f,2f,2f,20,20);
        Mesh mesh = m.end();

        //----


        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));


        mCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        mCamera.position.set(10f,10f,10);
        mCamera.lookAt(0,0,0);
        mCamera.near = 0.2f;
        mCamera.far = 300f;
        mCamera.update();

        renderable = new Renderable();
        renderable.environment = null;
        renderable.worldTransform.idt();

        renderable.material = new Material();
        renderable.meshPart.mesh = mesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = mesh.getNumIndices();
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;

        shader = new CoreShader();
        shader.init();

        Actor a = makeBackButton();
        myUiStage.addActor(a);
        setFadeIn();
    }

    @Override
    public void render(float delta) {
        // Clear the 'screen'.  Remember to clear the depth buffer when rendering 3d
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderContext.begin();
        shader.begin(mCamera,renderContext);
        shader.render(renderable);
        shader.end();
        renderContext.end();

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
