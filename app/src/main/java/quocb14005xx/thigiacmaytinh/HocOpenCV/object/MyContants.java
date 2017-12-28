package quocb14005xx.thigiacmaytinh.HocOpenCV.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;

/**
 * Created by quocb14005xx on 12/21/2017.
 */

public class MyContants {
    public static final String TAG="ABCD_QUOC";

    public static final String PATH_IMAGE= Environment.getExternalStorageDirectory()+"/HocOpenCV/hinhanh/";
    public static final String PATH_VIDEO= Environment.getExternalStorageDirectory()+"/HocOpenCV/video/";


    public static final int FRONT_CAM=98;
    //public static final int FLIP_FRONT_CAM=1;


    public static final int BACK_CAM=99;
   // public static final int FLIP_BACK_CAM=0;

    public static final int NONE_MODE = 0;
    public static final int GRAY_MODE = 1;
    public static final int CANNY_MODE = 2;
    public static final int BLUR_MODE = 3;
    public static final int DETECT_FACE_MODE=4;


    public static String RecordVideo_filepath() {
        File vrdir = new File(PATH_VIDEO);
        String mTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(vrdir, mTimeStamp + ".avi");
        String filepath = file.getAbsolutePath();
        Log.e(TAG, filepath);
        return filepath;
    }

    public static String RecordHinhAnh_filepath() {
        File vrdir = new File(PATH_IMAGE);
        String mTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(vrdir, mTimeStamp + ".jpeg");
        String filepath = file.getAbsolutePath();
        Log.e(TAG, filepath);
        return filepath;
    }


    public static final int STICKER1=0;
    public static final int STICKER2=1;
    public static final int STICKER3=2;
    public static final int STICKER4=3;
    public static final int STICKER5=4;


}
