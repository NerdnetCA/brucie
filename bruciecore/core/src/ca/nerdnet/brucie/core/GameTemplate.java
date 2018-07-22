package ca.nerdnet.brucie.core;

import ca.nerdnet.brucie.core.wrangler.CachedNameWrangler;
import ca.nerdnet.brucie.core.wrangler.NameMapWrangler;

/**
 * Game Template.
 *
 * Replace the MyGdxGame class supplied by the gdx-setup template with this, and
 * edit if needed for wrangler setup.
 */
public class GameTemplate extends BrucieGame {

    /** Constructor. Saves the configure object in a protected field.
     *  If the parameter is null, a new configure will be created.
     *
     * @param config BrucieConfig instance
     */
    public GameTemplate(BrucieConfig config) {
        if(config==null) {
            brucieConfig = new BrucieConfig();
        } else {
            brucieConfig = config;
        }

        // Set up the Scene Wrangler.
        // The core engine uses this to instantiate the Scenes
        // of your game. Set up the Scenes in the brucie/scenes.json file
        NameMapWrangler<Scene> w = new NameMapWrangler<Scene>();
        w.configure(new NameMapWrangler.Config(this,brucieConfig.getSceneConfig()));
        sceneWrangler = w;

        // Set up feature wrangler.
        // Again, used to find feature handlers see brucie/features.json
        // GameFeature instances are singletons.
        CachedNameWrangler<GameFeature> sw = new CachedNameWrangler<GameFeature>();
        sw.configure(new NameMapWrangler.Config(this,brucieConfig.features_json));
        featureWrangler = sw;
    }

}
