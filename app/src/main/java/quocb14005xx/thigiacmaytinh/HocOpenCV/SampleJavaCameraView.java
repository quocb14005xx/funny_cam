package quocb14005xx.thigiacmaytinh.HocOpenCV;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SampleJavaCameraView extends JavaCameraView {
    private CascadeClassifier mCascade;
    private MatOfRect faces;
    private Bitmap santaHat, santaFull, santaRia, background_detect;

    //ham khoi tao
    public SampleJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }


    public int chieuRongCamera()
    {
        return mCamera.getParameters().getPictureSize().width;
    }

    public int chieuDaiCamera()
    {
        return mCamera.getParameters().getPictureSize().height;
    }


    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    //phuong thuc chup anh tu camera opencv,,, (ds1: path save anh ,ds2: goc fix anh, ds3: mode la id cua thuat toan duoig ham XuLyHieuUng)
    public void takePicture(final String fileName, final int deg, final int mode) {
        PictureCallback callback = new PictureCallback() {

            private String mPictureFileName = fileName;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
                picture = XuLyHieuUng(picture, mode);
                try {
                    FileOutputStream out = new FileOutputStream(mPictureFileName);
                    Matrix m = new Matrix();
                    m.postRotate(deg);
                    picture = Bitmap.createScaledBitmap(picture, picture.getWidth(), picture.getHeight(), true);
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    picture.recycle();
                    mCamera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mCamera.takePicture(null, null, callback);
    }


    //xu ly cac hieu ung tra ve 1 bitmap (ds1: hinh bitmap dau vao, ds2: int mode cua giai thuat nao hieu ung nao)
    private Bitmap XuLyHieuUng(Bitmap input, int mode) {
        Bitmap output = Bitmap.createBitmap(input, 0, 0, input.getWidth(), input.getHeight());

        Mat mRgba = new Mat();
        Mat mGray = new Mat();
        Mat mIntermediate = new Mat();

        Utils.bitmapToMat(output, mRgba);//chuyen bitmap dau vao thanh Mat de xu ly hieu ung

        switch (mode) {
            case MyContants.NONE_MODE:
                output = input;
                break;
            case MyContants.GRAY_MODE:
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGBA2GRAY, 4);
                break;
            case MyContants.CANNY_MODE:
                Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY, 4);
                Imgproc.GaussianBlur(mGray, mGray, new Size(5, 5), 2, 2);
                Imgproc.Canny(mGray, mIntermediate, 35, 75);
                Imgproc.cvtColor(mIntermediate, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
                break;
            case MyContants.BLUR_MODE:
                break;
            case MyContants.DETECT_FACE_MODE:
                mRgba = FD(mRgba, 1);
                break;
        }
        Utils.matToBitmap(mRgba, output);//xử lý với Mat xong trả về bitmap để save lại
        return output;
    }



    //phuong thuc nhan dien return bitmap (doi so 1: Mat rgba cua anh, doi so 2 : id cua icon can ghep)
    private Mat FD(Mat matInput, int icon) {
        // CaiDatCascade();

        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = getContext().getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            mCascade=new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mat matInputGray = new Mat();
        faces = new MatOfRect();
        Imgproc.cvtColor(matInput, matInputGray, Imgproc.COLOR_RGBA2GRAY, 4);

        if (mCascade != null) {
            int height = matInputGray.rows();
            double faceSize = (double) height * 0.25;
            mCascade.detectMultiScale(matInputGray, faces, 1.1, 2, 10, new Size(faceSize, faceSize), new Size());
            Rect[] facesArray = faces.toArray();

            background_detect = Bitmap.createBitmap(matInputGray.width(), matInputGray.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(matInput, background_detect);
            Canvas canvas = new Canvas(background_detect);

            if (facesArray.length > 0) {
                for (int i = 0; i < facesArray.length; i++) {
                    //Imgproc.rectangle(mGray, facesArray[i].tl(), facesArray[i].br(), new Scalar(255,0,0), 3);

                    int temp = 300;
                    float xHat = (float) (facesArray[i].tl().x) - (temp / 2);
                    float yHat = (float) (facesArray[i].tl().y) - (temp / 2);

                    //scale thu nho phong to phu hop face
                    Bitmap scaleHat = Bitmap.createScaledBitmap(MainActivity.santaFull,
                            facesArray[i].width + temp,
                            temp + facesArray[i].height,
                            true);
                    canvas.drawBitmap(scaleHat, xHat, yHat, null);
                }
                Utils.bitmapToMat(background_detect, matInput);
            }
        }//mCascade!=null
        return matInput;
    }

}