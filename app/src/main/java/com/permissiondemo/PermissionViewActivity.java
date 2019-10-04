package com.permissiondemo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.permissiondemo.abstractView.AbstractPermissionView;

import app.grant.permission.AppPermission;
import app.grant.permission.PermissionListener;

public class PermissionViewActivity extends AbstractPermissionView {


    @Override
    public void invoke(Activity activity) {
        AppPermission.invokePermissionFrom(activity);

    }

    @Override
    public void invokeResults(int requestCode, int[] grantResults) {
        AppPermission.invokePermissionResult(requestCode, grantResults);
    }


    @Override
    protected void accessAll(PermissionListener listener) {
        AppPermission.grantAll(listener);
    }


    @Override
    public void accessSingle(String permission, PermissionListener listener) {
        AppPermission.grantSingle(listener, permission);

    }

    @Override
    protected void accessMultiple(String[] permissions, PermissionListener listener) {
        AppPermission.grantMultiple(listener, permissions);

    }

    @Override
    protected void allowSettings(boolean isAllow) {
        AppPermission.setForeceAllow(isAllow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
