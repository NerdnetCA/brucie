package ca.nerdnet.brucie.core.wrangler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

import ca.nerdnet.brucie.core.BrucieGame;

/**
 * Wrangler for singleton classes.
 */

public class CachedNameWrangler<T> extends NameMapWrangler<T> implements CachedWrangler<T> {
    private static final String TAG = "SINGLETONWRANGLER";

    protected ObjectMap<String, T> objectMap;

    public CachedNameWrangler() {
        objectMap = new ObjectMap<String, T>();
    }

    public void register(String key, T inst) {
        objectMap.put(key, inst);
    }

    @Override
    public T wrangle(String name, WrangleParams param) {
        T inst = objectMap.get(name);

        if(inst != null) return inst;

        String classname = classnameMap.get(name);

        if(classname != null) {
            try {
                Gdx.app.log(TAG,"Wrangling class '"+classname+"' as "+name);
                inst = (T) Class.forName(classname).getConstructor().newInstance();
                if (inst instanceof WrangledObject) {
                    ((WrangledObject) inst).configure(mBrucieGame,param);
                }
                objectMap.put(name,inst);
                return inst;
            } catch (Exception e ) {
                // Couldn't instantiate
                Gdx.app.log(TAG,"Unable to instantiate " + classname);
            }
        } else {
            Gdx.app.log(TAG,"No class found for " + name);
        }
        return null;
    }

}
