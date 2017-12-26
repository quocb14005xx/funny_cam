package quocb14005xx.thigiacmaytinh.HocOpenCV.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import quocb14005xx.thigiacmaytinh.HocOpenCV.fragment.frag_intro1;
import quocb14005xx.thigiacmaytinh.HocOpenCV.fragment.frag_intro2;
import quocb14005xx.thigiacmaytinh.HocOpenCV.fragment.frag_intro3;


public class IntroPager_Adapter extends FragmentStatePagerAdapter {
    public IntroPager_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:return new frag_intro1();
           case 1: return new frag_intro2();
           case 2:return new frag_intro3();
           default:break;
       }
       return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
