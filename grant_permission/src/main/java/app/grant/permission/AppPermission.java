package app.grant.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class AppPermission {


    private PermissionListener permissionListener;
    private int requestCode;
    private Activity appActivity;
    private boolean isAllowFromSettings = false;
    private ArrayList<String> applyPermissions = new ArrayList<>();


    private AppPermission(Activity appActivity) {
        this.appActivity = appActivity;
    }


    private void onPermissionResult(int requestCode, boolean granted) {

        if (requestCode == this.requestCode) {
            if (granted) {
                permissionListener.onAppGranted();
            } else {
                permissionListener.onAppDenied();
            }
        }

    }


    /**
     * @return checking the device version for runtime permission
     * if "Build.VERSION.SDK_INT >= 23" return true else false
     */
    private boolean askForPermission() {
        return Build.VERSION.SDK_INT >= 23;
    }


    @SuppressLint("StaticFieldLeak")
    private static AppPermission appPermission = null;

    /**
     * @param activity invoking the runtime permission from Activity
     */
    public static void invokePermissionFrom(Activity activity) {
        if (appPermission == null) {
            appPermission = new AppPermission(activity);
        }

    }


    /**
     * @param requestCode which has been passed grant(int requestCode,...) permission
     * @param grantResults gather the permission results from user which is granted or denied
     * and notify to {@link PermissionListener}
     */
    public static void invokePermissionResult(int requestCode, @NonNull int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (appPermission.isAllowFromSettings) {
                    appPermission.altertAppSetting();
                }
                appPermission.onPermissionResult(requestCode, false);
                return;
            }
        }
        appPermission.onPermissionResult(requestCode, true);


    }


    /**
     * @param requestCode which passes from user action permission request code
     * @param permissionListener is listen and notify whether app is granted or denied.
     */
    public static void grant(int requestCode, PermissionListener permissionListener) {
        appPermission.requestCode = requestCode;
        appPermission.permissionListener = permissionListener;
        appPermission.grantPermissions();
    }

    /**
     * @param isAllowFromSettings is a boolean flag to forcefully
     *  open to setting screen on permission is denied
     *
     */
    public static void setForeceAllow(boolean isAllowFromSettings) {
        appPermission.setAllowFromSettings(isAllowFromSettings);
    }


    private void grantPermissions() {

        applyPermissions.clear();
        if (askForPermission()) {
            String[] permissions = null;
            try {
                PackageInfo info = appActivity.getPackageManager().getPackageInfo(appActivity.getPackageName(), PackageManager.GET_PERMISSIONS);
                if (info.requestedPermissions != null) {
                    int len = info.requestedPermissions.length;
                    permissions = new String[len];
                    System.arraycopy(info.requestedPermissions, 0, permissions, 0, len);
                } else {
                    Toast.makeText(appActivity, "No Permission Request found!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(appActivity, "Added Permissions is not proper", Toast.LENGTH_SHORT).show();
            }

            if (permissions == null)
                return;

            int length = permissions.length;


            if (length > 0) {
                for (String permission : permissions) {
                    if (!permissionExits(permission)) {
                        applyPermissions.add(permission);
                    }
                }
            }

            length = applyPermissions.size();

            if (length > 0) {
                requestPermission();
            } else {
                onPermissionResult(requestCode, true);
            }
        } else {
            onPermissionResult(requestCode, true);
        }
    }


    private boolean permissionExits(String permission) {

        return ContextCompat.checkSelfPermission(
                appActivity,
                permission
        ) == PackageManager.PERMISSION_GRANTED;
    }


    private void altertAppSetting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(appActivity);
        alertDialogBuilder.setTitle("Grant Permission!");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Go to settings and provide permission to access this feature");
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent myAppSettings = new
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(0);
                appActivity.startActivityForResult(myAppSettings, 5555);
            }
        });

        alertDialogBuilder.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        Object[] objNames = applyPermissions.toArray();
        String[] permissions = Arrays.copyOf(objNames, objNames.length, String[].class);
        ActivityCompat.requestPermissions(appActivity, permissions, this.requestCode);

    }


    private void setAllowFromSettings(boolean allowFromSettings) {
        isAllowFromSettings = allowFromSettings;
    }
}
