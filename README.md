# Grant App Permissions

Runtime Permission library to access system permissions in Android L or higher.


## Usage

### Step : 1
Add app-level permissions in `mainifest` file

```xml 

    <uses-permission android:name="android.permission.CAMERA"/>
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


```java
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
