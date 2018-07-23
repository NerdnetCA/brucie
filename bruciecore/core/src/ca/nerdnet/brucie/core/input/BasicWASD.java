package ca.nerdnet.brucie.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class BasicWASD extends AbstractDeePad implements DeePad, InputProcessor {

    private int mState;

    private int keyFor_UP = Input.Keys.W;
    private int keyFor_LEFT = Input.Keys.A;
    private int keyFor_DOWN = Input.Keys.R;
    private int keyFor_RIGHT = Input.Keys.S;

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public void reset() {
        mState = 0;
    }

    public void configureKeys(String json) {
        // TODO: Implement this!
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean processed = false;
        if(keycode == keyFor_UP) {
            mState |= DeePad.PAD_UP;
            processed = true;
        }
        if(keycode == keyFor_DOWN) {
            mState |= DeePad.PAD_DOWN;
            processed = true;
        }
        if(keycode == keyFor_LEFT) {
            mState |= DeePad.PAD_LEFT;
            processed = true;
        }
        if(keycode == keyFor_RIGHT) {
            mState |= DeePad.PAD_RIGHT;
            processed = true;
        }
        return processed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean processed = false;
        if(keycode == keyFor_UP) {
            mState &= ~DeePad.PAD_UP;
            processed = true;
        }
        if(keycode == keyFor_DOWN) {
            mState &= ~DeePad.PAD_DOWN;
            processed = true;
        }
        if(keycode == keyFor_LEFT) {
            mState &= ~DeePad.PAD_LEFT;
            processed = true;
        }
        if(keycode == keyFor_RIGHT) {
            mState &= ~DeePad.PAD_RIGHT;
            processed = true;
        }
        return processed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
