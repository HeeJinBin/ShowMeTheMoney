package com.example.smtm7.AirButton;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smtm7.DetailsView.DetailsActivity;
import com.example.smtm7.R;

public class FloatingService extends Service{

    private WindowManager windowManager;
    private View view;

    private LinearLayout first_overlay;
    private LinearLayout second_overlay;
    private ImageButton airButton;
    private ImageButton captureButton;
    private ImageButton appButton;
    private ImageButton backButton;

    int first_x = 0;
    int first_y = 0;
    float first_rawx = 0;
    float first_rawy = 0;

    private String TAG = "FloatingViewService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_floating,null);

        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.RIGHT| Gravity.TOP;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(view, params);

        first_overlay = (LinearLayout) view.findViewById(R.id.first_overlay);
        second_overlay = (LinearLayout) view.findViewById(R.id.second_overlay);

        first_overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Log.d(TAG,"Touch : "+event.getAction());

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    first_x = params.x;
                    first_y = params.y;
                    first_rawx = event.getRawX();
                    first_rawy = event.getRawY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG,"[X,Y]] "+event.getX()+" "+event.getY());
                    params.x = first_x + (int)(event.getRawX() - first_rawx);
                    params.y = first_y + (int)(event.getRawY() - first_rawy);
                    windowManager.updateViewLayout(view, params);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {

                }
                return true;
            }
        });

        second_overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Log.d(TAG,"Touch : "+event.getAction());

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    first_x = params.x;
                    first_y = params.y;
                    first_rawx = event.getRawX();
                    first_rawy = event.getRawY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG,"[X,Y]] "+event.getX()+" "+event.getY());
                    params.x = first_x + (int)(event.getRawX() - first_rawx);
                    params.y = first_y + (int)(event.getRawY() - first_rawy);
                    windowManager.updateViewLayout(view, params);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {

                }
                return true;
            }
        });

        airButton = (ImageButton) view.findViewById(R.id.floating_button);
        backButton = (ImageButton) view.findViewById(R.id.overlay_back_button);
        captureButton = (ImageButton) view.findViewById(R.id.capture_button);
        appButton = (ImageButton) view.findViewById(R.id.app_button);

        airButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("onClick: ", "first overlay");
                first_overlay.setClickable(false);
                first_overlay.setVisibility(View.GONE);
                second_overlay.setClickable(true);
                second_overlay.setVisibility(View.VISIBLE);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                view = null;
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                stopSelf();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_overlay.setClickable(true);
                first_overlay.setVisibility(View.VISIBLE);
                second_overlay.setClickable(false);
                second_overlay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }
}
