package ca.nerdnet.brucie.test.master;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

public class UiTest extends Scene implements BrucieListener {
    private static final String TAG = "UITEST";

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;

    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("humbaba/humbaba_ui.json", Skin.class);
    }

    @Override
    public void show() {
        super.show();
        mySkin = assetManager.get("humbaba/humbaba_ui.json", Skin.class);
        myUiStage = new UiStage();

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

        Button backButton = new Button(mySkin, "plain_blue");
        backButton.add(new Label("Back", mySkin));
        backButton.addListener(new ButtonEventAdapter(this,"back"));
        t.add(backButton).height(64);
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
