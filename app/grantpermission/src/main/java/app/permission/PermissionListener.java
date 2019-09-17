package app.permission;

interface PermissionListener {

    String[] addRequiredPermissions();

    int addRequestCode();

    void onAppGranted();

    void onAppDenied();

    boolean allowFromSettings();

}