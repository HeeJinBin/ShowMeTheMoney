package com.example.floatingbutton;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class CaptureService extends Service implements View.OnTouchListener{
    private WindowManager windowManager2;
    public View squareFrame;


    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    private int _yDelta;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private boolean check = true;
    private int linear_width;
    public static int linear_height;

    private ImageButton imageButton;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(getApplicationContext(),"캡쳐 onCreate",Toast.LENGTH_LONG).show();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "capture service onCreate");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        squareFrame = inflater.inflate(R.layout.capture_layout, null);
        Log.d("test", "set inflater");
        //squareFrame.setOnTouchListener(this);
        squareFrame.setOnTouchListener(this);

        relativeLayout = squareFrame.findViewById(R.id.wooram);
        linearLayout = relativeLayout.findViewById(R.id.capture_crop);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.RIGHT;
        windowManager2 = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager2.addView(squareFrame, params);
        //FloatingService.captureOrBackButton.bringToFront();


        int location[] = new int[2];
        Log.d("testlocation", "---------------------Width : " + location[0]);
        Log.d("testlocation","----------------------Height : " + location[1]);

        ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

                        public void onGlobalLayout() {
                if(check) {
                    Log.d("렐러",relativeLayout.getHeight()+"");
                    Log.d("리니어",linearLayout.getHeight()+"");
                    linear_height = linearLayout.getHeight();
                    check = false;
                }
            }
        };

        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (squareFrame != null) {
            windowManager2.removeView(squareFrame);
            squareFrame = null;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int X = (int) motionEvent.getRawX();
        final int Y = (int) motionEvent.getRawY();
        /*
        Display display = windowManager2.getDefaultDisplay();
        in height = display.getHeight();

         */
        DisplayMetrics displayMetrics = new DisplayMetrics();

        Display display = windowManager2.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            _yDelta = (int) (Y - view.getTranslationY());
            Log.d("height",height+"");
            Log.d("view.getHeight()",view.getHeight()+"");
            Log.d("view.getY()",view.getY()+"");
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            view.setTranslationY(Y - _yDelta);
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Log.d("x",location[0]+"");
            Log.d("y",location[1]+"");

            if (  view.getY() + view.getHeight() / 2 + (height - view.getHeight()) > height ) {
                view.setY(0);
                Log.d("업height",height+"");
                Log.d("업view.getHeight()",view.getHeight()+"");
                Log.d("업view.getY()",view.getY()+"");
            }
            else if ( view.getY()  + view.getHeight() / 2 < 0) {
                view.setY(0);
            }
        }
        /*
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int action = motionEvent.getAction();
        int pointerCount = motionEvent.getPointerCount();


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (pointerCount == 1) {
                    xpos = motionEvent.getRawX();
                    ypos = motionEvent.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerCount == 1) {
                    WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
                    float dx = xpos - motionEvent.getRawX();
                    float dy = ypos - motionEvent.getRawY();
                    xpos = motionEvent.getRawX();
                    ypos = motionEvent.getRawY();

                    Log.d(TAG, "lp.x : " + lp.x + ", dx : " + dx + "lp.y : " + lp.y + ", dy : " + dy);

                    lp.x = (int) (lp.x - dx);
                    lp.y = (int) (lp.y - dy);

                    windowManager.updateViewLayout(view,lp);
                    return true;
                }
                break;
        }

         */
        return false;
    }
}