package ca.nerdnet.brucie.daleks;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.ui.MenuManager;
import ca.nerdnet.brucie.ui.Panel;
import ca.nerdnet.brucie.ui.PanelBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.Scene;

/**
 * Created by colin on 11/12/17.
 */

public class MainMenuScene extends Scene implements BrucieListener {
    private static final String TAG = "MAINMENUSCENE";

    private boolean done = false;

    private Stage myStage = null;

    private Skin mySkin;

    @Override
    public void preload() {
        // Load assets
        loadAsset("ui/ctulublu_ui.json",Skin.class);
    }
    @Override
    public void dispose() {
        myStage.dispose();
        super.dispose(); // supermethod will unload the assets.
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

        // Store references to all of our assets.
        AssetManager am = myGame.getAssetManager();
        mySkin = am.get("ui/ctulublu_ui.json",Skin.class);

        // Build the menu
        PanelBuilder pb = new PanelBuilder(myGame, mySkin, this);

        pb.setTitle("Daleks").setFramePad(30f).setButtonMargin(60f);
        pb.setWidth(400f);

        pb.addButton("Play","play").addButton("Info","about").addButton("Settings", "settings");


        Panel p = pb.build();
        myStage.addActor(p);

        //MenuManager d = new MenuManager();
        //d.dot();

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

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("about".equals(action)) {

            return true;
        } else if("play".equals(action)) {
            // Queue up the 'game' scene, as defined
            // in the scenes.json
            myGame.queueScene("GAME");
            done=true;
            return true;
        } else if("settings".equals(action)) {

            return true;
        }
        return false;
    }
}
