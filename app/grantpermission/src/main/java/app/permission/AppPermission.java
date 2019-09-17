package app.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("Registered")
public class AppPermission extends Activity {


    private PermissionListener permissionListener;
    private Activity appActivity;
    private ArrayList<String> applyPermissions = new ArrayList<>();


    private AppPermission(Activity appActivity) {
        this.appActivity = appActivity;
    }


    private void onPermissionResult(int requestCode, boolean granted) {

        if (requestCode == permissionListener.addRequestCode()) {
            if (granted) {
                permissionListener.onAppGranted();
            } else {
                permissionListener.onAppDenied();
            }
        }

    }


    private boolean askForPermission() {
        return Build.VERSION.SDK_INT >= 23;
    }


    @SuppressLint("StaticFieldLeak")
    private static AppPermission appPermission = null;

    public static void invokePermissionFrom(Activity activity) {
        if (appPermission == null) {
            appPermission = new AppPermission(activity);
        }

    }


    public static void invokePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        appPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public static void grant(PermissionListener permissionListener) {
        appPermission.grantPermissions(permissionListener);
    }


    private void grantPermissions(PermissionListener permissionListener) {

        this.permissionListener = permissionListener;
        applyPermissions.clear();
        if (askForPermission()) {

            String[] permissions = permissionListener.addRequiredPermissions();

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
                onPermissionResult(permissionListener.addRequestCode(), true);
            }
        } else {
            onPermissionResult(permissionListener.addRequestCode(), true);
        }
    }


    private boolean permissionExits(String permission) {

        return ContextCompat.checkSelfPermission(
                appActivity,
                permission
        ) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (permissionListener.allowFromSettings()) {
                    settingAlert();
                }
                onPermissionResult(requestCode, false);
                return;
            }
        }
        onPermissionResult(requestCode, true);
    }


    private void settingAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(appActivity);
        alertDialogBuilder.setTitle("Grant Permission!");
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
        ActivityCompat.requestPermissions(appActivity, permissions, this.permissionListener.addRequestCode());

    }

}
