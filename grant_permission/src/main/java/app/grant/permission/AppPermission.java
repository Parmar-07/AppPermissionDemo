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
    private int requestCode = 555;
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
     * @param requestCode  which has been passed grant(int requestCode,...) permission
     * @param grantResults gather the permission results from user which is granted or denied
     *                     and notify to {@link PermissionListener}
     */
    public static void invokePermissionResult(int requestCode, @NonNull int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (appPermission.isAllowFromSettings) {
                    appPermission.alertAppSetting();
                }
                appPermission.onPermissionResult(requestCode, false);
                return;
            }
        }
        appPermission.onPermissionResult(requestCode, true);


    }


    /**
     * @param permissionListener is listen and notify whether app is granted or denied.
     */
    public static void grantAll(PermissionListener permissionListener) {
        appPermission.permissionListener = permissionListener;
        appPermission.grantPermissions(null);
    }


    public static void grantSingle(PermissionListener permissionListener, String permission) {
        appPermission.permissionListener = permissionListener;
        appPermission.grantPermissions(new String[]{permission});
    }

    public static void grantMultiple(PermissionListener permissionListener, String... permissions) {
        appPermission.permissionListener = permissionListener;
        appPermission.grantPermissions(permissions);
    }


    public static String[] getAllManifestPermissions() {
        return appPermission.getManifestPermissions();
    }

    public static boolean isPermissionGranted(String permission) {
        return appPermission.permissionExits(permission);
    }


    /**
     * @param isAllowFromSettings is a boolean flag to forcefully
     *                            open to setting screen on permission is denied
     */
    public static void setForceAllow(boolean isAllowFromSettings) {
        appPermission.setAllowFromSettings(isAllowFromSettings);
    }


    private String[] getManifestPermissions() {

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

        return permissions;
    }

    private void grantPermissions(String[] userPermissions) {

        applyPermissions.clear();
        if (askForPermission()) {
            String[] permissions = null;

            if (userPermissions != null) {
                permissions = userPermissions;

            } else {
                permissions = getManifestPermissions();
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


    private void alertAppSetting() {

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


    /**
     * Added all android dangerous permissions string
     */

    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    public static final String CAMERA = "android.permission.CAMERA";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String ADD_VOICEMAIL = "android.permission.ADD_VOICEMAIL";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";


}
