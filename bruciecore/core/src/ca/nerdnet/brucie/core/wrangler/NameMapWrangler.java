package ca.nerdnet.brucie.core.wrangler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

import ca.nerdnet.brucie.core.BrucieGame;

public class NameMapWrangler<T> implements Wrangler<T> {
    private static final String TAG = "NAMEMAPWRANGLER";

    protected ObjectMap<String, String> classnameMap;

    protected BrucieGame mBrucieGame;

    private Config mConfig;

    public void config(WranglerConfig wranglerConfig) {
        if(wranglerConfig instanceof Config) {
            mConfig = (Config)wranglerConfig;
            mBrucieGame = mConfig.brucieGame;
        } else {
            // Unrecognized config object.
            // TODO: Handle this error gracefully.
        }
    }

    @Override
    public void initialize() {
        classnameMap = new ObjectMap<String, String>();
        if (mConfig.jsonFile != null) {
            Json json = new Json();
            FileHandle fh = Gdx.files.internal(mConfig.jsonFile);
            Array<JsonValue> defs = json.fromJson(Array.class, fh);
            Iterator<JsonValue> iter = defs.iterator();
            while (iter.hasNext()) {
                JsonValue mapdef = iter.next();
                NameMapDef wdef = json.readValue(NameMapDef.class, mapdef);

                if (wdef.name != null)
                    classnameMap.put(wdef.name, wdef.classname);
            }
        }
    }

    @Override
    public T wrangle(String key, WrangleParams params) {
        // sanity check
        if(classnameMap != null) {
            String classname = classnameMap.get(key);

            if(classname != null) {
                try {
                    Gdx.app.log(TAG,"Wrangling class '"+classname+"' as "+key);
                    T inst = (T) Class.forName(classname).getConstructor().newInstance();
                    if (inst instanceof WrangledObject) {
                        ((WrangledObject) inst).configure(mBrucieGame,params);
                    }
                    return inst;
                } catch (Exception e ) {
                    // Couldn't instantiate
                    Gdx.app.log(TAG,"Unable to instantiate: " + classname);
                    e.printStackTrace();
                }
            } else {
                Gdx.app.log(TAG,"No class found for " + key);
            }
            return null;


        } else {
            // TODO: Throw a nice exception?
        }
        return null;
    }

    public static class Config extends WranglerConfig {
        public String jsonFile;

        public Config(BrucieGame game, String jsonFile) {
            this.brucieGame = game;
            this.jsonFile = jsonFile;
        }
    }

    public static class NameMapDef {
        public String name;
        public String classname;
    }
}
