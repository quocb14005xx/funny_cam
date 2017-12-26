package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;
import quocb14005xx.thigiacmaytinh.HocOpenCV.view.SampleJavaCameraView;
import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    // private  Canvas canvas;
    public static Bitmap santaHat, santaRia, santaFull, vnFlag, background_detect, background_touched;
    private CascadeClassifier mCascade;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mPictureEffectMenuItems;
    private SubMenu mPictureEffectsMenu;


    private SampleJavaCameraView mOpenCvCameraView;
    private MatOfRect faces;
    private Mat mRgba;
    private Mat mIntermediate;
    private Mat mGray;
    private Mat mTemp;
    private Canvas canvasTouch;


    private int mViewMode;
    private BaseLoaderCallback callback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case SUCCESS:

                    //cai dat cascade
                    try {

                        Context context = getApplicationContext();
                        santaHat = BitmapFactory.decodeResource(context.getResources(), R.drawable.noel_hat);
                        santaRia = BitmapFactory.decodeResource(context.getResources(), R.drawable.ria_noel2);
                        santaFull = BitmapFactory.decodeResource(context.getResources(), R.drawable.noel_1k1);
                        vnFlag = BitmapFactory.decodeResource(context.getResources(), R.drawable.vnflag);

                        InputStream inputStream = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
                        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
                        File cascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
                        FileOutputStream os = new FileOutputStream(cascadeFile);
                        byte[] buffer = new byte[4096];
                        int byteread;
                        while ((byteread = inputStream.read(buffer)) != -1) {
                            os.write(buffer, 0, byteread);
                        }
                        inputStream.close();
                        os.close();

                        mCascade = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if (mCascade.empty()) {
                            Log.e(MyContants.TAG, "load cascasde that bai!");
                        }

                        cascadeDir.delete();
                        cascadeFile.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /// cat dat cascade


                    mOpenCvCameraView.enableView();
                    break;

                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    private boolean clickChangeCam = true;//bien check click change camera
    private boolean isRecord = false;//mac dinh khong quay
    private float xTouch[]= new float[999], yTouch[]= new float[999] ;
    private boolean touching;
    private boolean touchingFirst = true;

    //vòng đời & override
    public MainActivity() {
        Log.e(MyContants.TAG, "Instance new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        mOpenCvCameraView = findViewById(R.id.camera_view);
        mOpenCvCameraView.setVisibility(View.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setOnTouchListener(this);


    }

    @Override
    protected void onPause() {

        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, callback);
        } else {
            callback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
        System.exit(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mOpenCvCameraView.isEffectSupported()) {
            List<String> effects = mOpenCvCameraView.getEffectList();
            if (effects == null) {
                Log.e(MyContants.TAG, "Thiết bị không hỗ trợ các hiệu ứng màu!");
                return true;
            }

            mColorEffectsMenu = menu.addSubMenu("Hiệu ứng màu");
            mEffectMenuItems = new MenuItem[effects.size()];
            int idx = 0;
            ListIterator<String> effectItr = effects.listIterator();
            while (effectItr.hasNext()) {
                String element = effectItr.next();
                Log.e(MyContants.TAG, element);
                mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, element);
                idx++;
            }
            /////////////////////////////////////
            ArrayList<String> effectPic = new ArrayList<>();
            effectPic.add("None");
            effectPic.add("Xám");
            effectPic.add("Canny");
            effectPic.add("Blur");
            effectPic.add("Thêm Sticker");
            mPictureEffectsMenu = menu.addSubMenu("Hiệu ứng ảnh");
            mPictureEffectMenuItems = new MenuItem[effectPic.size()];
            for (int i = 0; i < effectPic.size(); i++) {
                mPictureEffectMenuItems[i] = mPictureEffectsMenu.add(2, i, Menu.NONE, effectPic.get(i).toString());
            }
        }
        return true;
    }


    private void ClearCanvasTouch()//clear những thứ tự vẽ
    {
        for (int i=0;i<indexTouch;i++)
        {
            xTouch[i]=-999;yTouch[i]=-999;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == 1) {
            mOpenCvCameraView.setEffect((String) item.getTitle());
            Log.e(MyContants.TAG, (String) item.getTitle());
            Toast.makeText(this, mOpenCvCameraView.getEffect(), Toast.LENGTH_SHORT).show();
        } else if (item.getGroupId() == 2) {
            switch (item.getItemId()) {
                case 0:
                    mViewMode = MyContants.NONE_MODE;
                    ClearCanvasTouch();
                    break;//none mode
                //ở NODE MODE không có touching =false là vì mode này bình thường thì có thể touch để vẽ
                case 1:
                    mViewMode = MyContants.GRAY_MODE;
                    touching=false;//touching =false vì khi chỉnh sang các hiệu ứng thì ko tự edit
                    break;//gray mode
                case 2:
                    mViewMode = MyContants.CANNY_MODE;
                    touching=false;//touching =false vì khi chỉnh sang các hiệu ứng thì ko tự edit
                    break;//canny mode
                case 3:
                    mViewMode = MyContants.BLUR_MODE;
                    touching=false;//touching =false vì khi chỉnh sang các hiệu ứng thì ko tự edit
                    break;//blur mode
                case 4:
                    mViewMode = MyContants.DETECT_FACE_MODE;
                    touching=false;//touching =false vì khi chỉnh sang các hiệu ứng thì ko tự edit
                    break;//detect face mode
            }

        }
        return true;
    }


    private int indexTouch=0;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (mViewMode==MyContants.NONE_MODE)
            {
                touching = true;
            }
            xTouch[indexTouch] = motionEvent.getX();
            yTouch[indexTouch] = motionEvent.getY();
            indexTouch++;
        }
        // Log.e(MyContants.TAG,"touch :"+touching+"  -  "+motionEvent.getX()+" -"+motionEvent.getY());

        return true;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.e(MyContants.TAG, "wid-hei:" + mOpenCvCameraView.chieuRongCamera() + "-" + mOpenCvCameraView.chieuDaiCamera());

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
        mIntermediate = new Mat(height, width, CvType.CV_8UC4);
        mTemp = new Mat(height, width, CvType.CV_8UC4);


        faces = new MatOfRect();//list mat face

        //bitmap anh goc của detect
        background_detect = Bitmap.createBitmap(mRgba.width(), mRgba.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mRgba, background_detect);


        //background nay la bitmap cho cac xử ly ko phải là detect
        background_touched = Bitmap.createBitmap(mRgba.width(), mRgba.height(), Bitmap.Config.RGB_565);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mTemp.release();
        mGray.release();
        mIntermediate.release();
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        final int viewMode = mViewMode;
        switch (viewMode) {
            case MyContants.NONE_MODE:
                mRgba = inputFrame.rgba();
                break;
            case MyContants.GRAY_MODE:
                mRgba = inputFrame.gray();
                break;
            case MyContants.CANNY_MODE:
                Imgproc.GaussianBlur(mGray, mGray, new Size(5, 5), 2, 2);
                Imgproc.Canny(mGray, mIntermediate, 35, 75);
                Imgproc.cvtColor(mIntermediate, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
                break;
            case MyContants.BLUR_MODE:
                Imgproc.blur(mRgba, mRgba, new org.opencv.core.Size(40, 40));
                break;
            case MyContants.DETECT_FACE_MODE:
                if (mCascade != null) {
                    int height = mGray.rows();
                    double faceSize = (double) height * 0.25;
                    mCascade.detectMultiScale(mGray, faces, 1.1, 2, 10, new Size(faceSize, faceSize), new Size());
                    Rect[] facesArray = faces.toArray();

                    //để khởi tạo ở đây vì cameraframe luôn trả về nên phải khởi tạo mới bitmap ở 1 mRgba khác nhau
                    background_detect = Bitmap.createBitmap(mGray.width(), mGray.height(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(mRgba, background_detect);
                    Canvas canvas = new Canvas(background_detect);

                    if (facesArray.length > 0) {
                        for (int i = 0; i < facesArray.length; i++) {
                            //Imgproc.rectangle(mGray, facesArray[i].tl(), facesArray[i].br(), new Scalar(255,0,0), 3);

                            int temp = 300;
                            float xHat = (float) (facesArray[i].tl().x) - (temp / 2);
                            float yHat = (float) (facesArray[i].tl().y) - (temp / 2);

                            //scale thu nho phong to phu hop face
                            Bitmap scaleHat = Bitmap.createScaledBitmap(santaFull,
                                    facesArray[i].width + temp,
                                    temp + facesArray[i].height,
                                    true);
                            canvas.drawBitmap(scaleHat, xHat, yHat, null);
                        }
                        Utils.bitmapToMat(background_detect, mRgba);
                    }
                }
                break;
        }

        if (touching)//run khi đã chạm và phải chế độ NONE không bật bất kì hiệu ứng nào
        {
            Utils.matToBitmap(mRgba, background_detect);
            canvasTouch = new Canvas(background_detect);
            for (int i=0;i<indexTouch;i++)
            {
                canvasTouch.drawBitmap(vnFlag, xTouch[i], yTouch[i], null);
            }
            Utils.bitmapToMat(background_detect, mRgba);
        }

        ///////////////////////với camera trước thì phải lật ma trận ảnh lại
        if (clickChangeCam == false) {
            Core.flip(mRgba, mRgba, 1);
        }
        return mRgba;
    }


    /// button onclick
    public void TakePicture(View view) {
        String fileName = MyContants.RecordHinhAnh_filepath();
        if (!touching)//nếu là xử lý chạm thì chụp vs phương thức opencv và ngược lại là chụp bởi onTakepicture của thiết bị
        {
            if (clickChangeCam == false)//như describe ở dưới false là ở trạng thái cam front nên deg =-90
            {
                mOpenCvCameraView.takePicture(fileName, -90, mViewMode);
            } else {
                mOpenCvCameraView.takePicture(fileName, 90, mViewMode);
            }
        }
        else {
            Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGBA2BGR, 4);
            Imgcodecs.imwrite(fileName, mRgba);
        }
        Toast.makeText(this, "Chụp!", Toast.LENGTH_SHORT).show();
    }

    /*
    * khởi tạo clickChangeCam=true là vì khi run app camera là back khi click button thì sẽ chuyển thành front bởi vậy
    * trước tiên clickchangecam phải = true để click đc chuyển qua cam trước và set lại =false
    * ở trên onCameraFrame if(clickChangeCam==false) mới flip code =1 bởi vì đang giai đoạn của cam front nên flip 1
    * và không flip với cam back bởi vì mặc định nó đã đúng cho nên click cái nữa trở về true ko chạy flip code
    * */
    public void ThayDoiMayAnh(View view) {

        mOpenCvCameraView.disableView();
        if (clickChangeCam) {
            mOpenCvCameraView.setCameraIndex(MyContants.FRONT_CAM);
            clickChangeCam = false;
        } else {
            mOpenCvCameraView.setCameraIndex(MyContants.BACK_CAM);
            clickChangeCam = true;
        }
        mOpenCvCameraView.enableView();
    }


    public void QuayPhim(View view) {
        Toast.makeText(this, "Chưa phát triễn!", Toast.LENGTH_SHORT).show();
    }


    ///!button onclick


}
