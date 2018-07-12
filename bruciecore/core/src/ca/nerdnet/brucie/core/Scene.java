package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Superclass for the scenes of the game.
 *
 *
 */


public abstract class Scene implements Screen, WrangledObject {
    private static final String TAG = "SCENE";


    public static final float WIPE_TIME = 0.9f;
    private static final int WIPE_FADEIN = 1;
    private static final int WIPE_FADEOUT = 2;

    protected boolean isWiping;
    protected boolean done;

    protected BrucieGame myGame;
    protected AssetManager assetManager;

    // helps track asset loading.
    private Array<String> assetList;

    private ShapeRenderer fadeRenderer;
    private OrthographicCamera mCamera;
    private float mWidth=256f, mHeight=256f;
    private float wipeSpeedFactor;
    private float wipeTime;
    private int wipeType;

    /**
     * From WrangledObject.
     * @param game
     * @param param
     */
    public void configure(BrucieGame game, String param) {
        myGame = game;
        assetList = new Array<String>(false,16);
        assetManager = myGame.getAssetManager();
    }

    /** Scenes are generally allowed to run until isDone returns false.
     * @return
     */
    public boolean isDone() {
        return done;
    }

    /** preload is called to allow the scene to queue up asset loads.
     *
     */
    public abstract void preload();

    /** using loadAsset rather than the libgdx AssetManager directly,
     * allows the Brucie Scene class to do some management for you.
     *
     * @param name
     * @param assetType
     */
    public void loadAsset(String name, Class assetType) {
        myGame.assetManager.load(name, assetType);
        assetList.add(name);
    }

    /** loadAsset, but with parameter.
     *
     * @param name
     * @param assetType
     * @param param
     */
    public void loadAsset(String name, Class assetType, AssetLoaderParameters param) {
        myGame.assetManager.load(name, assetType, param);
        assetList.add(name);
    }

    /**
     * dispose really REALLY needs to be called for Scenes, in order to mark
     * assets for unloading. The hide method will do this.
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "Disposing Scene " + getClass().getName());
        Iterator<String> iter = assetList.iterator();
        while(iter.hasNext()) {
            myGame.assetManager.unload(iter.next());
        }
    }

    /** hide will call dispose. See dispose. If you override this, make sure to call super.
     *
     */
    @Override
    public void hide() {
        dispose();
    }

    public void show() {
        wipeSpeedFactor = 1f/WIPE_TIME;
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false,mWidth,mHeight);
        mCamera.update();
        fadeRenderer = new ShapeRenderer();
    }

    public void resize(int screenWidth, int screenHeight) {
        //mCamera.update(); // Is this needed??
    }

    /** Call this supermethod at the end of your overriding method.
     *
     * @param delta
     */
    public void render(float delta) {
        // Render the wipe
        if (isWiping) {
            // Update timing
            wipeTime += (delta * wipeSpeedFactor);
            if (wipeTime > 1f) {
                Gdx.app.log(TAG,"Wipe done.");
                wipeTime = 1f;
                isWiping = false;
                if(wipeType == WIPE_FADEOUT) done = true; // this should be removed in favour of a better way.
            }

            // Render the wipe
            float fadeAmount;

            switch (wipeType) {
                case WIPE_FADEIN:
                    fadeAmount = 1f - wipeTime;
                    break;
                case WIPE_FADEOUT:
                default:
                    fadeAmount = wipeTime;
                    break;
            }

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            fadeRenderer.setProjectionMatrix(mCamera.combined);
            fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            fadeRenderer.setColor(0f, 0f, 0f, fadeAmount);
            fadeRenderer.rect(0f, 0f, mWidth, mHeight);
            fadeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

        }
    }


    public void setFadeIn() {
        if(!isWiping) {
            isWiping=true;
            wipeTime = 0f;
            wipeType = WIPE_FADEIN;
        }
    }

    public void setFadeOut() {
        if(!isWiping) {
            isWiping=true;
            wipeTime = 0f;
            wipeType = WIPE_FADEOUT;
        }
    }

}
