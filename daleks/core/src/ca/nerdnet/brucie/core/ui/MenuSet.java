package ca.nerdnet.brucie.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.Iterator;

import ca.nerdnet.brucie.core.BrucieGame;
import ca.nerdnet.brucie.core.BrucieListener;

public class MenuSet {

    // Private finals
    private final BrucieGame myGame;
    private final BrucieListener myListener;
    private final Skin mySkin;

    // Data map
    private ObjectMap<String, MenuDef> myMenus;

    // settables
    private float framePad = 25f;
    private float buttonMargin = 40f;
    private float panelWidth = 500f;

    /**
     *
     * @param game
     * @param skin
     * @param listener
     */
    public MenuSet(BrucieGame game, Skin skin, BrucieListener listener) {
        myGame = game;
        mySkin = skin;
        myListener = listener;
    }

    /**
     *
     * @param name name of menu (ex. MAIN)
     * @return
     */
    public Panel getPanel(String name) {
        Panel p = null;

        /* Get the menu definition */
        MenuDef menu = myMenus.get(name);
        if(menu != null) {
            PanelBuilder pb = new PanelBuilder(myGame, mySkin, myListener);

            // setup builder params
            pb.setTitle(menu.title).setFramePad(framePad)
                    .setButtonMargin(buttonMargin)
                    .setWidth(panelWidth);

            for(MenuItem i : menu.items) {
                pb.addButton(i.title,i.name);
            }

            // build panel
            p = pb.build();
        }

        // return panel, or null if menu was not in the map.
        return p;
    }

    /** Read JSON file and create menuset name/data map,
      * discarding any old data.
      *
      * @param filename
     */

    public void readJson(String filename) {
        FileHandle fh = Gdx.files.internal(filename);
        Json json = new Json();
        Array<JsonValue> defs = json.fromJson(Array.class, fh);
        ObjectMap<String,MenuDef> newMap = new ObjectMap<String, MenuDef>();
        Iterator<JsonValue> iter = defs.iterator();
        while(iter.hasNext()) {
            JsonValue tdef = iter.next();
            MenuDef mdef = json.readValue(MenuDef.class,tdef);
            if(mdef.name != null) {
                newMap.put(mdef.name, mdef);
            }
        }
        myMenus = newMap;
    }

    /* setters */
    public void setFramePad(float value) {
        framePad = value;
    }
    public void setButtonMargin(float value) {
        buttonMargin = value;
    }
    public void setPanelWidth(float value) {
        panelWidth = value;
    }

    /* getters are here for completeness. I don't think we need these */
    public float getFramePad() {
        return framePad;
    }
    public float getButtonMargin() {
        return buttonMargin;
    }
    public float getPanelWidth() {
        return panelWidth;
    }

    /* POJOs for holding the JSON data

     */
    public static class MenuDef {
        public String name;
        public String title;
        public ArrayList<MenuItem> items;
    }

    public static class MenuItem {
        public String name;
        public String title;
    }
}
