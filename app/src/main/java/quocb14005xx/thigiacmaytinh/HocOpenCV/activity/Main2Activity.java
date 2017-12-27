package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;


//Main2Activity xử lý với ảnh từ thư viện
public class Main2Activity extends AppCompatActivity {
    private Bitmap bitmap_root;//xử lý trên ảnh này
    private ImageView imgSHOW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        LayAnhTuIntroActivity();


    }
    //lấy ảnh từ intent bên kia
    private void LayAnhTuIntroActivity()
    {
        Uri dataBitmap = Uri.parse(getIntent().getStringExtra("image_selected"));
        try {
            InputStream stream = getContentResolver().openInputStream(dataBitmap);
            bitmap_root = BitmapFactory.decodeStream(stream);
            stream.close();

            imgSHOW= findViewById(R.id.imageShow);
            imgSHOW.setImageBitmap(bitmap_root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
