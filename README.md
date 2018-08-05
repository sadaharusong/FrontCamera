# FrontCamera

打开前置摄像头并且实时获取数据
首先需要用到的是一个叫Camera类的，更新的一个叫Camera2的类。

Camera2是从 SDK 5.0（API Level 21）开始才被引入的。里面的方法有些还是比原来的方便许多的，但是，用户系统版本参差不齐，所以暂时还是使用Camera来进行开发。

------

# SufaceView

这个View可以直接重内存或者DMA等硬件接口获取所得的图像数据，是个非常重要的绘图容器，所以，开发相机应用一般都是使用它。



SurfaceView可以在主线程之外的线程中向屏幕绘图，这样可以避免画图任务繁重的时候，造成主线程的阻塞，从而提高了程序的反应速度。



使用SurfaceView，一般要实现一个SurfaceHolder.Callback接口，通过此接口。因为使用SurfaceView，所有的绘图工作必须得在Surface被创建之后才能开始，而在Surface被销毁之前必须结束，所以Callback中的surfaceCreated和surfaceDestroyed就变成了绘图处理代码的边界。



需要重写的方法：
``` java
surfaceChanged(SurfaceHolder holderformatwidthheight){}

surfaceCreated(SurfaceHolder holder){}

surfaceDestroyed(SurfaceHolder holder) {}
```

SurfaceHolder，可以把它看成是surface的控制器，用来操纵surface。处理它的Canvas上画的效果和动画，控制表面，大小，像素等等。

一般步骤：

- 检测并访问摄像头，判断摄像头是否可访问

- 创建预览，SurfaceView控件预览，并且实现SurfaceHolder接口。此控件能预览摄像的实时图像。

- 其他二次开发操作：如设置监听来拍照，保存照片啊，等等其他开发。

- 释放摄像头，摄像头使用完毕，应用程序必须正确的释放，方便第二次调用或其他程序使用。

------
# Camera
Camera类中的一些处理都是通过Callback来进行的：

``` java
mCamera.autoFocus(mAutoFocusCallback)
```

其中mAutoFocusCallback继承自Camera.AutoFocusCallback接口，用户可以自定义的是对焦完成后的操作（比如延迟拍照等）；



Camera类中的一些处理都是通过一堆Callback来进行的：

``` java
mCamera.takePicture(shutterCallbackrawCallbackjpegCallback)
```

比如实现拍照的操作

``` java
mCamera.setPreviewCallback(previewCallback)
```

这个方法可以获取到相机实时处理的Callback，然后通过重写其中的方法获取到相机通过摄像头获取的实时数据，这个就对二次开发十分重要，这个Callback也是十分重要的。


注意事项：

初始化相机时要先遍历设备上相机的个数，然后再通过

``` java
cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT
```

去选择是前置还是后置，再用

``` java
Camera.open(camIdx);
```

带参数的open(index)方法打开。



若要处理实时获取的数据，建议新开一个AsyncTask来完成，把耗时操作放入doInBackground方法里。

``` java
 @Override
    protected Object doInBackground(Object[] params) {}
```

记得在使用完后或者切换界面时需要释放相机资源，把Callback设置为空。

``` java
mCamera.setPreviewCallback(null);
mCamera.stopPreview();
mCamera.release();
mCamera = null
```
