package com.ff.ca2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ff.ca2.cameras.FitCenterCameraView;

public class MutileCaptrueActivity extends AppCompatActivity {

    FitCenterCameraView textureView;
    FitCenterCameraView textureView2;
    FitCenterCameraView textureView3;
    FitCenterCameraView textureView4;
    FitCenterCameraView textureView5;
    FitCenterCameraView textureView6;
    FitCenterCameraView textureView7;
    FitCenterCameraView textureView8;
    FitCenterCameraView textureView9;
    MutileCapture capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutile);

        textureView = findViewById(R.id.textureView);
        textureView2 = findViewById(R.id.textureView2);
        textureView3 = findViewById(R.id.textureView3);
        textureView4 = findViewById(R.id.textureView4);
        textureView5 = findViewById(R.id.textureView5);
        textureView6 = findViewById(R.id.textureView6);
        textureView7 = findViewById(R.id.textureView7);
        textureView8 = findViewById(R.id.textureView8);
        textureView9 = findViewById(R.id.textureView9);

        capture = new MutileCapture(this, new FitCenterCameraView[]{textureView, textureView2,
                textureView3, textureView4, textureView5, textureView6, textureView7
                , textureView8, textureView9});
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }
}
