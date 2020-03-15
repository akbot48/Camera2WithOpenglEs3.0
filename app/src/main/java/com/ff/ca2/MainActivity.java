package com.ff.ca2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ff.ca2.cameras.FitCenterCameraView;
import com.ff.ca2.render.GLRender;

public class MainActivity extends AppCompatActivity implements GLRender.TextureListener {

    FitCenterCameraView textureView;
    Capture capture;
    Button btnTake;
    GLSurfaceView glSurfaceView;
    private final int CONTEXT_CLIENT_VERSION = 3;
    SurfaceTexture surfaceTexture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = findViewById(R.id.textureView);
        capture = new Capture(this, textureView);
        btnTake = findViewById(R.id.take);
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture.takePicture();
            }
        });
        if (detectOpenGLES30()) {
            Toast.makeText(this, "支持opengl", Toast.LENGTH_SHORT).show();
            glSurfaceView = findViewById(R.id.glSurfaceView);
            glSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
            GLRender render = new GLRender(this);
            glSurfaceView.setRenderer(render);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        glSurfaceView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        glSurfaceView.onPause();
    }

    private boolean detectOpenGLES30() {
        ActivityManager am =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x30000);
    }

    @Override
    public void textureCreated(int textureId) {
        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setDefaultBufferSize(glSurfaceView.getWidth(), glSurfaceView.getHeight());
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                glSurfaceView.requestRender();
            }
        });
        capture.setGlSurfaceTexture(surfaceTexture);
    }

    @Override
    public void onDrawFrame() {
        surfaceTexture.updateTexImage();
    }
}
