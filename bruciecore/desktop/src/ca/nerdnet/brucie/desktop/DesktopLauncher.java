package ca.nerdnet.brucie.desktop;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.GameTemplate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// This sets the window size
		// Future: matching up resolutions becomes important in some situations.
		//   some kind of resolution management might be nice here
		config.width = 1024;
		config.height = 680;

		// Create BrucieConfig instance. This remains the same across platforms at
		// this time, but does not need to.
		BrucieConfig bconfig = new BrucieConfig();

		// Create game
		GameTemplate game = new GameTemplate(bconfig);

		// Register any dynamic features
		//game.registerFeature("NULL");

		// Launch the LibGDX app
		new LwjglApplication(game, config);
	}
}
