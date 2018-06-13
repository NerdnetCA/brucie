package ca.nerdnet.brucie.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.core.ui.MenuSet;
import ca.nerdnet.brucie.core.ui.Panel;

public class NewUiTest extends Scene implements BrucieListener {
    private static final String TAG = "NEWUITEST";

    private static final float MENUFLINGSPEED = 0.35f;

    private Skin mySkin;
    private boolean done;
    private Stage myStage;
    private float accTime;
    private Panel myPanel;
    private boolean phide;
    private MenuSet menuSet;


    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
    }

    @Override
    public void show() {
        AssetManager assetManager = myGame.getAssetManager();
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);

        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
        FitViewport vp = new FitViewport(BrucieConfig.getVWidth(),BrucieConfig.getVHeight(),cam);
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(vp);

        menuSet = new MenuSet(myGame, mySkin, this);
        menuSet.setPanelWidth(600f);
        menuSet.readJson("examplemenuset.json");

        Panel p = menuSet.getPanel("MAIN");
        myStage.addActor(p);
        myPanel = p;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myStage.act(delta);
        myStage.draw();

    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        myStage.getViewport().update(screenWidth,screenHeight);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("btnSub1".equals(action)) {
            flingMenu("SUB1");
            return true;
        } else if("poop".equals(action)) {
            myGame.queueScene("S-TWO");
            done=true;
            return true;
        } else if("btnMain".equals(action)) {
            flingMenu("MAIN");
            return true;
        }
        return false;
    }

    private void flingMenu(final String newMenuName) {
        myPanel.setTouchable(Touchable.disabled);
        myPanel.addAction(
            Actions.sequence(
                Actions.moveTo(-1024, 0, MENUFLINGSPEED, Interpolation.exp5In),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        myPanel.remove();
                        Panel p = menuSet.getPanel(newMenuName);
                        myPanel = p;
                        myStage.addActor(p);
                        p.setX(1024);
                        p.addAction(
                                Actions.moveTo(0,0,MENUFLINGSPEED, Interpolation.exp5Out)
                        );
                    }
                })
            )
        );

    }
}
