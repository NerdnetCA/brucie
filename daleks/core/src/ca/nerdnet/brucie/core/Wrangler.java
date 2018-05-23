package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;


public class Wrangler<T> {
    private static final String TAG="WRANGLER";
    protected ObjectMap<String, String> classnameMap;

    protected BrucieGame myGame;

    private String jsonFile = null;

    public Wrangler(BrucieGame game) {
        myGame = game;
        classnameMap = new ObjectMap<String, String>();
    }

    public void setJsonFile(String internalFilename) {
        jsonFile = internalFilename;
    }

    public void initializeWrangleables() {
        if(jsonFile != null) {
            Json json = new Json();
            FileHandle fh = Gdx.files.internal(jsonFile);
            Array<JsonValue> defs = json.fromJson(Array.class, fh);
            Iterator<JsonValue> iter = defs.iterator();
            while(iter.hasNext()) {
                JsonValue mapdef = iter.next();
                WrangleDef wdef = json.readValue(WrangleDef.class,mapdef);

                if(wdef.name != null)
                    classnameMap.put(wdef.name,wdef.classname);
            }
        }
    }

    public void registerWrangleable(String name, String classname) {
        classnameMap.put(name, classname);
    }

    public Array<String> getKeys() {
        return classnameMap.keys().toArray();
    }
    public String getClassname(String key) {
        return classnameMap.get(key);
    }

    public T getWrangledInstance(String name,String param) {
        String classname = classnameMap.get(name);

        if(classname != null) {
            try {
                Gdx.app.log(TAG,"Wrangling class '"+classname+"' as "+name);
                T inst = (T) Class.forName(classname).getConstructor().newInstance();
                if (inst instanceof WrangledObject) {
                    ((WrangledObject) inst).configure(myGame,param);
                }
                return inst;
            } catch (Exception e ) {
                // Couldn't instantiate
                Gdx.app.log(TAG,"Unable to instantiate: " + classname);
            }
        } else {
            Gdx.app.log(TAG,"No class found for " + name);
        }
        return null;

    }
}
