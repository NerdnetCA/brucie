package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by colin on 7/24/17.
 */

public abstract class Scene implements Screen, WrangledObject {
    private static final String TAG = "SCENE";

    // Scene class helps track asset loading.
    private Array<String> assetList;

    protected BrucieGame myGame;

    /**
     * From WrangledObject.
     * @param game
     * @param param
     */
    public void configure(BrucieGame game, String param) {
        myGame = game;
        assetList = new Array<String>(false,16);
    }

    public boolean isDone() {
        return true;
    }

    public abstract void preload();

    public void loadAsset(String name, Class assetType) {
        myGame.assetManager.load(name, assetType);
        assetList.add(name);
    }

    public void loadAsset(String name, Class assetType, AssetLoaderParameters param) {
        myGame.assetManager.load(name, assetType, param);
        assetList.add(name);
    }

    public void dispose() {
        Gdx.app.log(TAG, "Disposing Scene " + getClass().getName());
        Iterator<String> iter = assetList.iterator();
        while(iter.hasNext()) {
            myGame.assetManager.unload(iter.next());
        }
    }

    public void hide() {
        dispose();
    }
}
