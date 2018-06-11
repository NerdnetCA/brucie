package ca.nerdnet.brucie.unablox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import ca.nerdnet.brucie.core.*;

public class VoxTest extends Scene {
    PerspectiveCamera camera;
    Model chunkModel;
    ModelInstance instance;
    ModelBatch modelBatch;
    Environment environment;

    @Override
    public void show() {
        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        //chunkModel = createChunkModel();
        instance = new ModelInstance(chunkModel);
    }

 /*   private Model createChunkModel(){
        ChunkData d = new ChunkData(16);
        final float halfSize = d.size / 2;
        // fill the chunk data w/ some random crap
        for(int x = d.size; x-->0;){
            for(int y = d.size; y-->0;){
                for(int z = d.size; z-->0;){
                    Vector3 len = new Vector3(halfSize - x,halfSize - y,halfSize - z);
                    if(len.len() / d.size > .4f){
                        continue;
                    }
                    d.set(x, y, z, (byte)1);
                }
            }
        }
        return new ChunkModelBuilder().build(d);
    }
*/

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        keyboardControls();

        modelBatch.begin(camera);
        modelBatch.render(instance, environment);
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
            instance.transform.rotate(1f,1f,0f, -speed);
        }

    }

    @Override
    public void preload() {

    }

    @Override
    public void dispose () {
        chunkModel.dispose();
        super.dispose();
    }
}
