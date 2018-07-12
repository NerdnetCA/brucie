package ca.nerdnet.brucie.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class CoreShader implements Shader {

    private ShaderProgram program;
    private Camera mCamera;
    private RenderContext mContext;
    private int u_projViewTrans;
    private int u_worldTrans;

    @Override
    public void init() {
        String vshad = Gdx.files.internal("testb/vertshad01.glsl").readString();
        String fshad = Gdx.files.internal("testb/fragshad01.glsl").readString();
        program = new ShaderProgram(vshad,fshad);
        u_projViewTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        mCamera = camera;
        mContext = context;
        program.begin();
        program.setUniformMatrix(u_projViewTrans, mCamera.combined);
        mContext.setDepthTest(GL20.GL_LEQUAL);
        mContext.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
    }
}
