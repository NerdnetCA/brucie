package ca.nerdnet.brucie.test.master;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.brucie.core.ui.MenuSet;
import ca.nerdnet.brucie.core.ui.Panel;
import ca.nerdnet.brucie.core.ui.UiStage;

public class Main extends Scene implements BrucieListener {
    private static final String TAG = "MAIN";
    private static final float MENUFLINGSPEED = 0.3f;

    private Skin mySkin;

    private UiStage uiStage;
    private MenuSet menuSet;
    private Panel mPanel;

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
        super.show();

        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);

        uiStage = new UiStage();

        menuSet = new MenuSet(myGame, mySkin, this);
        menuSet.setPanelWidth(600f);
        menuSet.readJson("mastertest/menu.json");

        Panel p = menuSet.getPanel("MAIN");
        uiStage.addActor(p);
        mPanel = p;

        setFadeIn();
    }

    @Override
    public void render(float delta) {

        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        uiStage.act(delta);
        uiStage.draw();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        uiStage.resize(width,height);

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
        if("btn_test2d".equals(action)) {
            flingMenu("SUB_2D");
            return true;
        } else if("btn_test3d".equals(action)) {
            flingMenu("SUB_3D");
            return true;
        } else if("btn_basic2d".equals(action)) {
            myGame.queueScene("RENDER2D");
            setFadeOut();
            return true;
        } else if("btn_tilemap".equals(action)) {
            myGame.queueScene("TILEMAP");
            setFadeOut();
            return true;
        } else if("btn_voxal".equals(action)) {
            myGame.queueScene("VOXAL");
            setFadeOut();
            return true;
        } else if("btn_voxal2".equals(action)) {
            myGame.queueScene("VOXAL2");
            setFadeOut();
            return true;
        } else if("btn_tovoxal".equals(action)) {
            flingMenu("SUB_VOX");
            return true;
        } else if("btn_tristrip".equals(action)) {
            myGame.queueScene("TRISTRIP");
            setFadeOut();
            return true;
        } else if("btn_ui".equals(action)) {
            myGame.queueScene("UITEST");
            setFadeOut();
            return true;
        } else if("btn_main".equals(action)) {
            flingMenu("MAIN");
            return true;
        } else if("btn_uitest2".equals(action)) {
            myGame.queueScene("UITEST2");
            setFadeOut();
            return true;
        }
        return false;
    }

    private void flingMenu(final String newMenuName) {
        mPanel.setTouchable(Touchable.disabled);
        mPanel.addAction(
                Actions.sequence(
                        Actions.moveTo(-1024, 0, MENUFLINGSPEED, Interpolation.exp5In),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                mPanel.remove();
                                Panel p = menuSet.getPanel(newMenuName);
                                mPanel = p;
                                uiStage.addActor(p);
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
