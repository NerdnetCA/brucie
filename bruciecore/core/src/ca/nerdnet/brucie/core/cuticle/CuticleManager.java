package ca.nerdnet.brucie.core.cuticle;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ca.nerdnet.brucie.core.BrucieGame;

public class CuticleManager {
    private static final String TAG = "CUTICLEMANAGER";

    public static final int GREY = 0;
    public static final int BLUE = 1;
    public static final int RED = 2;
    public static final int CERUL = 3;
    public static final int YELLOW = 4;
    public static final int ORANGE = 5;
    public static final int GREEN = 6;
    public static final int BONE = 7;



    private final BrucieGame mGame;
    private Skin mSkin;
    private boolean dirtyConfig = true;

    private int buttonColourUp, buttonColourDown;
    private int buttonAltColourUp, buttonAltColourDown;
    private int sliderKnobColour;

    public CuticleManager(BrucieGame game) {
        mGame = game;
    }


    public void setButtonColourNormal(int col) {
        dirtyConfig = true;
        buttonColourUp = col;
    }
    public void setButtonColourActive(int col) {
        dirtyConfig = true;
        buttonColourDown = col;
    }
    public void setButtonAltColourNormal(int col) {
        dirtyConfig = true;
        buttonAltColourUp = col;
    }
    public void setButtonAltColourActive(int col) {
        dirtyConfig = true;
        buttonAltColourDown = col;
    }
    public void setSliderKnobColour(int col) {
        dirtyConfig = true;
        sliderKnobColour = col;
    }


    public Skin getSkin() {
        Skin rv;

        if(mSkin != null) rv = mSkin;
        else {
            rv = mGame.getAssetManager().get("", Skin.class);
            mSkin = rv;
        }
        if(dirtyConfig) {
            // if the config is not synced, rebuild the styles
            rebuild_style();
        }

        return rv;
    }

    private void rebuild_style() {
        rebuild_buttonstyles();
        rebuild_sliderstyles();
        //rebuild_scrollstyles();
        //rebuild_framestyles();
        //rebuild_
    }

    private void rebuild_sliderstyles() {
        Drawable knobVertDraw;
        Drawable knobHorzDraw;

        Slider.SliderStyle sliderStyleVert;
        Slider.SliderStyle sliderStyleHorz;

        knobHorzDraw = getSliderKnobHorz(sliderKnobColour);
        knobVertDraw = getSliderKnobVert(sliderKnobColour);

        sliderStyleHorz = mSkin.get("default-horizontal", Slider.SliderStyle.class);
        sliderStyleVert = mSkin.get("default-vertical", Slider.SliderStyle.class);

        sliderStyleHorz.knob = knobHorzDraw;
        sliderStyleVert.knob = knobVertDraw;
    }

    private void rebuild_buttonstyles() {

        Drawable upDraw;
        Drawable downDraw;
        Drawable disDraw;

        Button.ButtonStyle buttonStyle;
        TextButton.TextButtonStyle textButtonStyle;
        ImageButton.ImageButtonStyle imageButtonStyle;

        upDraw = getSquareBorderButton(buttonColourUp);
        downDraw = getSquareBorderButton(buttonColourDown);
        disDraw = getSquareBorderButton(GREY);
        buttonStyle = mSkin.get("square_bordered", Button.ButtonStyle.class);
        textButtonStyle = mSkin.get("square_bordered", TextButton.TextButtonStyle.class);
        imageButtonStyle = mSkin.get("square_bordered", ImageButton.ImageButtonStyle.class);
        buttonStyle.up = upDraw;
        buttonStyle.down = downDraw;
        buttonStyle.disabled = disDraw;
        textButtonStyle.up = upDraw;
        textButtonStyle.down = downDraw;
        textButtonStyle.disabled = disDraw;
        imageButtonStyle.up = upDraw;
        imageButtonStyle.down = downDraw;
        imageButtonStyle.disabled = disDraw;

        upDraw = getSquarePlainButton(buttonColourUp);
        downDraw = getSquarePlainButton(buttonColourDown);
        disDraw = getSquarePlainButton(GREY);
        buttonStyle = mSkin.get("square", Button.ButtonStyle.class);
        textButtonStyle = mSkin.get("square", TextButton.TextButtonStyle.class);
        imageButtonStyle = mSkin.get("square", ImageButton.ImageButtonStyle.class);
        buttonStyle.up = upDraw;
        buttonStyle.down = downDraw;
        buttonStyle.disabled = disDraw;
        textButtonStyle.up = upDraw;
        textButtonStyle.down = downDraw;
        textButtonStyle.disabled = disDraw;
        imageButtonStyle.up = upDraw;
        imageButtonStyle.down = downDraw;
        imageButtonStyle.disabled = disDraw;

        upDraw = getRoundBorderButton(buttonColourUp);
        downDraw = getRoundBorderButton(buttonColourDown);
        disDraw = getRoundBorderButton(GREY);
        buttonStyle = mSkin.get("round_bordered", Button.ButtonStyle.class);
        textButtonStyle = mSkin.get("round_bordered", TextButton.TextButtonStyle.class);
        imageButtonStyle = mSkin.get("round_bordered", ImageButton.ImageButtonStyle.class);
        buttonStyle.up = upDraw;
        buttonStyle.down = downDraw;
        buttonStyle.disabled = disDraw;
        textButtonStyle.up = upDraw;
        textButtonStyle.down = downDraw;
        textButtonStyle.disabled = disDraw;
        imageButtonStyle.up = upDraw;
        imageButtonStyle.down = downDraw;
        imageButtonStyle.disabled = disDraw;

        upDraw = getRoundPlainButton(buttonColourUp);
        downDraw = getRoundPlainButton(buttonColourDown);
        disDraw = getRoundPlainButton(GREY);
        buttonStyle = mSkin.get("round", Button.ButtonStyle.class);
        textButtonStyle = mSkin.get("round", TextButton.TextButtonStyle.class);
        imageButtonStyle = mSkin.get("round", ImageButton.ImageButtonStyle.class);
        buttonStyle.up = upDraw;
        buttonStyle.down = downDraw;
        buttonStyle.disabled = disDraw;
        textButtonStyle.up = upDraw;
        textButtonStyle.down = downDraw;
        textButtonStyle.disabled = disDraw;
        imageButtonStyle.up = upDraw;
        imageButtonStyle.down = downDraw;
        imageButtonStyle.disabled = disDraw;


    }


