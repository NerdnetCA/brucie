package ca.nerdnet.brucie.unablox.exa;

import com.badlogic.gdx.assets.AssetManager;

import ca.nerdnet.brucie.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.IndexBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.StringBuilder;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VBOTest extends Scene {
    private static final String TAG = "VBOTEST";
    private Texture myAsset;

    ShaderProgram shader;
    Texture texture;
    Matrix4 matrix = new Matrix4();

    Mesh newVBOMesh;

    SpriteBatch batch;
    BitmapFont bitmapFont;
    StringBuilder stringBuilder;

    WindowedMean newCounter = new WindowedMean(100);
    WindowedMean oldCounter = new WindowedMean(100);

    WindowedMean newCounterStress = new WindowedMean(100);
    WindowedMean oldCounterStress = new WindowedMean(100);

    @Override
    public void dispose() {
        newVBOMesh.dispose();
        texture.dispose();
        shader.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("brucie/splash.png", Texture.class);
    }

    @Override
    public void show() {
        AssetManager assetManager = myGame.getAssetManager();
        //myAsset = assetManager.get("myasset.png", Texture.class);
        /*if (Gdx.gl30 == null) {
            throw new GdxRuntimeException("GLES 3.0 profile required for this test");
        }*/
        String vertexShader = "attribute vec4 a_position;    \n" + "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
                + "uniform mat4 u_worldView;\n" + "varying vec4 v_color;" + "varying vec2 v_texCoords;"
                + "void main()                  \n" + "{                            \n" + "   v_color = a_color; \n"
                + "   v_texCoords = a_texCoord0; \n" + "   gl_Position =  u_worldView * a_position;  \n"
                + "}                            \n";
        String fragmentShader = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n" + "varying vec4 v_color;\n"
                + "varying vec2 v_texCoords;\n" + "uniform sampler2D u_texture;\n" + "void main()                                  \n"
                + "{                                            \n" + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
                + "}";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) {
            Gdx.app.log("ShaderTest", shader.getLog());
            Gdx.app.exit();
        }

        int numSprites = 1000;
        int maxIndices =  numSprites * 6;
        int maxVertices = numSprites * 6;

        VertexAttribute[] vertexAttributes = new VertexAttribute[] {VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0)};

        VertexBufferObject newVBO =  new VertexBufferObject(false, maxVertices, vertexAttributes);

        IndexBufferObjectSubData newIndices = new IndexBufferObjectSubData(false, maxIndices);

        newVBOMesh = new Mesh(newVBO, newIndices, false) {};

        float[] vertexArray = new float[maxVertices * 9];
        int index = 0;
        int stride = 9 * 6;
        for (int i = 0; i < numSprites; i++) {
            addRandomSprite(vertexArray, index);
            index += stride;
        }
        short[] indexArray = new short[maxIndices];
        for (short i = 0; i < maxIndices; i++) {
            indexArray[i] = i;
        }

        newVBOMesh.setVertices(vertexArray);
        newVBOMesh.setIndices(indexArray);

        texture = new Texture(Gdx.files.internal("brucie/splash.png"));

        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        stringBuilder = new StringBuilder();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }


    private void addRandomSprite (float[] vertArray, int currentIndex) {
        float width = MathUtils.random(0.05f, 0.2f);
        float height = MathUtils.random(0.05f, 0.2f);
        float x = MathUtils.random(-1f, 1f);
        float y = MathUtils.random(-1f, 1f);
        float r = MathUtils.random();
        float g = MathUtils.random();
        float b = MathUtils.random();
        float a = MathUtils.random();

        vertArray[currentIndex++] = x;
        vertArray[currentIndex++] = y;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = 1;

        vertArray[currentIndex++] = x + width;
        vertArray[currentIndex++] = y;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 1;
        vertArray[currentIndex++] = 1;

        vertArray[currentIndex++] = x + width;
        vertArray[currentIndex++] = y + height;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 1;
        vertArray[currentIndex++] = 0;

        vertArray[currentIndex++] = x + width;
        vertArray[currentIndex++] = y + height;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 1;
        vertArray[currentIndex++] = 0;

        vertArray[currentIndex++] = x;
        vertArray[currentIndex++] = y + height;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = 0;

        vertArray[currentIndex++] = x;
        vertArray[currentIndex++] = y;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = r;
        vertArray[currentIndex++] = g;
        vertArray[currentIndex++] = b;
        vertArray[currentIndex++] = a;
        vertArray[currentIndex++] = 0;
        vertArray[currentIndex++] = 1;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        texture.bind();
        shader.begin();
        shader.setUniformMatrix("u_worldView", matrix);
        shader.setUniformi("u_texture", 0);

        long beforeNew = System.nanoTime();
        newVBOMesh.render(shader, GL20.GL_TRIANGLES);
        Gdx.gl.glFlush();
        newCounter.addValue((System.nanoTime() - beforeNew));
        shader.end();


        batch.begin();
        /*stringBuilder.setLength(0);
        stringBuilder.append("O Mean Time: ");
        stringBuilder.append(oldCounter.getMean());
        bitmapFont.draw(batch, stringBuilder, 0, 200);
        stringBuilder.setLength(0);
        stringBuilder.append("N Mean Time: ");
        stringBuilder.append(newCounter.getMean());
        bitmapFont.draw(batch, stringBuilder, 0, 200 - 20);

        float oldMean = oldCounter.getMean();
        float newMean = newCounter.getMean();

        float meanedAverage = newMean/oldMean;
        stringBuilder.setLength(0);
        stringBuilder.append("New VBO time as a percentage of Old Time: ");
        stringBuilder.append(meanedAverage);
        bitmapFont.draw(batch, stringBuilder, 0, 200 - 40);

        stringBuilder.setLength(0);
        stringBuilder.append("Stress: O Mean Time: ");
        stringBuilder.append(oldCounterStress.getMean());
        bitmapFont.draw(batch, stringBuilder, 0, 200 - 80);
        stringBuilder.setLength(0);
        stringBuilder.append("Stress: N Mean Time: ");
        stringBuilder.append(newCounterStress.getMean());
        bitmapFont.draw(batch, stringBuilder, 0, 200 - 100);

        float oldMeanStress = oldCounterStress.getMean();
        float newMeanStress = newCounterStress.getMean();

        float meanedStressAverage = newMeanStress/oldMeanStress;
        stringBuilder.setLength(0);
        stringBuilder.append("Stress: New VBO time as a percentage of Old Time: ");
        stringBuilder.append(meanedStressAverage);
        bitmapFont.draw(batch, stringBuilder, 0, 200 - 120);
*/

        batch.end();
    }



}
