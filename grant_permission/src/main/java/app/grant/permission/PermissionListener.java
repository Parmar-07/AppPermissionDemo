package app.grant.permission;

public interface PermissionListener {


    /**
     * TODO stuff
     * when user is granted permission to the application
     */
    void onAppGranted();

    /**
     * TODO stuff
     * when user is denied permission to the application
     */
    void onAppDenied();


}