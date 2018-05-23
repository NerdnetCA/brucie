package ca.nerdnet.brucie.glue;

import ca.nerdnet.brucie.core.BrucieGame;
import ca.nerdnet.brucie.core.Scene;

/**
 * Created by colin on 11/8/17.
 *
 * Example: extending Scene
 *
 */

public class NullScene extends Scene {
    // Best practice, always define a TAG, for debug logging.
    private static final String TAG = "NULLSCENE";

    // You need
    private boolean done=false;

    /**
     *
     * @param game reference to the BrucieGame instance.
     * @param param String parameter which can be used however you like
     */
    @Override
    public void configure(BrucieGame game, String param) {
        // Always call supermethod first
        super.configure(game,param);
    }

    /**
     * The preload method is called when the scene is queued. Use this
     * to load assets from the asset manager.
     */
    @Override
    public void preload() {

    }

    /**
     * Your scene should return true when "over"
     *
     * @return true if scene is over
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     *
     */
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    /**
     * You need to dispose any disposables created in the show() method,
     * but don't worry about unloading assets. (provided you loaded them correctly)
     */
    @Override
    public void dispose() {

        // ALWAYS call super dispose!
        super.dispose();
    }

    /**
     * You need the resize method to handle window size changes. This won't be
     * an issue for Android platform.
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
