package ca.nerdnet.brucie.unablox.terrain;

public class TerrainData {

    private final byte[] dataMatrix;
    private int dimension;

    public TerrainData(int dim) {
        dimension = dim;
        dataMatrix = new byte[dim * dim];
    }

    public byte getCell(int x, int y) {
        return dataMatrix[x + y * dimension];
    }

    public void putCell(int x, int y, byte value) {
        dataMatrix[x + y * dimension] = value;
    }
}
