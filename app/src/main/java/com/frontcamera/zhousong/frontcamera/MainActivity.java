package com.frontcamera.zhousong.frontcamera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

public class MainActivity extends Activity {
    Context context = MainActivity.this;
    SurfaceView surfaceView;
    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView()
    {
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mCameraSurfaceHolder.setCameraSurfaceHolder(context,surfaceView);
    }

}
