package ca.nerdnet.brucie.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.BrucieConfig;

/**
 * This is a utility class used to simplify the writing of UI Scenes,
 * such as your main menu or configuration panels.
 */
public class UiStage extends Stage {
    FitViewport mViewport;
    OrthographicCamera mCamera;

    public UiStage() {
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false,
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight()
        );
        mViewport = new FitViewport(
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight(),
                mCamera
        );
        Gdx.input.setInputProcessor(this);
        setViewport(mViewport);
    }

    public void resize(int screenWidth, int screenHeight) {
        mViewport.update(screenWidth,screenHeight);
    }
}
