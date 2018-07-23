package ca.nerdnet.brucie.test.master;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;

import ca.nerdnet.brucie.core.*;

public class TilemapTest extends Scene {
    private static final String TAG = "TILEMAPTEST";
    private Texture myAsset;

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void preload() {
        //loadAsset("myassset.png", Texture.class);
    }

    @Override
    public void show() {
        super.show();
        //myAsset = assetManager.get("myasset.png", Texture.class);


    }

    @Override
    public void render(float delta) {

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
