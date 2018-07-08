package ca.nerdnet.brucie.voxal;

public interface IChunk {

    int getWidth();
    int getDepth();
    int getHeight();

    short get(int x, int y, int z);
    void set(int x, int y, int z, short value);

}
