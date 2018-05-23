package ca.nerdnet.brucie;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.BrucieGame;
import ca.nerdnet.brucie.core.GameFeature;
import ca.nerdnet.brucie.core.SingletonWrangler;
import ca.nerdnet.brucie.core.Wrangler;
import ca.nerdnet.brucie.core.Scene;

/** MyGdxGame.
 *
 * Replace the MyGdxGame generated by gdx-setup with this file,
 * then edit this file as appropriate.
 *
 * Inherited (accessible) fields from BrucieGame:
 *  sceneWrangler
 *  featureWrangler
 *  brucieConfig
 *  assetManager - (do we need this?)
 *
 *  Reviewed Sep 19, 2017 by colin
 */
public class MyGdxGame extends BrucieGame {
	/*
	 */

	public MyGdxGame(BrucieConfig config) {
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
		// GameFeature instances are treated as singletons.
		SingletonWrangler<GameFeature> sw = new SingletonWrangler<GameFeature>(this);
		sw.setJsonFile(config.features_json);
		featureWrangler = sw;
	}
}