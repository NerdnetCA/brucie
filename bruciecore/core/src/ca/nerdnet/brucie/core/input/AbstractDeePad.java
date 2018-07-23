package ca.nerdnet.brucie.core.input;

import com.badlogic.gdx.math.Vector2;

public abstract class AbstractDeePad implements DeePad {


    public boolean isUp() {
        return (getState() & PAD_UP)!=0;
    }
    public boolean isDown() {
        return (getState() & PAD_DOWN)!=0;
    }
    public boolean isLeft() {
        return (getState() & PAD_LEFT)!=0;
    }
    public boolean isRight() {
        return (getState() & PAD_RIGHT)!=0;
    }

    public void readVector(Vector2 vec) {
        int state = getState();
        int x=0, y=0;
        if((state & PAD_UP) != 0) {
            y=1;
        } else if((state & PAD_DOWN) != 0) {
            y=-1;
        } else if((state & PAD_LEFT) != 0) {
            x=-1;
        } else if((state & PAD_RIGHT) != 0) {
            x=1;
        }
        vec.set(x,y);
        vec.nor();
    }
}
