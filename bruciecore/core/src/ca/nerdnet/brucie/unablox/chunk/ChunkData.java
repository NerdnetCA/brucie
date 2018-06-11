package ca.nerdnet.brucie.unablox.chunk;


/** ChunkData encapsulates the data representing one world chunk.
 *
 * This is a provisional skeleton implementation, currently.

 */
public class ChunkData {

    public final short size;

    protected byte[] mydata;

    public ChunkData(int size) {
        this.size = (short)size;
        mydata = new byte[size * size * size];
    }

    public void set(int x, int y, int z, byte v) {
        mydata[(x * size * size) + y * size + z] = v;
    }

    public byte get (int x, int y, int z) {
        return mydata[(x * size * size) + y * size + z];
    }
}
