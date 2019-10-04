
# [Grant App Permissions-Demo.apk][0]

Runtime Permission library to access system permissions in Android L or higher.

## Usage

### Step : 1
Add app-level permissions in `mainifest` file
Following list of <b>dangerous</b> permissions to grant on Runtime

```xml 

    <uses-permission android:name="android.permission.READ_CALENDAR"/>
    <uses-permission android:name="android.permission.WRITE_CALENDAR"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
    <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

```
### Step : 2
Invoke the library to use, from `Activity` or `Fragment` need to <i>invokePermissionFrom</i> of library in override the `onCreate(...)` method,
`setForeceAllow` is optional for forcely allow permission from app setting 
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
	AppPermission.invokePermissionFrom(this);
	AppPermission.setForeceAllow(true);//optional
        ...

    }

}

```

### Step : 3

Grant the permissions from manifest `onClick()` method, <b><i>PermissionListener</i></b> callback will listen from user.

`onAppGranted()` will called when all permission is granted

`onAppDenied()` will called when permission is denied

# Grant Single

```java
       
       singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPermission.grantSingle(new PermissionListener() {

		    @Override
                    public void onAppGranted() {
                        //TODO on granted.....
                    }

                    @Override
                    public void onAppDenied() {
                         //TODO on denied.....
                    }

                },AppPermission.CAMERA);
            }
        });

```


# Grant Multiple

```java
       
       multipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPermission.grantMultiple(new PermissionListener() {
		
		@Override
                    public void onAppGranted() {
                        //TODO on granted.....
                    }

                    @Override
                    public void onAppDenied() {
                         //TODO on denied.....
                    }

                },AppPermission.CAMERA,AppPermission.RECEIVE_SMS,<PERMISSIONS...>);
            }
        });

```



# Grant All

```java
       
       allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPermission.grantAll(new PermissionListener() {
		
		@Override
                    public void onAppGranted() {
                        //TODO on granted.....
                    }

                    @Override
                    public void onAppDenied() {
                         //TODO on denied.....
                    }

                });
            }
        });

```


### Step : 4

To get the permission result, from `Activity` or `Fragment` need to <i>invokePermissionResult</i> of library in override the `onRequestPermissionsResult(...)` method, to listen the <b><i>PermissionListener</i></b> callback
```java

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        AppPermission.invokePermissionResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


```

# Implementation
Download the [@grant-app-permission-release.aar][1] file and copy to the libs folder, libs folder must be added to `project-level.gradle` file

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}

```
Add @aar file dependancy in `app-level.gradle` file

```gradle

dependencies {
implementation(name:'grant-app-permission-release', ext:'aar')
}

```
 [0]:https://github.com/Parmar-07/AppPermissionDemo/blob/master/app/demo/grant-permission-demo.apk
 [1]:https://github.com/Parmar-07/AppPermissionDemo/blob/master/grant_permission/aar/grant-app-permission-release.aar

