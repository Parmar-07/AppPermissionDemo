package com.permissiondemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import app.grant.permission.AppPermission;
import app.grant.permission.PermissionListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppPermission.invokePermissionFrom(this);
        final TextView grantResult = findViewById(R.id.grantResult);
        Button grantButton = findViewById(R.id.grantButton);

        final int requestCode = 1234;
        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPermission.grant(requestCode, new PermissionListener() {


                    @Override
                    public void onAppGranted() {
                        grantResult.setText("Permisison Granted");
                    }

                    @Override
                    public void onAppDenied() {
                        grantResult.setText("Permisison Denied");

                    }

                });
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        AppPermission.invokePermissionResult(requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
