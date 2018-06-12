package ca.nerdnet.brucie.unablox.exa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.ui.Panel;
import ca.nerdnet.brucie.ui.PanelBuilder;

public class MenuScene extends Scene implements BrucieListener {
    private static final String TAG = "MENUSCENE";
    private Texture myAsset;
    private Skin mySkin;
    private Stage myStage;

    protected boolean done;

    @Override
    public void dispose() {
        if(myStage != null) myStage.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
    }

    @Override
    public void show() {
        AssetManager assetManager = myGame.getAssetManager();
        mySkin = assetManager.get("ui/ctulublu_ui.json",Skin.class);

        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
        FitViewport vp = new FitViewport(BrucieConfig.getVWidth(), BrucieConfig.getVHeight(),cam);
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(vp);

        PanelBuilder pb = new PanelBuilder(myGame, mySkin, this);

        pb.setTitle("Brucie Demos").setFramePad(30f).setButtonMargin(60f);
        pb.setWidth(400f);

        pb.addButton("Delaunay","one")
                .addButton("VBO","two")
                .addButton("VBO","three")
                .addButton("tsr","four");

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

    @Override
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
        } else if("four".equals(action)) {
            myGame.queueScene("S-FOUR");
            done=true;
            return true;
        }
        return false;
    }
}
