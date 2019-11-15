package com.example.floatingbutton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

public class FloatingActivity extends AppCompatActivity {

    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openView();
        finish();
    }

    public void openView() {
        if(Settings.canDrawOverlays(this))
            startService(new Intent(this, FloatingService.class));
        else
            onObtainingPermissionOverlayWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
    }
}