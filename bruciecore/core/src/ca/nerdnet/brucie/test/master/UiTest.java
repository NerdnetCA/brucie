package ca.nerdnet.brucie.test.master;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class UiTest extends Scene implements BrucieListener {
    private static final String TAG = "UITEST";

    private static final int MAIN = 1;
    private static final int BUTTONS = 2;
    private static final int SLIDERS = 3;
    private static final int SCROLL = 4;
    private static final int FIELD = 5;
    private static final int SELECT = 6;

    // Managed Assets
    private Skin mySkin;

    // Disposables
    private UiStage myUiStage;
    private TextureAtlas myAtlas;

    private Tools tools;

    private Group uiGroup;
    private Actor currentPanel;
    private int currentPanelNumber;

    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("humbaba/humbaba_ui.json", Skin.class);
    }

    @Override
    public void show() {
        super.show();
        mySkin = assetManager.get("humbaba/humbaba_ui.json", Skin.class);
        myAtlas = assetManager.get("humbaba/humbaba_ui.atlas", TextureAtlas.class);
        myUiStage = new UiStage();

        tools = new Tools();

        Image bg = new Image(new TextureRegionDrawable(myAtlas.findRegion("gradient")));
        bg.setWidth(1024);
        bg.setHeight(680);
        myUiStage.addActor(bg);

        uiGroup = new Group();
        uiGroup.setPosition(0,0);
        uiGroup.setSize(1024,680);

        Actor ui = tools.makeui_main();
        currentPanel = ui;
        currentPanelNumber = MAIN;
        uiGroup.addActor(ui);

        myUiStage.addActor(uiGroup);

        Actor a = makeBackButton();
        myUiStage.addActor(a);

        setFadeIn();
    }

    @Override
    public void render(float delta) {
        // Clear the 'screen'.  Remember to clear the depth buffer when rendering 3d
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Insert Test render code here


        myUiStage.act(delta);
        myUiStage.draw();

        super.render(delta);
    }

    public Actor makeui() {
        Table rv = null;

        ImageButton imageButton, imageButton2;
        TextButton textButton;
        Label topLabel;
        CheckBox checkBox;
        Slider slider;
        TextField textField;
        SelectBox selectBox;
        List list;

        rv = new Table(mySkin);
        rv.setBackground("gradient");
        rv.setFillParent(true);
        rv.align(Align.top);

        topLabel = new Label("Humbaba Ui Test",mySkin,"default");
        rv.add(topLabel).colspan(2);

        rv.row().padTop(20);
        textButton = new TextButton("Button 1",mySkin,"default");
        rv.add(textButton).colspan(2);

        rv.row().padTop(10);
        imageButton = new ImageButton(mySkin,"button_save");
        rv.add(imageButton);
        imageButton2 = new ImageButton(mySkin,"default");
        Drawable d = new TextureRegionDrawable(myAtlas.findRegion("icon_save"));
        imageButton2.getStyle().imageUp = d;
        rv.add(imageButton2);

        rv.row().padTop(10);
        Button toggleButton = new Button(mySkin, "toggle");
        toggleButton.add("tsr");
        rv.add(toggleButton).colspan(2);

        rv.row().padTop(10);
        textField = new TextField("text",mySkin,"default");
        rv.add(textField).colspan(2);

        rv.row().padTop(10);
        slider = new Slider(0,100,1,false,mySkin);
        rv.add(slider).colspan(2);

        rv.row().padTop(10);
        selectBox = new SelectBox(mySkin);
        selectBox.setItems("one","two","tree","help","plez","make","it","scrolly");
        selectBox.getScrollPane().setVariableSizeKnobs(false);
        rv.add(selectBox).colspan(2);

        rv.row().padTop(10);
        list = new List(mySkin);
        list.setItems("one","tree","shrubbery","poopery","nunnery","long list","plz scroll","plz?","pleeeez");
        ScrollPane listScroll = new ScrollPane(list,mySkin);
        listScroll.setFlickScroll(false);
        listScroll.setVariableSizeKnobs(false);
        rv.add(listScroll).colspan(2);

        return rv;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        myUiStage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private Table makeBackButton(){
        Table t = new Table(mySkin);
        t.pad(20);
        t.setFillParent(true);
        t.align(Align.bottomRight);

        Button backButton = new Button(mySkin, "plain_blue");
        backButton.add(new Label("Back", mySkin));
        backButton.addListener(new ButtonEventAdapter(this,"back"));
        t.add(backButton).height(64);
        t.layout();
        return t;
    }

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("back".equals(action)) {
            if(currentPanelNumber == MAIN) {
                myGame.queueScene("BEGIN");
                setFadeOut();
            } else {
                Actor ui = tools.makeui_main();
                currentPanel.remove();
                currentPanel = ui;
                uiGroup.addActor(ui);
                currentPanelNumber = MAIN;
            }
            return true;
        } else if("btn_buttontests".equals(action)) {
            Actor ui = tools.makeui_buttontests();
            currentPanel.remove();
            currentPanel = ui;
            uiGroup.addActor(ui);
            currentPanelNumber = BUTTONS;
        } else if("btn_slidertests".equals(action)) {
            Actor ui = tools.makeui_slidertests();
            currentPanel.remove();
            currentPanel = ui;
            uiGroup.addActor(ui);
            currentPanelNumber = SLIDERS;
        } else if("btn_scrolltests".equals(action)) {
            Actor ui = tools.makeui_scrolltests();
            currentPanel.remove();
            currentPanel = ui;
            uiGroup.addActor(ui);
            currentPanelNumber = SCROLL;
        } else if("btn_textfieldtests".equals(action)) {
            Actor ui = tools.makeui_fieldtests();
            currentPanel.remove();
            currentPanel = ui;
            uiGroup.addActor(ui);
            currentPanelNumber = FIELD;
        } else if("btn_selectiontests".equals(action)) {
            Actor ui = tools.makeui_selectiontests();
            currentPanel.remove();
            currentPanel = ui;
            uiGroup.addActor(ui);
            currentPanelNumber = SELECT;
        }
        return false;
    }

    public class Tools {

        public Actor makeui_main() {
            Table rv = new Table(mySkin);

            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);

            // Heading
            Label heading = new Label("UI Test",mySkin,"heading1");
            rv.add(heading).colspan(1);

            rv.row().padTop(10);
            Button b1 = new TextButton("Button Tests",mySkin,"default");
            b1.addListener(new ButtonEventAdapter(UiTest.this,"btn_buttontests"));
            rv.add(b1);

            rv.row().padTop(10);
            Button b2 = new TextButton("Slider Tests",mySkin,"default");
            b2.addListener(new ButtonEventAdapter(UiTest.this,"btn_slidertests"));
            rv.add(b2);

            rv.row().padTop(10);
            Button b3 = new TextButton("Scroll Tests",mySkin,"default");
            b3.addListener(new ButtonEventAdapter(UiTest.this,"btn_scrolltests"));
            rv.add(b3);

            rv.row().padTop(10);
            Button b4 = new TextButton("Textfield Tests",mySkin,"default");
            b4.addListener(new ButtonEventAdapter(UiTest.this,"btn_fieldtests"));
            rv.add(b4);

            rv.row().padTop(10);
            Button b5 = new TextButton("SelectBox Tests",mySkin,"default");
            b5.addListener(new ButtonEventAdapter(UiTest.this,"btn_selectiontests"));
            rv.add(b5);

            return rv;
        }

        public Actor makeui_buttontests() {
            Table rv = new Table(mySkin);
            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);

            Button b;
            ImageButton ib;
            Drawable d;

            Label heading = new Label("Button Tests",mySkin,"heading1");
            rv.add(heading).colspan(3);

            rv.row().padTop(10);
            Label label_colours = new Label("Buttons come in 7 colours",mySkin,"default");
            rv.add(label_colours).colspan(3);
            rv.row().padTop(5);
            b = new TextButton("Button Blue/Cerul",mySkin,"square_bordered_bluecerul");
            rv.add(b);
            b = new TextButton("Button Red/Orange",mySkin,"square_bordered_redorange");
            rv.add(b);
            b = new TextButton("Button Cerul/Bone",mySkin,"square_bordered_cerulbone");
            rv.add(b);

            rv.row().padTop(10);
            Label label_borders = new Label("Bordered and unbordered", mySkin, "default");
            rv.add(label_borders).colspan(3);
            rv.row().padTop(5);
            b = new TextButton("Button Green/Orange",mySkin,"square_plain_greenorange");
            rv.add(b);
            b = new TextButton("Button Bone/Yellow",mySkin,"square_plain_boneyellow");
            rv.add(b);
            b = new TextButton("Button Yellow/Green",mySkin,"square_plain_yellowgreen");
            rv.add(b);

            rv.row().padTop(10);
            Label label_shape = new Label("Also round...", mySkin, "default");
            rv.add(label_shape).colspan(3);
            rv.row().padTop(5);
            ib = new ImageButton(mySkin,"round_plain_orangered");
            d = new TextureRegionDrawable(myAtlas.findRegion("icon_buttons"));
            ib.getStyle().imageUp = d;
            rv.add(ib);
            ib = new ImageButton(mySkin,"round_bordered_cerulbone");
            d = new TextureRegionDrawable(myAtlas.findRegion("icon_home"));
            ib.getStyle().imageUp = d;
            rv.add(ib);
            b = new TextButton("Disabled",mySkin,"square_plain_boneyellow");
            b.setDisabled(true);
            rv.add(b);

            return rv;
        }

        public Actor makeui_slidertests() {
            Table rv = new Table(mySkin);
            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);

            Label heading = new Label("Slider Tests",mySkin,"heading1");
            rv.add(heading).colspan(3);

            rv.row().padTop(10).height(350);
            Slider s = new Slider(0,100,1,true,mySkin,"default-vertical");
            rv.add(s);

            rv.row().padTop(10);
            ProgressBar progressBar = new ProgressBar(0,100,1,false,mySkin,"default-horizontal");
            progressBar.setValue(80);
            rv.add(progressBar);

            return rv;
        }
        public Actor makeui_scrolltests() {
            Table rv = new Table(mySkin);
            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);

            Label heading = new Label("Scroll Tests",mySkin,"heading1");
            rv.add(heading);

            rv.row().height(200);
            List<String> list = new List(mySkin,"plain");
            list.setItems("One","Two","Three","Four","I","Declare","A","Scrolling","List");
            ScrollPane scrollPane = new ScrollPane(list,mySkin,"framed");
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false,true);
            //scrollPane.setVariableSizeKnobs(false);
            rv.add(scrollPane);

            return rv;
        }

        public Actor makeui_fieldtests() {
            Table rv = new Table(mySkin);
            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);

            return rv;
        }

        public Actor makeui_selectiontests() {
            Table rv = new Table(mySkin);
            rv.setFillParent(true);
            rv.align(Align.top);
            rv.padTop(10);



            return rv;
        }
    }
}
