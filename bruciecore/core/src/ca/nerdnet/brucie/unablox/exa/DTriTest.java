package ca.nerdnet.brucie.unablox.exa;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;

import ca.nerdnet.brucie.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class DTriTest extends Scene {
    private static final String TAG = "DTRITEST";
    private Texture myAsset;

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void preload() {
        //loadAsset("myassset.png", Texture.class);
    }

    @Override
    public void show() {
        AssetManager assetManager = myGame.getAssetManager();
        //myAsset = assetManager.get("myasset.png", Texture.class);
        renderer = new ShapeRenderer();

        triangulate();
        System.out.println(seed);

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                seed = MathUtils.random.nextLong();
                System.out.println(seed);
                triangulate();
                return true;
            }

            public boolean mouseMoved (int screenX, int screenY) {
                triangulate();
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setColor(Color.RED);
        renderer.begin(ShapeType.Filled);
        for (int i = 0; i < points.size; i += 2)
            renderer.circle(points.get(i), points.get(i + 1), 4, 12);
        renderer.end();

        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeType.Line);
        for (int i = 0; i < triangles.size; i += 3) {
            int p1 = triangles.get(i) * 2;
            int p2 = triangles.get(i + 1) * 2;
            int p3 = triangles.get(i + 2) * 2;
            renderer.triangle( //
                    points.get(p1), points.get(p1 + 1), //
                    points.get(p2), points.get(p2 + 1), //
                    points.get(p3), points.get(p3 + 1));
        }
        renderer.end();

    }

    @Override
    public void resize(int width, int height) {
        renderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        renderer.updateMatrices();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private ShapeRenderer renderer;
    FloatArray points = new FloatArray();
    ShortArray triangles;
    DelaunayTriangulator trianglulator = new DelaunayTriangulator();
    long seed = MathUtils.random.nextLong();

    void triangulate () {
        // seed = 4139368480425561099l;
        // seed = 6559652580366669361l;
        MathUtils.random.setSeed(seed);

        int pointCount = 100;
        points.clear();
        for (int i = 0; i < pointCount; i++) {
            float value;
            do {
                value = MathUtils.random(10, 400);
            } while (points.contains(value));
            points.add(value);
            do {
                value = MathUtils.random(10, 400);
            } while (points.contains(value));
            points.add(value);
        }
        points.add(Gdx.input.getX());
        points.add(Gdx.graphics.getHeight() - Gdx.input.getY());

        triangles = trianglulator.computeTriangles(points, false);
    }


}
