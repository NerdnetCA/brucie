package ca.nerdnet.brucie.test.testb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
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
import ca.nerdnet.brucie.b3d.voxal.ChunkData;
import ca.nerdnet.brucie.b3d.voxal.ChunkMesh2;

public class ShaderTestBTwo extends Scene implements BrucieListener {
    private static final String TAG = "VOXALTEST";

    private boolean done=false;

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;

    // Other
    private ChunkData vchunk;
    private ShaderB2 shader;
    private RenderContext renderContext;
    private PerspectiveCamera mCamera;
    private Renderable mRenderable;

    private ChunkMesh2 chunkMesh;
    private Texture mTex;

    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        super.dispose();
    }

    @Override
    public void preload() {

        loadAsset("mastertest/voxtiles/toruqtiles.png",Texture.class);
        loadAsset("ui/ctulublu_ui.json", Skin.class);
    }

    @Override
    public void show() {
        super.show();
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        myUiStage = new UiStage();

        mTex = assetManager.get("mastertest/voxtiles/toruqtiles.png",Texture.class);
        mTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        vchunk = new ChunkData(16,16,16);

        for( int x=0; x<16; x++) {
            for( int z=0; z<16; z++) {
                vchunk.set(x,0,z, (short) 1);
            }
        }
        vchunk.set(0,1,0,(short)1);
        vchunk.set(1,1,0,(short)2);
        vchunk.set(2,1,0,(short)3);
        vchunk.set(3,1,0,(short)8);
        vchunk.set(4,1,0,(short)9);
        vchunk.set(3,0,3,(short)7);
        vchunk.set(15,1,0,(short)12);
        vchunk.set(15,1,15,(short)43);
        vchunk.set(0,1,15,(short)44);

        chunkMesh = new ChunkMesh2(16,16,16);
        //chunkMesh.calculateVertices(vchunk.voxels);


        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 0));


        mCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        mCamera.position.set(4f,4f,4f);
        mCamera.lookAt(0,0,0);
        mCamera.near = 0.2f;
        mCamera.far = 300f;
        mCamera.update();

        CameraInputController cin = new CameraInputController(mCamera);
        Gdx.input.setInputProcessor(
                new InputMultiplexer(
                        Gdx.input.getInputProcessor(),
                        cin
                )

        );

        DirectionalLight mylight = new DirectionalLight();
        mylight.setColor(1,1,1,1);
        mylight.setDirection(-1,-1,-1);
        Environment env = new Environment();
        env.add(mylight);

        mRenderable = new Renderable();
        mRenderable.environment = env;
        mRenderable.worldTransform.idt();

        mRenderable.material = new Material();
        mRenderable.meshPart.mesh = chunkMesh.getMesh(vchunk.voxels);
        mRenderable.meshPart.offset = 0;
        mRenderable.meshPart.size = mRenderable.meshPart.mesh.getNumIndices();
        mRenderable.meshPart.primitiveType = GL20.GL_TRIANGLES;

        shader = new ShaderB2();
        shader.init();

        Actor a = makeBackButton();
        myUiStage.addActor(a);



        setFadeIn();
    }

    @Override
    public void render(float delta) {
        // Clear the 'screen'.  Remember to clear the depth buffer when rendering 3d
        Gdx.gl20.glClearColor(0.2f,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        renderContext.begin();
        renderContext.textureBinder.bind(
                mTex
        );

        shader.begin(mCamera,renderContext);
        shader.render(mRenderable);
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
