package ca.nerdnet.brucie.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import ca.nerdnet.brucie.core.wrangler.CachedNameWrangler;
import ca.nerdnet.brucie.core.wrangler.CachedWrangler;
import ca.nerdnet.brucie.core.wrangler.NameMapWrangler;
import ca.nerdnet.brucie.core.wrangler.Wrangler;
import ca.nerdnet.brucie.core.wrangler.WranglerConfig;

/**
 * Main game handler.
 *
 * You are meant to extend this class with a constructor that does three things:
 *   1) Set BrucieConfig parameter (somehow. don't care how).
 *   2) Set the featureWrangler field. (probably create the instance)
 *   3) Set the sceneWrangler field. (probably create the instance)
 *
 * The wranglers can be anything you want with the only requirement that
 * the feature wrangler also extends from CachedWrangler.
 *
 */

public abstract class BrucieGame implements ApplicationListener {
    // Debug tag
    private static final String TAG = "BRUCIEGAME";

    // State flags
    private static final int MODE_BOOT = 0;   // We start in boot mode
    private static final int MODE_START = 0x10; // First scene is loading
    private static final int MODE_NORMAL = 0x01; // Normal running mode, rendering a scene
    private static final int MODE_PRELOAD = 0x11;// Rendering a scene, plus preloading assets for next.
    private static final int MODE_CUE = 0x21;    // Next scene is loaded, waiting for current to finish.
    private static final int MODE_LOADING = 0x12;// Current scene all done, waiting for next to load.

    // Global config object.
    protected BrucieConfig brucieConfig;

    // Wranglers.
    protected Wrangler<Scene> sceneWrangler;
    protected CachedWrangler<GameFeature> featureWrangler;

    // Global asset manager.
    protected AssetManager assetManager;

    // Private fields

    private Scene nextScene, currentScene;

    private int myMode=0;

    private SplashScreen splash;

    /** This is called by libgdx when the libgdx part of the engine
     * is ready, and now we need to boot and configure the Brucie
     * part of the engine.
     *
     */
    @Override
    public void create() {
        // Create the global asset manager
        assetManager = new AssetManager();

        // Initialization of the wranglers happens here because we want to wait
        // for libGdx to be fully set up first.
        sceneWrangler.initialize();
        featureWrangler.initialize();

        // Use the asset manager to load the "loading screen" image.
        // This texture is kept loaded "forever"
        assetManager.load(brucieConfig.loading_img,Texture.class);

        // Create the splash screen, call its configure method,
        // and call its preload method.
        splash = new SplashScreen();
        splash.configure(this,null);
        splash.preload();
        currentScene=splash;

        // Allow the preloaded splash assets to finish loading.
        assetManager.finishLoading();

        // Display the splash
        splash.show();
        splash.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Queue up the "BEGIN" scene for after the splash is done.
        queueScene("BEGIN");

        // Go into START mode.
        myMode = MODE_START;
    }

    /** Queue a scene to be displayed when the current is done.
     *
     * @param scenename
     */
    public void queueScene(String scenename) {
        if(nextScene != null) {
            // The queue has a size limit of one.
            // throw new BrucieException(); ?
            // or maybe do nothing?
            Gdx.app.log(TAG,"Attempted to queue scene with scene already queued!");
            return;
        } else {
            // Attempt to wrangle the scene by name.
            // Set it as the next scene.
            Scene s = sceneWrangler.wrangle(scenename,null);
            nextScene = s;
            // Invoke preload to queue asset loading for the scene,
            // on the global asset manager.
            s.preload();
            // Set preload mode so that the asset manager gets called for update.
            myMode = MODE_PRELOAD;
        }
    }

