package com.frontcamera.zhousong.frontcamera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

/**
 * Created by zhousong on 2016/9/18.
 * 相机类，相机的调用
 */
public class FrontCamera {
    static final String TAG = "Camera";
    Camera mCamera;
    int mCurrentCamIndex = 0;
    boolean previewing;

    public void setCamera(Camera camera)
    {
        this.mCamera = camera;
    }

    public int getCurrentCamIndex()
    {
        return this.mCurrentCamIndex;
    }

    public boolean getPreviewing()
    {
        return this.previewing;
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.i(TAG, "已经获取了bitmap:" + bitmap.toString());
            previewing = false;
            //需要保存可执行下面
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filePath = ImageUtil.getSaveImgePath();
                    File file = new File(filePath);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file, true);
                        fos.write(data);
                        ImageUtil.saveImage(file, data, filePath);
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();*/
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();//重新开启预览 ，不然不能继续拍照
            previewing = true;
        }


    };

    //初始化相机
    public Camera initCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        previewing = true;

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            //在这里打开的是前置摄像头,可修改打开后置OR前置
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    /**
     * 停止相机
     * @param mCamera 需要停止的相机对象
    * */
    public void StopCamera(Camera mCamera) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        previewing = false;
    }

    /**
     * 旋转屏幕后自动适配（若只用到竖的，也可不要）
     * 已经在manifests中让此Activity只能竖屏了
     * @param activity 相机显示在的Activity
     * @param cameraId 相机的ID
     * @param camera 相机对象
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
