package quocb14005xx.thigiacmaytinh.HocOpenCV.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;

/**
 * Created by quocb14005xx on 1/12/2018.
 */

public class MyImageView extends View {


    private boolean CUT_TOOL;

    private Paint mPaint;


    private float p1x, p1y, p2x, p2y;

    @Nullable
    private Bitmap mBitmap;

    public MyImageView(Context context) {
        super(context);
        init(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        CUT_TOOL = false;
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if (CUT_TOOL) {
            canvas.drawRect(p1x, p1y, p2x, p2y, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.e(MyContants.TAG, event.getX() + "----" + event.getY());
        if (mBitmap != null) {

            if (CUT_TOOL) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        p1x = event.getX();
                        p1y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        p2x = event.getX();
                        p2y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        p2x = event.getX();
                        p2y = event.getY();
                        break;

                }
            } else {
                Canvas canvas = new Canvas(mBitmap);
                canvas.drawPoint(event.getX(), event.getY(), mPaint);
            }
            invalidate();
        }


        return true;
    }

    //getter
    @Nullable
    public Bitmap getBitmap() {
        return mBitmap;
    }

    //setter
    public void setImageBitmap(@NonNull Bitmap bitmap) {
        Bitmap temp = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        this.mBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setModeCut(boolean isCut) {
        this.CUT_TOOL = isCut;
    }

    public void cutBitMap() {
        int wid=0, hei=0;

        if (deg2==90 || deg2== 270)
        {
            if(mBitmap.getWidth()>p2x)
            {
                wid=(int)p2x;
            }
            else
            {
                wid=mBitmap.getWidth();
            }
            if(mBitmap.getHeight()>p2y)
            {
                hei= (int) p2y;
            }
            else
            {
                hei=mBitmap.getHeight();
            }
        }
        if (deg2==0 || deg2== 180)
        {
            if(mBitmap.getWidth()>p2x)
            {
                hei=(int)p2x;
            }
            else
            {
                hei=mBitmap.getWidth();
            }
            if(mBitmap.getHeight()>p2y)
            {
                wid= (int) p2y;
            }
            else
            {
                wid=mBitmap.getHeight();
            }
        }

        Log.e(MyContants.TAG,"rotate = "+ deg2+"\nbitmapsize "+mBitmap.getWidth()+"-"+mBitmap.getHeight()+"\n"+(int) p1x
                + "-" + (int) p1y
                + "-" + wid
                + "-" + hei);

        this.mBitmap = Bitmap.createBitmap(
                this.mBitmap
                , (int) p1x
                , (int) p1y
                , wid
                , hei);
        invalidate();
        //lam tiep cutting picture dua vao rotate de xet anh ngang hay anh doc roi moi cat
    }
    private int deg2;
    public void setRotatebitmap(int deg) {
        deg2=deg;
        Matrix matrix = new Matrix();
        matrix.postRotate(deg);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        invalidate();
    }
}