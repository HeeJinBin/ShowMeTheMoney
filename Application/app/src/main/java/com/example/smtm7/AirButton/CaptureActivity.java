package com.example.smtm7.AirButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseOCR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureActivity extends Activity {

    private int mResultCode;
    private Intent mResultData;

    private MediaProjectionManager mpManager;
    public static final int REQUEST_MEDIA_PROJECTION = 1001;

    public int screenDensity;
    public  int displayWidth, displayHeight;
    public ImageReader imageReader;
    public VirtualDisplay virtualDisplay;
    private MediaProjection mProjection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "----------------------------captureActivity onCreate");

        //Intent tempIntent = new Intent(CaptureActivity.this, TestActi.class);
        //startActivityForResult(tempIntent,1001);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenDensity = displayMetrics.densityDpi;
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        mpManager = (MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE); //미디어프로젝션 시작?

        // permission을 확인하는 intent를 던져 사용자의 허가 · 불허가를받을
        if(mpManager != null){
            startActivityForResult(mpManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            Log.d("debug", "-----------------------------capture permission");
            Log.d("debug","----------------------------request media projection:"+REQUEST_MEDIA_PROJECTION);
        }

        //finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("debug", "-----------------------------onActivityResult");
        if (REQUEST_MEDIA_PROJECTION == requestCode) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this,
                        "User cancelled", Toast.LENGTH_LONG).show();
                return;
            }
            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection(resultCode, data);
        }
    }

    private void setUpMediaProjection(int code, Intent intent) {
        mProjection = mpManager.getMediaProjection(code, intent);
        setUpVirtualDisplay();
    }

    private void setUpVirtualDisplay() {
        imageReader = ImageReader.newInstance(displayWidth, displayHeight, PixelFormat.RGBA_8888 ,2);

        virtualDisplay = mProjection.createVirtualDisplay("ScreenCapture",
                displayWidth, displayHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
        Log.d("debug", "-----------------------------virtualdisplay : "+virtualDisplay.getDisplay());

        try {
            Thread.sleep(200); //1초 대기
            getScreenshot();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();
    }


    public void getScreenshot() {
        Log.d("debug", "-----------------------------getScreenshot");
        // ImageReader에서 화면을 검색

        Image image = imageReader.acquireLatestImage();
        Log.d("debug","-----------------------------------------------image: "+image);
        Image.Plane[] planes = image.getPlanes();

        ByteBuffer buffer = planes[0].getBuffer();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * displayWidth;

        // 버퍼에서 Bitmap을 생성
        Bitmap bitmap = Bitmap.createBitmap(
                displayWidth + rowPadding / pixelStride, displayHeight,
                Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Screenshot" + timeStamp;

        saveBitmaptoJPEG(bitmap, fileName);




    }

    public static void saveBitmaptoJPEG(Bitmap bitmap, String name){
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
        //String folder_name = "/"+"ScreenShot"+"/";
        String file_name = name+".jpg";
        String string_path = ex_storage;


        File file_path;
        try{
            Log.d("debug", "트라이는 들어왔따");
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                Log.d("debug", "디렉토리 없어서 만들려고 한다");
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);

            Bitmap bitmap_cropped = cropBitmap(bitmap);
            bitmap_cropped.compress(Bitmap.CompressFormat.JPEG,100,out);
            Log.d("debug", "@@@@@@@@2bitmap to jpeg");
            out.close();
        }catch (FileNotFoundException e){
            Log.e("FileNotFoundException",e.getMessage());
        }catch (IOException e){
            Log.e("IOException",e.getMessage());
        }
        Log.d("test", "&&&&&&&&&file path: "+string_path+file_name);

        //save
        NetworkHelper networkHelper = new NetworkHelper();
        final ApiService apiService = networkHelper.getApiService();
        File file = new File(string_path+file_name);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part photo = MultipartBody.Part.createFormData("file",file.getName(),reqFile);

        Log.d("OCR", string_path+file_name);

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

    public static Bitmap cropBitmap(Bitmap original){
        Bitmap result = Bitmap.createBitmap(original
                , 0, 1010
                ,original.getWidth(), 302); //시작x,시작y,넓이,높이
        return result;

    }

    @Override
    protected void onDestroy() {
        if (virtualDisplay != null) {
            Log.d("debug","release VirtualDisplay");
            virtualDisplay.release();
        }
        super.onDestroy();
        this.finish();
    }
}
