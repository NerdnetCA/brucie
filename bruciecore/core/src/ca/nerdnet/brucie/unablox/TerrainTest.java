package ca.nerdnet.brucie.unablox;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import ca.nerdnet.brucie.core.Scene;

public class TerrainTest extends Scene {
    ImmediateModeRenderer renderer;
    TerrainChunk chunk;
    Mesh mesh;
    PerspectiveCamera camera;
    Vector3 intersection = new Vector3();
    boolean intersected = false;
    long lastTime = System.nanoTime();
    private ShaderProgram shader;
    Texture texture;

    private Matrix4 matrix = new Matrix4();

    String vertexShader = "attribute vec4 a_position;    \n" + "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
            + "uniform mat4 u_worldView;\n" + "varying vec4 v_color;" + "varying vec2 v_texCoords;"
            + "void main()                  \n" + "{                            \n" + "   v_color = a_color; \n"
            + "   v_texCoords = a_texCoord0; \n" + "   gl_Position =  u_worldView * a_position;  \n"
            + "}                            \n";
    String fragmentShader = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n" + "varying vec4 v_color;\n"
            + "varying vec2 v_texCoords;\n" + "uniform sampler2D u_texture;\n" + "void main()                                  \n"
            + "{                                            \n" + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
            + "}";

    @Override
    public void show () {
        //renderer = new ImmediateModeRenderer();

        chunk = new TerrainChunk(32, 32, 4);

        Random rand = new Random();
        int len = chunk.vertices.length;
        for (int i = 3; i < len; i += 4)
            chunk.vertices[i] = Color.toFloatBits(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), 255);

        mesh = new Mesh(true, chunk.vertices.length / 3, chunk.indices.length, new VertexAttribute(VertexAttributes.Usage.Position,
                3, "a_position"), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"));

        mesh.setVertices(chunk.vertices);
        mesh.setIndices(chunk.indices);

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 5, 5);
        camera.direction.set(0, 0, 0).sub(camera.position).nor();
        camera.near = 0.5f;
        camera.far = 300;

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) {
            Gdx.app.log("ShaderTest", shader.getLog());
            Gdx.app.exit();
        }
        texture = new Texture(Gdx.files.internal("brucie/splash.png"));

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

    @Override public void render (float delta) {
        GL20 gl = Gdx.gl20;
        gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gl.glEnable(GL20.GL_DEPTH_TEST);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        //camera.apply(gl);
        gl.glBlendColor(1, 1, 1, 1);

        shader.begin();

        texture.bind();

        shader.setUniformMatrix("u_worldView", matrix);
        shader.setUniformi("u_texture", 0);

        mesh.render(shader ,GL20.GL_TRIANGLES);

        Gdx.gl20.glFlush();
        shader.end();

        /*if (intersected) {
            gl.glPointSize(10);
            renderer.begin(GL20.GL_POINTS);
            renderer.color(1, 0, 0, 1);
            renderer.vertex(intersection.x, intersection.y, intersection.z);
            renderer.end();
        }*/

        handleInput(Gdx.input, Gdx.graphics.getDeltaTime());

        if (System.nanoTime() - lastTime > 1000000000) {
            Gdx.app.log("TerrainTest", "fps: " + Gdx.graphics.getFramesPerSecond());
            lastTime = System.nanoTime();
        }
    }

    private void handleInput (Input input, float delta) {
        if (input.isTouched()) {
            Ray ray = camera.getPickRay(input.getX(), input.getY());
            if (Intersector.intersectRayTriangles(ray, chunk.vertices, chunk.indices, 4, intersection)) intersected = true;
        } else {
            intersected = false;
        }

        if (input.isKeyPressed(Keys.W)) camera.position.z -= delta;
        if (input.isKeyPressed(Keys.S)) camera.position.z += delta;
        if (input.isKeyPressed(Keys.A)) camera.position.x -= delta;
        if (input.isKeyPressed(Keys.D)) camera.position.x += delta;
        if (input.isKeyPressed(Keys.Q)) camera.position.y += delta;
        if (input.isKeyPressed(Keys.E)) camera.position.y -= delta;
    }

    @Override
    public void preload() {
        loadAsset("brucie/splash.png",Texture.class);
    }

    final static class TerrainChunk {
        public final byte[] heightMap;
        public final short width;
        public final short height;
        public final float[] vertices;
        public final short[] indices;
        public final int vertexSize;

        public TerrainChunk (int width, int height, int vertexSize) {
            if ((width + 1) * (height + 1) > Short.MAX_VALUE)
                throw new IllegalArgumentException("Chunk size too big, (width + 1)*(height+1) must be <= 32767");

            this.heightMap = new byte[(width + 1) * (height + 1)];
            this.width = (short)width;
            this.height = (short)height;
            this.vertices = new float[heightMap.length * vertexSize];
            this.indices = new short[width * height * 6];
            this.vertexSize = vertexSize;

            buildIndices();
            buildVertices();
        }

        public void buildVertices () {
            int heightPitch = height + 1;
            int widthPitch = width + 1;

            int idx = 0;
            int hIdx = 0;
            int inc = vertexSize - 3;

            for (int z = 0; z < heightPitch; z++) {
                for (int x = 0; x < widthPitch; x++) {
                    vertices[idx++] = x;
                    vertices[idx++] = heightMap[hIdx++];
                    vertices[idx++] = z;
                    idx += inc;
                }
            }
        }

        private void buildIndices () {
            int idx = 0;
            short pitch = (short)(width + 1);
            short i1 = 0;
            short i2 = 1;
            short i3 = (short)(1 + pitch);
            short i4 = pitch;

            short row = 0;

            for (int z = 0; z < height; z++) {
                for (int x = 0; x < width; x++) {
                    indices[idx++] = i1;
                    indices[idx++] = i2;
                    indices[idx++] = i3;

                    indices[idx++] = i3;
                    indices[idx++] = i4;
                    indices[idx++] = i1;

                    i1++;
                    i2++;
                    i3++;
                    i4++;
                }

                row += pitch;
                i1 = row;
                i2 = (short)(row + 1);
                i3 = (short)(i2 + pitch);
                i4 = (short)(row + pitch);
            }
        }
    }

}

