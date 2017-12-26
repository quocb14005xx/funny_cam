package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;
import quocb14005xx.thigiacmaytinh.HocOpenCV.adapter.IntroPager_Adapter;

public class Introduce_Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView intro1, intro2, intro3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_);
        mPager = (ViewPager) findViewById(R.id.pagerIntro);
        mPagerAdapter = new IntroPager_Adapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);
        intro1 =findViewById(R.id.imgIntro1);
        intro2 =findViewById(R.id.imgIntro2);
        intro3 =findViewById(R.id.imgIntro3);
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

    public void StartActivityHieuUng(View view)
    {
    }

    public void StartActivityNapCard(View view) {
    }

    public void StartActivityGhepKhung(View view) {
    }

    public void StartActivityLibrary(View view) {
    }
}
