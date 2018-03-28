package quocb14005xx.thigiacmaytinh.HocOpenCV.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    private float[] points;
    private static int index_points;
    private Canvas canvas;
    Paint clearPaint;


    @Nullable
    private Bitmap mBitmap;

    @Nullable
    private Bitmap mBitmapTemp;

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
        mPaint.setStrokeWidth(30);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


        points = new float[99999];
        index_points = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.save();
        if (CUT_TOOL) {
            if (p1x > p2x && p1y > p2y) {
                canvas.drawRect(p2x, p2y, p1x, p1y, mPaint);
            } else {
                canvas.drawRect(p1x, p1y, p2x, p2y, mPaint);
            }
        }
//        else
//        {
//            canvas.drawPoints(points,mPaint);
//        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
//                points[index_points]=event.getX();
//                points[index_points+1]=event.getY();
//                Log.e(MyContants.TAG,String.valueOf(points[index_points]) +" : "+ String.valueOf(points[index_points+1]));
//                index_points+=2;
                canvas = new Canvas(mBitmap);
                canvas.drawPoint(event.getX(), event.getY(), mPaint);
//
//                canvas.drawPoints(points, mPaint);
            }
            if (p2x > this.mBitmap.getWidth()) {
                p2x = this.mBitmap.getWidth();
            }
            if (p2y > this.mBitmap.getHeight()) {
                p2y = this.mBitmap.getHeight();
            }
            invalidate();
        }
        return true;
    }

    //getter
    @Nullable
    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    //setter
    public void setImageBitmap(@NonNull Bitmap bitmap) {
        Bitmap temp = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        this.mBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap temp2 = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        this.mBitmapTemp = temp2.copy(Bitmap.Config.ARGB_8888, true);
    }


    //setter mode cut hay khong cut
    public void setModeCut(boolean isCut) {
        this.CUT_TOOL = isCut;
    }

    /*
    * @ cut bitmap tu x1,y1 den x2,y2
    * */
    public void cutBitMap() {
        if (p1x > p2x && p1y > p2y) {
            this.mBitmap = Bitmap.createBitmap(
                    this.mBitmap
                    , (int) p2x
                    , (int) p2y
                    , (int) (p1x - p2x), (int) (p1y - p2y));
        } else {
            this.mBitmap = Bitmap.createBitmap(
                    this.mBitmap
                    , (int) p1x
                    , (int) p1y
                    , (int) (p2x - p1x), (int) (p2y - p1y));
        }


        invalidate();
        //lam tiep cutting picture dua vao rotate de xet anh ngang hay anh doc roi moi cat
    }

    private static int deg2;

    public void setRotatebitmap(int deg) {
        deg2 = deg;
        Matrix matrix = new Matrix();
        matrix.postRotate(deg);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        invalidate();
    }

    public void clearCanvas() {
        canvas.restore();
        canvas.drawBitmap(mBitmapTemp, 0, 0, clearPaint);
        invalidate();
    }
}