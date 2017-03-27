package communication.wyl.com.weave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangyuelong on 17/2/16.
 */

public class Weave extends View{
    //画笔
    private Paint  mPaint;
    //抗锯齿
    private DrawFilter mDrawFilter;
    //屏幕宽
    private int screenWidth;
    //屏幕高
    private int screenHeight;
    //波浪点的列表
    private List<Float> positions;
    private List<Float> mPositons;
    //临时列表
    private List<Float> temps=new ArrayList<>();
    private List<Float> mTemps=new ArrayList<>();
    //循环周期
    private float mCycle;
    //浪高
    private int WAVEHEIGHT = 20;
    //速度
    private int mSpeed = 5;

    public Weave(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.WHITE);
        mDrawFilter = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取屏幕的宽高
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        //初始化周期
        mCycle = (float) (2*Math.PI / screenWidth);
        positions = new ArrayList<>();
        mPositons = new ArrayList<>();
        for(int i = 0;i < screenWidth;i++){
            //初始化波浪点
            float positon = (float) (WAVEHEIGHT * Math.sin(mCycle *i))/2;
            float mpositon= (float) (WAVEHEIGHT * Math.cos(mCycle *i+Math.PI/2))/2;
            positions.add(positon);
            mPositons.add(mpositon);
            Log.i("position",i+"="+positon);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);
        drawWave(canvas);
    }

    private void drawWave(Canvas canvas) {
        for(int i=0;i<positions.size();i++){
            //画竖线
            canvas.drawLine(i,screenHeight-positions.get(i)-WAVEHEIGHT,i,screenHeight,mPaint);
            canvas.drawLine(i,screenHeight-mPositons.get(i)-WAVEHEIGHT,i,screenHeight,mPaint);
        }
        //清空临时数据
        temps.clear();
        int nowPosition = 0;
        Iterator<Float> iterator = positions.iterator();
        while (iterator.hasNext()){
            //交换临时点的位置
            temps.add(iterator.next());
            iterator.remove();
            nowPosition = nowPosition +1;
            if (nowPosition==mSpeed)
                break;
        }
        positions.addAll(temps);
        //
        mTemps.clear();
        int mNowPosition = 0;
        Iterator<Float> mIterator = mPositons.iterator();
        while (mIterator.hasNext()){
            //交换临时点的位置
            mTemps.add(mIterator.next());
            mIterator.remove();
            mNowPosition = mNowPosition +1;
            if (mNowPosition==10)
                break;
        }
        mPositons.addAll(mTemps);
        //重绘
        invalidate();
    }

    public Weave(Context context) {
        super(context);
    }

    public Weave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