    private Drawable getSliderKnobHorz(int colour) {
        Drawable rv = null;

        switch (colour) {
            case BLUE:
                rv = mSkin.getDrawable("slider_knob_horz_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("slider_knob_horz_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("slider_knob_horz_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("slider_knob_horz_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("slider_knob_horz_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("slider_knob_horz_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("slider_knob_horz_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("slider_knob_horz_grey");
                break;
        }
        return rv;
    }

    private Drawable getSliderKnobVert(int colour) {
        Drawable rv = null;

        switch (colour) {
            case BLUE:
                rv = mSkin.getDrawable("slider_knob_vert_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("slider_knob_vert_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("slider_knob_vert_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("slider_knob_vert_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("slider_knob_vert_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("slider_knob_vert_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("slider_knob_vert_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("slider_knob_vert_grey");
                break;
        }
        return rv;
    }

    private Drawable getSquarePlainButton(int colour) {
        Drawable rv = null;

        switch(colour) {
            case BLUE:
                rv = mSkin.getDrawable("button_square_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("button_square_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("button_square_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("button_square_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("button_square_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("button_square_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("button_square_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("button_square_grey");
                break;
        }
        return rv;
    }
    private Drawable getSquareBorderButton(int colour) {
        Drawable rv = null;

        switch(colour) {
            case BLUE:
                rv = mSkin.getDrawable("button_border_square_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("button_border_square_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("button_border_square_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("button_border_square_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("button_border_square_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("button_border_square_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("button_border_square_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("button_border_square_grey");
                break;
        }
        return rv;
    }
    private Drawable getRoundPlainButton(int colour) {
        Drawable rv = null;

        switch(colour) {
            case BLUE:
                rv = mSkin.getDrawable("button_round_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("button_round_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("button_round_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("button_round_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("button_round_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("button_round_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("button_round_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("button_round_grey");
                break;
        }
        return rv;
    }
    private Drawable getRoundBorderButton(int colour) {
        Drawable rv = null;

        switch(colour) {
            case BLUE:
                rv = mSkin.getDrawable("button_border_round_blue");
                break;
            case RED:
                rv = mSkin.getDrawable("button_border_round_red");
                break;
            case CERUL:
                rv = mSkin.getDrawable("button_border_round_cerul");
                break;
            case GREEN:
                rv = mSkin.getDrawable("button_border_round_green");
                break;
            case YELLOW:
                rv = mSkin.getDrawable("button_border_round_yellow");
                break;
            case ORANGE:
                rv = mSkin.getDrawable("button_border_round_orange");
                break;
            case BONE:
                rv = mSkin.getDrawable("button_border_round_bone");
                break;
            case GREY:
                rv = mSkin.getDrawable("button_border_round_grey");
                break;
        }
        return rv;
    }
}
