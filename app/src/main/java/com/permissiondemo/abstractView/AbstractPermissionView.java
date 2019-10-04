package com.permissiondemo.abstractView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.permissiondemo.R;

import app.grant.permission.AppPermission;
import app.grant.permission.PermissionListener;

public abstract class AbstractPermissionView extends AppCompatActivity {

    public abstract void invoke(Activity activity);

    public abstract void invokeResults(int requestCode, int[] grantResults);

    public abstract void accessSingle(String permission, PermissionListener listener);

    protected abstract void accessMultiple(String[] permissions, PermissionListener listener);

    protected abstract void accessAll(PermissionListener listener);

    protected abstract void allowSettings(boolean isAllow);


    private PermissionView allView;
    private PermissionView multipleView;
    private PermissionView singleView;

    private PermissionListDialog permissionList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        invoke(this);

        setContentView(R.layout.activity_permissions);

        final ImageButton allowSetting = findViewById(R.id.allowSetting);
        allowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = view.getTag().toString().equals("0");
                if (status) {
                    allowSetting.setTag("1");
                    allowSetting.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.denied));
                } else {
                    allowSetting.setTag("0");
                    allowSetting.setColorFilter(ContextCompat.getColor(view.getContext(), android.R.color.white));
                }
                allowSettings(status);
            }
        });


        allView = findViewById(R.id.allView);
        multipleView = findViewById(R.id.multipleView);
        singleView = findViewById(R.id.singleView);


        final String[] permissions = AppPermission.getAllManifestPermissions();
        permissionList = PermissionListDialog.from(this).loadPermission(permissions);

        allView.setListImageViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultView(singleView);
                defaultView(multipleView);
                defaultView(allView);

                allView.setDescription(getManifestPermission(permissions));
                accessAll(getListener(allView));
            }
        });


        multipleView.setListImageViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                defaultView(singleView);
                defaultView(multipleView);
                defaultView(allView);

                permissionList
                        .multiplePermission(getResources().getColor(R.color.multipleBG), new PermissionListDialog.MultipleCallBack() {
                            @Override
                            public void grantPermissions(String[] permission) {
                                multipleView.setDescription(getManifestPermission(permission));
                                permissionList.viewDialog();
                                accessMultiple(permission, getListener(multipleView));
                            }
                        }).get();


            }
        });


        singleView.setListImageViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultView(singleView);
                defaultView(multipleView);
                defaultView(allView);
                permissionList
                        .singlePermission(getResources().getColor(R.color.singleBG), new PermissionListDialog.SingleCallBack() {
                            @Override
                            public void grantPermission(String permission) {
                                singleView.setDescription("{" + permission.replace("android.permission.", "") + "}");
                                permissionList.viewDialog();
                                accessSingle(permission, getListener(singleView));
                            }

                        }).get();

            }
        });


    }

    private String getManifestPermission(String[] permissions) {
        StringBuilder builder = new StringBuilder();
        if (permissions != null) {
            builder.append("{ ");

            for (String permission : permissions) {
                permission = permission.replace("android.permission.", "");
                builder.append(permission).append(", ");
            }
            builder.append("}");
            return builder.toString();
        }

        return "";
    }

    private PermissionListener getListener(final PermissionView view) {
        return new PermissionListener() {
            @Override
            public void onAppGranted() {
                grantedView(view);
            }

            @Override
            public void onAppDenied() {
                deniedView(view);
            }
        };
    }


    private void defaultView(PermissionView view) {
        view.setDescription(null);
        view.setLockTint(android.R.color.white);
        view.setLockResource(R.drawable.locked);
    }

    private void grantedView(PermissionView view) {
        view.setLockTint(R.color.grant);
        view.setLockResource(R.drawable.unlocked);
    }

    private void deniedView(PermissionView view) {
        view.setLockTint(R.color.denied);
        view.setLockResource(R.drawable.locked);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        invokeResults(requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
