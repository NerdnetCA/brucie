package ca.nerdnet.brucie.test.testa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;
import ca.nerdnet.brucie.core.ui.ButtonEventAdapter;
import ca.nerdnet.brucie.core.ui.UiStage;

public class GroundMesh extends Scene implements BrucieListener, RenderableProvider {
    private static final String TAG = "BASIC3D";

    // Camera settings
    private static final float FOV = 67;
    private static final float CAM_X = 8f;
    private static final float CAM_Y = 3f;
    private static final float CAM_Z = 8f;

    // Terrain mesh size
    private static final int MESH_X = 9;
    private static final int MESH_Z = 9;

    // floats per vertex
    private static final int VERTEX_SIZE = 8;

    // vertices per terrain quadrangle
    // This is 6 because 2 triangles * 3 verts each
    private static final int QUAD_SIZE = 6;


    // Managed assets
    private Skin mySkin;
    private Texture groundTexture;

    // Disposables
    private UiStage myUiStage;
    private Mesh groundMesh;
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;

    // Other
    private Material groundMaterial;
    private PerspectiveCamera camera;
    private float[] cachedVertices;
    private short[] cachedIndices;
    private int meshSize;

    private DirectionalLight myLight;
    private Environment environment;
    private FrameBuffer fbo;
    private Texture clothTexture;


    @Override
    public void dispose() {
        // Always dispose your disposables
        if(myUiStage != null) myUiStage.dispose();
        if(groundMesh != null) groundMesh.dispose();
        if(modelBatch != null) modelBatch.dispose();
        if(spriteBatch != null) spriteBatch.dispose();
        super.dispose();
    }

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json", Skin.class);
        loadAsset("testa/earth_cracked.png",Texture.class);
        loadAsset("testa/cloth_01.png",Texture.class);
    }

    @Override
    public void show() {
        mySkin = assetManager.get("ui/ctulublu_ui.json", Skin.class);
        groundTexture = assetManager.get("testa/earth_cracked.png",Texture.class);
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        clothTexture = assetManager.get("testa/cloth_01.png",Texture.class);
        clothTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        myUiStage = new UiStage();

        Actor a = makeBackButton();
        myUiStage.addActor(a);

        groundMaterial = new Material(ColorAttribute.createDiffuse(Color.BLUE));

        /*fbo = new FrameBuffer(Pixmap.Format.RGB565,FBO_RESOLUTION,FBO_RESOLUTION,false);
        spriteBatch = new SpriteBatch();
        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false,FBO_RESOLUTION,FBO_RESOLUTION);
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
*/

        //groundMaterial = new Material(ColorAttribute.createDiffuse(Color.BLUE));
        groundMaterial = new Material();
        Texture t = fbo.getColorBufferTexture();
        t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        groundMaterial.set(
                //TextureAttribute.createDiffuse(t)

                TextureAttribute.createDiffuse(clothTexture)
        );

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(CAM_X,CAM_Y,CAM_Z);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 50f;
        camera.update();

        modelBatch = new ModelBatch();

        myLight = new DirectionalLight();
        myLight.set(0.6f,0.6f,0.6f,-0.6f,-0.2f,-0.4f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
        environment.add(myLight);

        cachedVertices = new float[
                MESH_Z *
                MESH_X *
                VERTEX_SIZE
            ];
        cachedIndices = new short[
                (MESH_X -1) *
                (MESH_Z-1) *
                QUAD_SIZE
            ];

        groundMesh = buildMesh();

    }

    private float hmap(int x, int z) {
        if(x < 0 ||
                x >= MESH_X-1 ||
                z < 0 ||
                z >= MESH_Z-1
                ) { return 0f; }
        if(x%3==1) { return 0f; }
        else { return 0.5f; }
    }

    private Mesh buildMesh() {
        Mesh mesh;

        Vector3 u = new Vector3();
        Vector3 v = new Vector3();
        Vector3 n = new Vector3();

        int numverts=0;
        int iverts=0;
        int numindices=0;
        for(int mz=0; mz<MESH_Z; mz++) {
            for(int mx=0; mx<MESH_X; mx++) {
                cachedVertices[iverts++] = mx;
                cachedVertices[iverts++] = 0f;
                cachedVertices[iverts++] = mz;
                cachedVertices[iverts++] = 0f;
                cachedVertices[iverts++] = 1f;
                cachedVertices[iverts++] = 0f;
                cachedVertices[iverts++] = hmap(mx,mz);
                cachedVertices[iverts++] = mz;

                u.set(2,  hmap(mx+1, mz)-hmap(mx-1,mz),0);
                v.set(0, hmap(mx, mz+1)-hmap(mx,mz-1),2);

                v.crs(u);
                v.nor();
                cachedVertices[iverts++] = v.x;
                cachedVertices[iverts++] = v.y;
                cachedVertices[iverts++] = v.z;

                cachedVertices[iverts++] = (float)mx / ((float)MESH_X/2);
                cachedVertices[iverts++] = (float)mz / ((float)MESH_Z/2);

                numverts++;
            }
        }
        for(int mz=0; mz<MESH_Z-1; mz++) {
            for(int mx=0; mx<MESH_X-1; mx++) {
                short vb = (short)((mz * MESH_X) + mx);
                cachedIndices[numindices++] = vb;
                cachedIndices[numindices++] = (short)(vb+MESH_X);
                cachedIndices[numindices++] = (short)(vb+MESH_X+1);
                cachedIndices[numindices++] = (short)(vb+MESH_X+1);
                cachedIndices[numindices++] = (short)(vb+1);
                cachedIndices[numindices++] = vb;
            }
        }

        mesh = new Mesh( true,
                numverts,
                numindices,
                VertexAttribute.Position(),VertexAttribute.Normal(),
                VertexAttribute.TexCoords(0)
                );
        mesh.setVertices(cachedVertices);
        mesh.setIndices(cachedIndices);

        meshSize = numindices;

        return mesh;
    }


    @Override
    public void render(float delta) {
        fbo.begin();
        Gdx.gl20.glViewport(0,0,fbo.getWidth(),fbo.getHeight());
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(groundTexture,0,0,
                fbo.getWidth(),
                fbo.getHeight()
        );
        spriteBatch.end();
        fbo.end();

        Gdx.gl20.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(camera);
        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
        groundTexture.bind();

        modelBatch.render(this, environment);
        modelBatch.end();

        myUiStage.act(delta);
        myUiStage.draw();


    }

    @Override
    public void resize(int width, int height) {
        myUiStage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private Table makeBackButton(){
        Table t = new Table(mySkin);
        t.pad(20);
        t.setFillParent(true);
        t.align(Align.bottomRight);

        Button backButton = new Button(mySkin, "default");
        backButton.add(new Label("Back", mySkin));
        backButton.addListener(new ButtonEventAdapter(this,"back"));
        t.add(backButton);
        t.layout();
        return t;
    }

    @Override
    public boolean isDone() { return done; }
    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("back".equals(action)) {
            myGame.queueScene("BEGIN");
            done=true;
            return true;
        }
        return false;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        Renderable renderable = pool.obtain();
        renderable.material = groundMaterial;
        //renderable.worldTransform.set(mySpin);
        renderable.meshPart.mesh = groundMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = meshSize;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);

    }
}
