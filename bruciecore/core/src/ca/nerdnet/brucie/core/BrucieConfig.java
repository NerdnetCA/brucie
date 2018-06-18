package ca.nerdnet.brucie.core;

/**
 * Created by colin on 7/24/17.
 */

public class BrucieConfig {
    public static final String scenes_json = "brucie/scenes.json";
    public static final String features_json = "brucie/features.json";
    public static final String loading_img = "brucie/loading.png";

    private static String scene_config_file = scenes_json;

    public static int getVWidth() {
        return 1024;
    }
    public static int getVHeight() {
        return 680;
    }

    public static String getSceneConfig() {
        return scene_config_file;
    }
    public static void setSceneConfig(String configFile) {
        scene_config_file = configFile;
    }
}
