package ca.nerdnet.brucie.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.MenuSet;
import ca.nerdnet.brucie.core.ui.Panel;
import ca.nerdnet.brucie.core.ui.UiStage;

public class MainTestB extends Scene implements BrucieListener {
    private static final String TAG = "MAINTESTB";

    private static final float MENUFLINGSPEED = 0.35f;

    private Skin mySkin;
    private UiStage myStage;
    private MenuSet menuSet;
    private Panel myPanel;

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
        super.show();
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        myStage = new UiStage();

        menuSet = new MenuSet(myGame, mySkin, this);
        menuSet.setPanelWidth(600f);
        menuSet.readJson("testb/menuset.json");

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

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        myStage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("btnMain".equals(action)) {
            flingMenu("MAIN");
            return true;
        } else if("btnBasic".equals(action)) {
            myGame.queueScene("S-BASIC");
            setFadeOut();
            return true;
        } else if("btnTemplate".equals(action)) {
            myGame.queueScene("S-TEMPLATE");
            setFadeOut();
            return true;
        } else if("btnVoxshad".equals(action)) {
            myGame.queueScene("S-VOXSHAD");
            setFadeOut();
            return true;
        } else if("btnSub1".equals(action)) {
            flingMenu("SUB1");
            return true;
        } else if("btnGround".equals(action)) {
            myGame.queueScene("S-GROUND");
            setFadeOut();
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
