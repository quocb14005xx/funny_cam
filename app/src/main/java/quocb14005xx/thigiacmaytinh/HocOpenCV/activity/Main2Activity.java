package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.opencv.features2d.FeatureDetector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;
import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;
import quocb14005xx.thigiacmaytinh.HocOpenCV.view.MyImageView;


//Main2Activity xử lý với ảnh từ thư viện
public class Main2Activity extends AppCompatActivity {
    private Bitmap bitmap_root;//xử lý trên ảnh này
    private MyImageView imageView;//show trong view này

    private int REQUEST_CODE = 1234;
    //click Quay ảnh
    private int deg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                bitmap_root = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap_root);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //load anh goc
        LayAnhTuIntroActivity();



    }

    /**
     * can lay bitmap dang ve ra thi goi {@link MyImageView#getBitmap()}
     */

    //lấy ảnh từ intent bên intro trong thư viện qua đây
    private void LayAnhTuIntroActivity() {
        Uri dataBitmap = Uri.parse(getIntent().getStringExtra("image_selected"));
        try {
            InputStream stream = getContentResolver().openInputStream(dataBitmap);
            bitmap_root = BitmapFactory.decodeStream(stream);
            if (bitmap_root.getWidth() > 1280 || bitmap_root.getHeight() > 1280)//nếu ảnh đầu vào mà size lớn quá thì resize lại để xử lý
            {
                bitmap_root = Bitmap.createScaledBitmap(bitmap_root, 1280, 960, true);
            }

            stream.close();
            imageView = findViewById(R.id.imageShow);
            imageView.setImageBitmap(bitmap_root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //click Undo
    public void XuLyUnDo(View view) {
    }

    //click Redo
    public void XuLyReDo(View view) {

    }

    public void XuLyQuayAnh(View view) {
        deg=deg+90;
        imageView.setRotatebitmap(deg);
        if (deg==360)
        {
            deg=0;
        }
    }

    //click save ảnh đã chỉnh sửa
    public void XuLySaveBitmap(View view) {
        try {
            FileOutputStream outputStream = new FileOutputStream(MyContants.RecordHinhAnh_filepath());
            Bitmap bitmap = imageView.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            bitmap.recycle(); //->crash

            Toast.makeText(this, "Đã lưu rồi", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //click để thay đổi ảnh khác
    public void XuLyThayDoiAnhKhac(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //click cắt ảnh
    private boolean isCutOk;
    public void XuLyCutAnh(View view) {
        if (!isCutOk)
        {
            imageView.setModeCut(true);
        }
        else
        {
            imageView.cutBitMap();
        }
        isCutOk=!isCutOk;
    }

}
