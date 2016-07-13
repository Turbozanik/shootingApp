package by.roman.shootingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    FeatureDetector blobDetector;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int widthPix = (int) Math.ceil(dm.widthPixels * (dm.densityDpi / 160.0));
                    int height = (int) Math.ceil(dm.heightPixels * (dm.densityDpi/160)); //533 dip
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setMaxFrameSize(widthPix, height);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    static {
        if (!OpenCVLoader.initDebug()) {
            System.loadLibrary("opencv_java3"); //load opencv_java lib

            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
        }
    }

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            Log.d(TAG, "Everything should be fine with using the camera.");
        } else {
            Log.d(TAG, "Requesting permission to use the camera.");
            String[] CAMERA_PERMISSONS = {
                    Manifest.permission.CAMERA
            };
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSONS, 0);
        }


        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
//        //blobDetector.create(FeatureDetector.SIMPLEBLOB);
//        Mat lowerRed = new Mat();
//        Mat upperRed = new Mat();
//        Mat redHueImage = new Mat();
//        Mat green = new Mat();
//        Mat greenHueImage = new Mat();
//        Mat blue = new Mat();
//        Mat blueHueImage = new Mat();
//        Mat white = new Mat();
//        Mat whiteHueImage = new Mat();
//        Mat orange;
//
//        Mat rgba = inputFrame.rgba();
//        //Core.flip(rgba, rgba, 1);
//        Mat HSVMat = new Mat();
//        Imgproc.medianBlur(rgba,rgba,3);
//        Imgproc.cvtColor(rgba, HSVMat, Imgproc.COLOR_RGB2HSV, 0);
//
//        Mat frameH = new Mat();
//        Imgproc.threshold(frameH, frameH, 155, 160, Imgproc.THRESH_BINARY);
//        // Imgproc.threshold(frameS, frameS, 0, 100, Imgproc.THRESH_BINARY);
//        Mat frameV = new Mat();
//        Imgproc.threshold(frameV, frameV, 250, 256, Imgproc.THRESH_BINARY);
//
//        Core.inRange(HSVMat,new Scalar(0,70,100),new Scalar(10,255,255),lowerRed);
//        Core.inRange(HSVMat,new Scalar(160,70,50),new Scalar(180,255,255),upperRed);
//        Core.addWeighted(lowerRed,1.0,upperRed,1.0,0.0,redHueImage);
//
//        Core.inRange(HSVMat,new Scalar(34,50,50),new Scalar(80,220,200),green);
//        Core.addWeighted(redHueImage,1.0,green,1.0,0.0,greenHueImage);
//
//        Core.inRange(HSVMat,new Scalar(92,100,100),new Scalar(124,255,255),blue);
//        Core.addWeighted(greenHueImage,1.0,blue,1.0,0.0,blueHueImage);
//
//        Core.inRange(HSVMat,new Scalar(0,0,100),new Scalar(10,255,255),white);
//        Core.addWeighted(blueHueImage,1.0,white,1.0,0.0,whiteHueImage);
//
//
//        Imgproc.GaussianBlur(blueHueImage, blueHueImage, new Size(9, 9), 2, 2);
//        double dp = 1.2d;
//        double minDist = 100;
//        int minRadius = 10;
//        int maxRadius = 100;
//        double param1 = 70, param2 = 72;
//        Mat circles = new Mat();
//
//
//        Imgproc.HoughCircles(blueHueImage, circles, Imgproc.HOUGH_GRADIENT, dp, blueHueImage.rows()/8, param1, param2, minRadius, maxRadius);
//        int numCircles = (circles.rows() == 0) ? 0 : circles.cols();
//        for (int i = 0; i < numCircles; i++) {
//            double[] circleCoordinates = circles.get(0, i);
//            int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];
//            Point center = new Point(x, y);
//            int radius = (int) circleCoordinates[2];
//            Imgproc.circle(rgba, center, radius, new Scalar(0, 255, 0), 4);
//        }
//        circles.release();
//        lowerRed.release();
//        upperRed.release();
//        HSVMat.release();
//        redHueImage.release();
//
//        return rgba;
        List<MatOfPoint> list = new ArrayList<>();
        Mat frame = new Mat();
        Mat gray = new Mat();
        Mat hierarchy = new Mat();
        Mat originalFrame = inputFrame.rgba();


        Imgproc.medianBlur(originalFrame,originalFrame,3);
        Imgproc.cvtColor(originalFrame, gray, Imgproc.COLOR_RGB2GRAY, 0);
        Imgproc.threshold(gray,frame,220,255,0);
        Imgproc.findContours(frame,list,hierarchy,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

        double dp = 1.2d;
        double minDist = 100;
        int minRadius = 10;
        int maxRadius = 100;
        double param1 = 70, param2 = 72;

        for(MatOfPoint point: list) {
            Rect rect = Imgproc.boundingRect(point);
            if(rect.width<70 && rect.height<70) {
                Imgproc.rectangle(originalFrame, rect.tl(), rect.br(), new Scalar(255, 255, 0), 20, 8, 0);
            }
            rect = null;
        }
        frame.release();
        gray.release();
        hierarchy.release();
        list.clear();
        //System.gc();

        return originalFrame;
    }

}