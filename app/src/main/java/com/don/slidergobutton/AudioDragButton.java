package com.don.slidergobutton;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DON on 17/03/17.
 */

public class AudioDragButton extends AppCompatButton {

  private static final String TAG = "AudioDragButton";
  private int lastX;                    //最後位移
  private int lastY;                    //最後位移
  private int startX;                   //相對屏幕原始位置
  private int startY;                   //相對屏幕原始位置
  private int startLeft;                //layout原始位置
  private int startRight;               //layout原始位置
  private int startTop;                 //layout原始位置
  private int startBottom;              //layout原始位置
  private int screenWidth;              //屏幕寬度
  private int screenHeight;             //屏幕寬度
  private boolean isDragLeft = true;   //X軸，左滑，右滑設置
  private int maxCancelWidth = 500;     //最大取消距離
  private float scaleX = 2.5f;         //按鈕放大X
  private float scaleY = 2.5f;         //按鈕放大Y
  private AudioDragDelegate audioDragDelegate;
  private boolean isCancel = false;
  private int moveViewStartLeft;                //layout原始位置
  private int moveViewStartRight;               //layout原始位置
  private int moveViewStartTop;                 //layout原始位置
  private int moveViewStartBottom;              //layout原始位置
  public View view;            //跟隨移動的VIEW

  public interface AudioDragDelegate {

    void cancelAudio();

    void startAudio();

    void endAudio();
  }

  public AudioDragButton(Context context) {
    super(context);
    init();
  }

  public AudioDragButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public AudioDragButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    DisplayMetrics dm = getResources().getDisplayMetrics();
    screenWidth = dm.widthPixels;
    screenHeight = dm.heightPixels;
  }

  public void setAudioDragDelegate(AudioDragDelegate audioDragDelegate) {
    this.audioDragDelegate = audioDragDelegate;
  }

  public boolean isDragLeft() {
    return isDragLeft;
  }

  public void setDragLeft(boolean dragLeft) {
    isDragLeft = dragLeft;
  }

  public int getMaxCancelWidth() {
    return maxCancelWidth;
  }

  public void setMaxCancelWidth(int maxCancelWidth) {
    this.maxCancelWidth = maxCancelWidth;
  }

  @Override
  public float getScaleX() {
    return scaleX;
  }

  @Override
  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }

  @Override
  public float getScaleY() {
    return scaleY;
  }

  @Override
  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        isCancel = false;
        startX = lastX = (int) event.getRawX();
        startY = lastY = (int) event.getRawY();

        startLeft = this.getLeft();
        startRight = this.getRight();
        startTop = this.getTop();
        startBottom = this.getBottom();

        if (null != view) {
          moveViewStartLeft = view.getLeft();
          moveViewStartRight = view.getRight();
          moveViewStartTop = view.getTop();
          moveViewStartBottom = view.getBottom();
        }
        this.animate().setDuration(100).scaleX(getScaleX());
        this.animate().setDuration(100).scaleY(getScaleY());

        if (null != audioDragDelegate) {
          this.audioDragDelegate.startAudio();
        }
        Log.i(TAG, "ACTION_DOWN");
        return true;
      }
      case MotionEvent.ACTION_MOVE: {
        //        Log.i(TAG, "ACTION_MOVE");
        // 移动中动态设置位置
        // 取消錄音
        if (Math.abs(event.getRawX() - startX) >= maxCancelWidth) {
          this.layout(startLeft, startTop, startRight, startBottom);
          if(null != view) {
            view.layout(moveViewStartLeft, moveViewStartTop, moveViewStartRight,
                moveViewStartBottom);
          }
          this.animate().setDuration(0).scaleX(1.0f);
          this.animate().setDuration(0).scaleY(1.0f);
          this.setPressed(false);
          if (null != audioDragDelegate && !isCancel) {
            isCancel = true;
            Log.i(TAG, "cancelAudio");
            this.audioDragDelegate.cancelAudio();
          }
          return true;
        }
        if (isDragLeft) {
          if (event.getRawX() >= startX) {
            return true;
          }
        } else {
          if (event.getRawX() <= startX) {
            return true;
          }
        }
        int dx = (int) event.getRawX() - lastX;
        int dy = (int) event.getRawY() - lastY;
        int left = this.getLeft() + dx;
        int top = this.getTop() + dy;
        int right = this.getRight() + dx;
        int bottom = this.getBottom() + dy;

        if (left < 0) {
          left = 0;
          right = left + this.getWidth();
        }
        if (right > screenWidth) {
          right = screenWidth;
          left = right - this.getWidth();
        }
        if (top < 0) {
          top = 0;
          bottom = top + this.getHeight();
        }
        if (bottom > screenHeight) {
          bottom = screenHeight;
          //          top = bottom - this.getHeight();
        }

        this.layout(left, startTop, right, startBottom);

        if (null != view) {
          int tvleft = view.getLeft() + dx;
          int tvtop = view.getTop() + dy;
          int tvright = view.getRight() + dx;
          int tvbottom = view.getBottom() + dy;

          if (tvleft < 0) {
            tvleft = 0;
            tvright = tvleft + view.getWidth();
          }
          if (tvright > screenWidth) {
            tvright = screenWidth;
            tvleft = tvright - view.getWidth();
          }
          if (tvtop < 0) {
            tvtop = 0;
            tvbottom = tvtop + view.getHeight();
          }
          if (bottom > screenHeight) {
            bottom = screenHeight;
            //          top = bottom - this.getHeight();
          }

          view.layout(tvleft, moveViewStartTop, tvright, moveViewStartBottom);
        }
        // 将当前的位置再次设置
        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();
        return true;
      }
      case MotionEvent.ACTION_UP: {
        Log.i(TAG, "ACTION_UP");
        this.layout(startLeft, startTop, startRight, startBottom);
        if (null != view) {
          view.layout(moveViewStartLeft, moveViewStartTop, moveViewStartRight, moveViewStartBottom);
        }
        this.animate().setDuration(0).scaleX(1.0f);
        this.animate().setDuration(0).scaleY(1.0f);
        if (null != audioDragDelegate && !isCancel) {
          this.audioDragDelegate.endAudio();
          Log.i(TAG, "endAudio");
        }
        return true;
      }

      case MotionEvent.ACTION_CANCEL: {
        Log.i(TAG, "ACTION_CANCEL");
        if (null != audioDragDelegate && !isCancel) {
          this.layout(startLeft, startTop, startRight, startBottom);
          if (null != view) {
            view.layout(moveViewStartLeft, moveViewStartTop,
                moveViewStartRight, moveViewStartBottom);
          }
          this.animate().setDuration(0).scaleX(1.0f);
          this.animate().setDuration(0).scaleY(1.0f);
          isCancel = true;
          Log.i(TAG, "cancelAudio");
          this.audioDragDelegate.cancelAudio();
        }
        return true;
      }

      case MotionEvent.ACTION_OUTSIDE: {
        Log.i(TAG, "ACTION_OUTSIDE");
      }
      break;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }

  public void addViewWithMove(View tv) {
    view = tv;
  }
}