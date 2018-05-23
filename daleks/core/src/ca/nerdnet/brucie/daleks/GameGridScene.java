package ca.nerdnet.brucie.daleks;

import ca.nerdnet.brucie.ui.MenuManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.Scene;

/** GameGridScene is the main game scene.
 * We need to implement an arbitrarily sized playfield on which
 * daleks and player can romp.
 *
 *
 * Created by colin on 11/12/17.
 */

public class GameGridScene extends Scene {
    private static final String TAG = "GAMEGRIDSCENE";

    private boolean done = false;

    private Stage myStage = null;

    @Override
    public void preload() {

    }
    @Override
    public void dispose() {
        myStage.dispose();
        super.dispose(); // ALWAYS CALL super dispose()
    }

    @Override
    public void show() {
        // Standard setup for gdx 2d 'Stage'
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
        FitViewport viewport = new FitViewport(BrucieConfig.getVWidth(), BrucieConfig.getVHeight(),camera);
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(viewport);


        // Create the game grid



        // Create some onscreen buttons/controls



    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myStage.act(delta);
        myStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        myStage.getViewport().update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     *
     * @return value of the 'done' field.
     */
    public boolean isDone() {
        return done;
    }
}
