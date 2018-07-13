package ca.nerdnet.brucie.test.master;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import javax.swing.text.AttributeSet;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;
import ca.nerdnet.brucie.core.voxal.ChunkData;
import ca.nerdnet.brucie.core.voxal.ChunkMesh2;
import ca.nerdnet.brucie.test.testb.ShaderB2;
import ca.nerdnet.brucie.voxal.VoxelChunk;

public class Voxal2Test extends Scene implements BrucieListener {
    private static final String TAG = "VOXAL2TEST";

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;
    private PerspectiveCamera mCamera;

    private VoxalWorld voxalWorld;
    private Texture mTex;
    private ChunkData vchunk;
    private ShaderB2 shader;

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
        loadAsset("mastertest/voxtiles/toruqtiles.png",Texture.class);
    }

    @Override
    public void show() {
        super.show();
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        myUiStage = new UiStage();

        mTex = assetManager.get("mastertest/voxtiles/toruqtiles.png",Texture.class);
        //mTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

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


        shader = new ShaderB2();
        shader.init();

        voxalWorld = new VoxalWorld(mTex, shader);
        voxalWorld.setChunkData(vchunk);

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

        Actor a = makeBackButton();
        myUiStage.addActor(a);

        setFadeIn();
    }

    @Override
    public void render(float delta) {
        // Clear the 'screen'.  Remember to clear the depth buffer when rendering 3d
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Insert Test render code here
        voxalWorld.render(mCamera);

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

    public static class VoxalWorld implements Disposable {

        private final Shader myShader;
        private ChunkData mData;
        private Mesh mMesh;
        private ChunkMesh2 chunkMesh;
        private Texture mTex;
        private Renderable mRenderable;
        private RenderContext renderContext;

        public VoxalWorld(Texture mytex, Shader myshad) {
            mTex = mytex;
            myShader = myshad;
        }

        public void setChunkData(ChunkData dat) {
            mData = dat;
            renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 0));

            chunkMesh = new ChunkMesh2(16,16,16);
            mMesh = chunkMesh.getMesh(dat.voxels);

            mRenderable = new Renderable();

            mRenderable.worldTransform.idt();
            mRenderable.material = new Material();
            mRenderable.meshPart.mesh = mMesh;
            mRenderable.meshPart.offset = 0;
            mRenderable.meshPart.size = mMesh.getNumIndices();
            mRenderable.meshPart.primitiveType = GL20.GL_TRIANGLES;

        }

        public void render(Camera cam) {
            renderContext.begin();
            renderContext.textureBinder.bind(
                    mTex
            );

            myShader.begin(cam,renderContext);
            myShader.render(mRenderable);
            myShader.end();

            renderContext.end();
        }

        @Override
        public void dispose() {
            if(mMesh != null) mMesh.dispose();
        }
    }
}
