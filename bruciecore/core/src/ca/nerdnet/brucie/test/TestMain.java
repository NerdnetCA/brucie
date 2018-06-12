package ca.nerdnet.brucie.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.core.ui.*;

public class TestMain extends Scene implements BrucieListener {
    private static final String TAG = "TESTMAIN";
    private Texture myAsset;

    private Stage myStage;
    private Skin mySkin;
    private boolean done = false;

    @Override
    public void dispose() {
        if(myStage != null) myStage.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
        loadAsset("bg.png",Texture.class);
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

        // Build the menu
        PanelBuilder pb = new PanelBuilder(myGame, mySkin, this);

        pb.setTitle("Brucie Tests").setFramePad(30f).setButtonMargin(60f);
        pb.setWidth(400f);

        pb.addButton("One","one")
                .addButton("Two","two")
                .addButton("Three","three");

        Texture bgtex = myGame.getAssetManager().get("bg.png");
        Sprite bg = new Sprite(bgtex);
        Image bgim = new Image(bgtex);
        myStage.addActor(bgim);

        Panel p = pb.build();
        myStage.addActor(p);

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

    public boolean isDone() { return done; }

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("one".equals(action)) {
            myGame.queueScene("S-ONE");
            done=true;
            return true;
        } else if("two".equals(action)) {
            myGame.queueScene("S-TWO");
            done=true;
            return true;
        } else if("three".equals(action)) {
            myGame.queueScene("S-THREE");
            done=true;
            return true;
        }
        return false;
    }

}
