package com.example.smtm7.AirButton;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Environment;
import android.os.IBinder;
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

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseOCR;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.DetailsView.DetailsActivity;
import com.example.smtm7.R;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloatingService extends Service implements View.OnTouchListener{

    private String TAG = "FloatingService";

    private WindowManager windowManager;
    private View floatingView;

    private LinearLayout first_overlay;
    private LinearLayout second_overlay;
    private ImageButton airButton;
    private ImageButton captureButton;
    private ImageButton appButton;
    private ImageButton backButton;
    private ImageButton deleteButton;

    float xpos = 0;
    float ypos = 0;

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
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.RIGHT;

        first_overlay = floatingView.findViewById(R.id.first_overlay);
        second_overlay = floatingView.findViewById(R.id.second_overlay);

        airButton = floatingView.findViewById(R.id.floating_button);
        backButton = floatingView.findViewById(R.id.overlay_back_button);
        captureButton = floatingView.findViewById(R.id.capture_button);
        appButton = floatingView.findViewById(R.id.app_button);
        deleteButton = floatingView.findViewById(R.id.delete_button);

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
                // 일단 여기다 캡쳐 서버랑 통신함
                NetworkHelper networkHelper = new NetworkHelper();
                final ApiService apiService = networkHelper.getApiService();

                //저장하는 경로
                String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
                String file_name = "test1.jpg";
                //그래서 저장되는 최종 path는 string_path+file_name

                File file = new File(ex_storage+file_name);

                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                final MultipartBody.Part photo = MultipartBody.Part.createFormData("file",file.getName(),reqFile);

                Log.d("OCR", ex_storage+file_name);

                apiService.upload(photo).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            apiService.getOCR(photo).enqueue(new Callback<ResponseOCR>() {
                                @Override
                                public void onResponse(Call<ResponseOCR> call, Response<ResponseOCR> response) {
                                    ResponseOCR body = response.body();

                                    //OCR 결과
                                    if(body.getMessage().equals("OCR Success")){
                                        Toast.makeText(getApplicationContext(), body.getPGname()+", "+body.getDate()+", "+body.getPrice(),Toast.LENGTH_LONG);
                                        Log.d("OCR", body.getPGname()+", "+body.getDate()+", "+body.getPrice());
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseOCR> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(floatingView);
                floatingView = null;
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);
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
        return false;
    }
}
