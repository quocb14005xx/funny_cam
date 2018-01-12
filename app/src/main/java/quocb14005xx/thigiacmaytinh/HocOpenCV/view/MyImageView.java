package quocb14005xx.thigiacmaytinh.HocOpenCV.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;

/**
 * Created by quocb14005xx on 1/12/2018.
 */

public class MyImageView extends View {
    private final Object mLock = new Object();
    private Paint mPaint;
    private ArrayList<Point> mListPoint;
    @Nullable
    private Bitmap mBitmap;

    public MyImageView(Context context) {
        super(context);
        initPaint(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        mListPoint = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mLock) {
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(MyContants.TAG, event.getX() + "----" + event.getY());
        /*mListPoint.add(new Point(Math.round(event.getX()), Math.round(event.getY())));*/
        if (mBitmap != null) {
            synchronized (mLock) {
                Canvas canvas = new Canvas(mBitmap);
                canvas.drawPoint(event.getX(), event.getY(), mPaint);
            }
            invalidate();
        }
        return true;
    }

    @Nullable
    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setImageBitmap(@NonNull Bitmap bitmap) {
        this.mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }
}