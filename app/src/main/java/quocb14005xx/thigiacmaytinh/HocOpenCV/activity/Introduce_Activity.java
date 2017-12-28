package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;
import quocb14005xx.thigiacmaytinh.HocOpenCV.adapter.IntroPager_Adapter;

public class Introduce_Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    public static Bitmap santaHat,thuglife,santaRia,santaFull,vnFlag;
    public static ArrayList<Bitmap> danhSachBitmap= new ArrayList<>();//list ảnh sticker static load sẵn khi vào app lận về sau chỉ cần gọi lại xử lý




    private Intent it;
    private Dialog dialog;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView intro1, intro2, intro3;
    private int REQUEST_CODE = 1234;


    View.OnClickListener onCLick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            if (view.getId() == R.id.btnFromThuVien) {
                XuLyLoadAnhThuVien();
            } else {
                startActivity(new Intent(Introduce_Activity.this, MainActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_);
        mPager = (ViewPager) findViewById(R.id.pagerIntro);
        mPagerAdapter = new IntroPager_Adapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);
        intro1 = findViewById(R.id.imgIntro1);
        intro2 = findViewById(R.id.imgIntro2);
        intro3 = findViewById(R.id.imgIntro3);


        //vao intro thi load het anh static lan cac activity khac xai
        santaHat= BitmapFactory.decodeResource(getResources(), R.drawable.noel_hat);
        santaRia= BitmapFactory.decodeResource(getResources(), R.drawable.ria_noel2);
        santaFull= BitmapFactory.decodeResource(getResources(), R.drawable.noel_1200);
        vnFlag= BitmapFactory.decodeResource(getResources(), R.drawable.vnflag);
        thuglife= BitmapFactory.decodeResource(getResources(), R.drawable.thuglifefix);


        danhSachBitmap.add(santaHat);
        danhSachBitmap.add(santaRia);
        danhSachBitmap.add(santaFull);
        danhSachBitmap.add(vnFlag);
        danhSachBitmap.add(thuglife);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            intro1.setBackgroundResource(R.drawable.intro_dot_selected);
            intro2.setBackgroundResource(R.drawable.intro_dot_default);
            intro3.setBackgroundResource(R.drawable.intro_dot_default);
        } else if (position == 1) {
            intro1.setBackgroundResource(R.drawable.intro_dot_default);
            intro2.setBackgroundResource(R.drawable.intro_dot_selected);
            intro3.setBackgroundResource(R.drawable.intro_dot_default);
        } else if (position == 2) {
            intro1.setBackgroundResource(R.drawable.intro_dot_default);
            intro2.setBackgroundResource(R.drawable.intro_dot_default);
            intro3.setBackgroundResource(R.drawable.intro_dot_selected);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //click vào 1 trong 4 form của app
    public void StartActivityHieuUng(View view) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_load_anh);
        dialog.setTitle("Chọn ảnh để thêm hiệu ứng");
        dialog.show();
        Button from = dialog.findViewById(R.id.btnFromThuVien);
        Button newPic = dialog.findViewById(R.id.btnNewPicture);
        from.setOnClickListener(onCLick);//click nút thư viện
        newPic.setOnClickListener(onCLick);//click nút video camera effect ảnh mới

    }
    private void XuLyLoadAnhThuVien() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //do tra ve 1 data.getData là 1 Uri nên pass data uri qua bên kia rồi mới decodebitmap
            it = new Intent(Introduce_Activity.this, Main2Activity.class);
            it.putExtra("image_selected", data.getData().toString());
            startActivity(it);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //click tính năng nạp card
    public void StartActivityNapCard(View view) {
    }
    //click tính năng ghép khung
    public void StartActivityGhepKhung(View view) {
    }

    //click xem thử viện ảnh của app
    public void StartActivityLibrary(View view) {
        startActivity(new Intent(this, ThuVienAnh_Activity.class));
    }
}
