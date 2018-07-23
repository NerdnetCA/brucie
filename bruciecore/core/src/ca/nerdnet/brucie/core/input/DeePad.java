package ca.nerdnet.brucie.core.input;

import com.badlogic.gdx.math.Vector2;

public interface DeePad {
    public static final int PAD_UP = 0x01;
    public static final int PAD_DOWN = 0x02;
    public static final int PAD_LEFT = 0x04;
    public static final int PAD_RIGHT = 0x08;

    void reset();
    int getState();

    boolean isUp();
    boolean isDown();
    boolean isLeft();
    boolean isRight();

    void readVector(Vector2 vec);

}
