package quocb14005xx.thigiacmaytinh.HocOpenCV.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;

/**
 * Created by quocb14005xx on 1/12/2018.
 */

public class MyImageView extends AppCompatImageView {
    Paint mPaint;
    ArrayList<Point> listPoint;

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

    private void initPaint(Context ctx) {
        listPoint = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point p : listPoint) {
            canvas.drawPoint(p.x, p.y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(MyContants.TAG, event.getX() + "----" + event.getY());
        listPoint.add(new Point(Math.round(event.getX()), Math.round(event.getY())));
        invalidate();
        return true;
    }


}