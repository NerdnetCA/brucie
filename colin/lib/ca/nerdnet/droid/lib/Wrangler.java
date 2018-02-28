package ca.nerdnet.droid.lib;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by colin on 2/28/18.
 */

public abstract class Wrangler<T> {
    protected HashMap<String, Class<T>> classmap;

    public abstract T wrangleInstance
            (String key, @Nullable Bundle extra)
            throws WranglerException;

}
