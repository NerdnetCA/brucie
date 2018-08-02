package ca.nerdnet.brucie;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import ca.nerdnet.brucie.core.*;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		BrucieConfig.setSceneConfig("brucie/scenes_mastertest.json");

		GameTemplate b = new GameTemplate(new BrucieConfig());


		initialize(b , config);
	}
}
