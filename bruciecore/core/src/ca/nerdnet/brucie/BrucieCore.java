package ca.nerdnet.brucie;

import ca.nerdnet.brucie.core.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** BrucieCore game skeleton. This is more simplified than the default libgdx main class.
 *  All that is needed is to provide the BrucieConfig instance appropriate to the platform.
 *
 */
public class BrucieCore extends BrucieGame {

	/** Constructor needs BrucieConfig instance.
	 *
	 * @param config
	 */
	public BrucieCore(BrucieConfig config) {
		// Call super with the config. ALWAYS DO THIS FIRST
		super(config);

		// Set up the Scene Wrangler.
		// The core engine uses this to instantiate the Scenes
		// of your game. Set up the Scenes in the brucie/scenes.json file
		Wrangler<Scene> w = new Wrangler<Scene>(this);
		w.setJsonFile(config.scenes_json);
		sceneWrangler = w;

		// Set up feature wrangler.
		// Again, used to find feature handlers see brucie/features.json
		// GameFeature instances are singletons.
		SingletonWrangler<GameFeature> sw = new SingletonWrangler<GameFeature>(this);
		sw.setJsonFile(config.features_json);
		featureWrangler = sw;
	}

}