    /** The main render loop.
     *
     *
     */
    public void render() {
        // How much time has passed since last render call
        float delta = Gdx.graphics.getDeltaTime();

        switch(myMode) {
            case MODE_NORMAL:
                // Normal running mode, just render the current scene.
                currentScene.render(delta);
                break;
            case MODE_START:
                // Bootstrap mode - display the splash.
                splash.render(delta);
                // Check the asset manager and the splash for done-ness
                if(assetManager.update() && splash.isDone()) {
                    Gdx.app.log(TAG,"Switching to Normal Mode");
                    myMode=MODE_NORMAL;
                    currentScene = nextScene;
                    nextScene=null;
                    splash.hide();
                    splash=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
                break;
            case MODE_PRELOAD:
                // Normal running with a queued scene that may still be loading assets.
                // First, render the scene
                currentScene.render(delta);
                // Then check the asset manager
                if(assetManager.update()) {
                    // Assets loaded, is the current scene done?
                    if(currentScene.isDone()) {
                        Gdx.app.log(TAG,"Switching to Loading Mode");
                        // Go into "Loading..." mode
                        myMode=MODE_LOADING;
                        currentScene.hide();
                        currentScene = new LoadingScreen();
                        currentScene.configure(this,null);
                        currentScene.show();
                        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    } else {
                        // Current scene is not done, but the next is ready.
                        // This is the case we want to shoot for, because
                        // it stops the loading screen from ever showing.
                        Gdx.app.log(TAG,"Switching to CUE Mode");
                        myMode=MODE_CUE;
                    }
                }
                break;
            case MODE_CUE:
                // Next scene is queued up and we are just waiting for the
                // current one to finish.
                currentScene.render(delta);
                // Check if the scene is "done", and when it is, switch to next.
                if(currentScene.isDone()) {
                    Gdx.app.log(TAG,"CUE Triggered, switching to normal");
                    currentScene.hide();
                    currentScene=nextScene;
                    nextScene=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    myMode=MODE_NORMAL;
                }
                break;
            case MODE_LOADING:
                // The current scene is done, but the next one is not ready.
                // The current scene should be pointing to a loading screen
                // at this point...
                currentScene.render(delta);
                // Same as CUE mode, but we're waiting on the asset manager.
                if(assetManager.update()) {
                    Gdx.app.log(TAG,"Loading done - Switching to Normal Mode");
                    currentScene.hide();
                    currentScene=nextScene;
                    nextScene=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    myMode=MODE_NORMAL;
                }
                break;
        }
    }



    /** Get the feature wrangler. This may not be needed, as
     * featureWrangler is protected, not private.
     *
     * @return Wrangler
     */
    public Wrangler<GameFeature> getFeatureWrangler() {
        return featureWrangler;
    }

    /** Get a feature from the feature wrangler.
     *
     * @param name
     * @return GameFeature
     */
    public GameFeature getFeature(String name) {
        GameFeature f = featureWrangler.wrangle(name,null);
        return f;
    }

    /** Register a feature in the wrangler.
     *
     * @param name Feature name
     * @param f GameFeature instance to register
     */
    public void registerFeature(String name, GameFeature f) {
        featureWrangler.register(name,f);
    }

    /** LibGDX method called when the display is resized or changed.
     * NOTE: Being created is a change.
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        currentScene.resize(width,height);
    }

    /** Libgdx lifecycle method - game has shut down,
     * cleanup time.
     */
    @Override
    public void dispose () {
        if (currentScene != null) currentScene.hide();

        // These should get uncommented eventually, but there
        // was an issue....
        //if (assetManager != null) assetManager.dispose();
        //if (nextScene != null) nextScene.hide();
    }

    /** LibGDX lifecycle method. Pass it on to the current scene.
     * These methods are less important for the desktop target
     * than for the android target.
     */
    @Override
    public void pause () {
        if (currentScene != null) currentScene.pause();
    }

    /** LibGDX lifecycle method. Pass it on to the current scene.
     * These methods are less important for the desktop target
     * than for the android target.
     */
    @Override
    public void resume () {
        if (currentScene != null) currentScene.resume();
    }

    /** Return the global asset manager.
     *
     * @return
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }
}
