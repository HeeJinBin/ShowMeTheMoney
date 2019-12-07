package com.example.floatingbutton;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;



public class FloatingService extends Service implements View.OnTouchListener{

    private String TAG = "FloatingService";

    private WindowManager windowManager;
    private View floatingView;

    //private LinearLayout airButton;
    public static LinearLayout second_overlay;
    public static ImageButton airButton;
    private ImageButton captureButton;
    private ImageButton appButton;
    private ImageButton backButton;
    private ImageButton deleteButton;

    public int screenDensity;
    public int displayWidth, displayHeight;

    float xpos = 0;
    float ypos = 0;

    private int _xDelta;
    private int _yDelta;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingView = inflater.inflate(R.layout.activity_floating, null);
        floatingView.setOnTouchListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP|Gravity.RIGHT;


        second_overlay = floatingView.findViewById(R.id.second_overlay);

        airButton = floatingView.findViewById(R.id.floating_button);
        backButton = floatingView.findViewById(R.id.overlay_back_button);
        captureButton = floatingView.findViewById(R.id.capture_button);
        appButton = floatingView.findViewById(R.id.app_button);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenDensity = displayMetrics.densityDpi;
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
        airButton.setOnTouchListener(this);
        airButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick: ", "first overlay");
                //airButton.setClickable(false);
                airButton.setVisibility(View.GONE);
                second_overlay.setClickable(true);
                second_overlay.setX(airButton.getX());
                second_overlay.setY(airButton.getY());
                second_overlay.setVisibility(View.VISIBLE);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_overlay.setClickable(false);
                second_overlay.setVisibility(View.GONE);
                startService(new Intent(FloatingService.this, CaptureService.class));
                //stopSelf();
            }
        });


        //실제 캡쳐
        captureOrBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class );
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1001, intent, 0);
                try {
                    pendingIntent.send();
                    third_overlay.setClickable(false);
                    third_overlay.setVisibility(View.GONE);
                    airButton.setClickable(true);
                    airButton.setVisibility(View.VISIBLE);
                }
                catch(PendingIntent.CanceledException e) {
                    Log.d("test", "-------------------------pending exception ");
                    e.printStackTrace();
                }
            }
        });

        //캡쳐안함
        captureOrBackButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"Back to second",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CaptureService.class);
                stopService(intent);

                third_overlay.setClickable(false);
                third_overlay.setVisibility(View.GONE);
                second_overlay.setClickable(true);
                second_overlay.setVisibility(View.VISIBLE);

                return true;
            }
        });

        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(floatingView);
                floatingView = null;
                stopSelf();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                airButton.setClickable(true);
                airButton.setVisibility(View.VISIBLE);
                second_overlay.setClickable(false);
                second_overlay.setVisibility(View.GONE);
            }
        });


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);
        Log.d("지금","ㅇㅇ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        /*
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("너 되니","");
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
        final int X = (int) motionEvent.getRawX();
        final int Y = (int) motionEvent.getRawY();
        /*
        Display display = windowManager2.getDefaultDisplay();
        in height = display.getHeight();

         */
        DisplayMetrics displayMetrics = new DisplayMetrics();

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            _xDelta = (int) (X - view.getTranslationX());
            _yDelta = (int) (Y - view.getTranslationY());
            Log.d("height",height+"");
            Log.d("view.getHeight()",view.getHeight()+"");
            Log.d("view.getY()",view.getY()+"");
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            view.setTranslationX(X - _xDelta);
            view.setTranslationY(Y - _yDelta);
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Log.d("x",location[0]+"");
            Log.d("y",location[1]+"");
        }
        return false;
    }
}
