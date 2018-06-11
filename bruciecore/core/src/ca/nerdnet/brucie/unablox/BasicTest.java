package ca.nerdnet.brucie.unablox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.unablox.chunk.ChunkData;
import ca.nerdnet.brucie.unablox.chunk.ChunkModelBuilder;


public class BasicTest extends Scene {

    private PerspectiveCamera camera;
    public Model model;
    public ModelInstance instance;

    private Texture myAsset;
    private ModelBatch modelBatch;
    private Environment environment;

    private CameraInputController camController;
    private ChunkModelBuilder cmb;

    @Override
    public void dispose() {
        if(modelBatch != null) modelBatch.dispose();
        if(model != null) model.dispose();
        //if(vertexBufferObject != null) vertexBufferObject.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        //loadAsset("myassset.png",Texture.class);
    }

    @Override
    public void show() {
        AssetManager assetManager = myGame.getAssetManager();
        //myAsset = assetManager.get("myasset.png",Texture.class);


        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(
                67,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        camera.position.set(0f,0f,20f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

/*        VertexAttribute vA = new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position");
        VertexAttribute vC = new VertexAttribute(VertexAttributes.Usage.Position, 4, "a_color");
        VertexAttributes vAs = new VertexAttributes(new VertexAttribute[]{vA, vC});

        vertexBufferObject = new VertexBufferObject(false, 2000, vAs);*/

        //ModelBuilder modelBuilder = new ModelBuilder();
        /*model = modelBuilder.createBox(5f,5f,5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
                );
        instance = new ModelInstance(model);*/

        model = buildChunk();
        instance = new ModelInstance(model);


    }
    private void keyboardControls() {
        final float speed = 2f;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            instance.transform.rotate(0f,1f,0f, speed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            instance.transform.rotate(0f,1f,0f, -speed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            instance.transform.rotate(1f,0f,0f, speed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            instance.transform.rotate(1f,0f,0f, -speed);
        }

    }
    private Model buildChunk() {
        ChunkData d = new ChunkData(16);
        final float halfSize = d.size/2;
        for(int x = d.size; x-- > 0;) {
            for (int y = d.size; y-- > 0; ) {
                for (int z = d.size; z-- > 0; ) {
                    Vector3 len = new Vector3(halfSize - x, halfSize - y, halfSize - z);
                    if (len.len() / d.size > .29f) {
                        continue;
                    }
                    d.set(x, y, z, (byte) 1);
                }
            }
        }
        //d.set(0,1,8,(byte)1);
        cmb = new ChunkModelBuilder();
        return cmb.build(d);
    }

    private void fillbuffer() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //keyboardControls();

        camController.update();
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(instance,environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
