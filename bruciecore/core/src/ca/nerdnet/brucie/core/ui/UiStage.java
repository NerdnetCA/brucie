package ca.nerdnet.brucie.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
        this(BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
    }

    public UiStage(int width, int height) {
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false,
                width,
                height
        );
        mViewport = new FitViewport(
                width,
                height,
                mCamera
        );
        Gdx.input.setInputProcessor(this);
        setViewport(mViewport);
    }



    public void resize(int screenWidth, int screenHeight) {
        mViewport.update(screenWidth,screenHeight);
    }

    public Camera getCamera() {
        return mCamera;
    }
}
