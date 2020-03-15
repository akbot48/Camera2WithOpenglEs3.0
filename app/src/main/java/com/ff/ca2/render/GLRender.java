package com.ff.ca2.render;

import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.ff.ca2.ESShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer {

    // Handle to a program object
    private int mProgramObject;

    // Sampler location
    private int mSamplerLoc;

    private FloatBuffer mVertices;
    private ShortBuffer mIndices;
    // Additional member variables
    private int mWidth;
    private int mHeight;
    private int mTextureId;

    private final float[] mVerticesData =
            {
                    -0.5f, 0.5f, 0.0f, // Position 0
                    0.0f, 0.0f, // TexCoord 0
                    -0.5f, -0.5f, 0.0f, // Position 1
                    0.0f, 1.0f, // TexCoord 1
                    0.5f, -0.5f, 0.0f, // Position 2
                    1.0f, 1.0f, // TexCoord 2
                    0.5f, 0.5f, 0.0f, // Position 3
                    1.0f, 0.0f // TexCoord 3
            };

    private final short[] mIndicesData =
            {
                    0, 1, 2, 0, 2, 3
            };

    public GLRender(TextureListener listener) {
        this.listener = listener;
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);
    }

    private TextureListener listener;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        String vShaderStr =
                "#version 300 es              				\n" +
                        "layout(location = 0) in vec4 a_position;   \n" +
                        "layout(location = 1) in vec2 a_texCoord;   \n" +
                        "out vec2 v_texCoord;     	  				\n" +
                        "void main()                  				\n" +
                        "{                            				\n" +
                        "   gl_Position = a_position; 				\n" +
                        "   v_texCoord = a_texCoord;  				\n" +
                        "}                            				\n";

        String fShaderStr =
                "#version 300 es                                     \n" +
                        "#extension GL_OES_EGL_image_external : require      \n" +
                        "precision mediump float;                            \n" +
                        "in vec2 v_texCoord;                            	 \n" +
                        "layout(location = 0) out vec4 outColor;        \n" +
                        "uniform samplerExternalOES uTextureSampler;         \n" +
                        "void main()                                         \n" +
                        "{                                                   \n" +
                        "  outColor = texture( uTextureSampler, v_texCoord );\n" +
                        "}                                                   \n";

        // Load the shaders and get a linked program object
        mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);

        // Get the sampler location
        mSamplerLoc = GLES30.glGetUniformLocation(mProgramObject, "uTextureSampler");

        mTextureId = createSimpleTexture2D();
        listener.textureCreated(mTextureId);
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Set the viewport
        GLES30.glViewport(0, 0, mWidth, mHeight);

        // Clear the color buffer
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES30.glUseProgram(mProgramObject);

        // Load the vertex position
        mVertices.position(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT,
                false,
                5 * 4, mVertices);
        // Load the texture coordinate
        mVertices.position(3);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT,
                false,
                5 * 4,
                mVertices);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);

        // Bind the texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);

        // Set the sampler texture unit to 0
        GLES30.glUniform1i(mSamplerLoc, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, mIndices);
        listener.onDrawFrame();
    }

    private int createSimpleTexture2D() {
        int[] textureId = new int[1];
        //  Generate a texture object
        GLES30.glGenTextures(1, textureId, 0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId[0]);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
        return textureId[0];
    }

    public interface TextureListener {
        void textureCreated(int textureId);
        void onDrawFrame();
    }
}
