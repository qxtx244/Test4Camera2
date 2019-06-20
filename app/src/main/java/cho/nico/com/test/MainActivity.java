package cho.nico.com.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;

import com.arcsoft.face.FaceEngine;
import com.example.camera2lib.Camera2Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResizeCameraViewCallback {
    private String[] permissonArray = new String[]
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
            };

    private List<String> mRequestPermission = new ArrayList<String>();

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;

    private FaceEngine faceEngine;

    AutoFitTextureView surfaceView;
    SurfaceHolder surfaceHolder;

    private final String TAG = getClass().getSimpleName();


    public static final String APP_ID = "6F8VPaapjic6BkoU2dbrfp1WKwdCjokFNqyFLAYFMRfi";
    public static final String SDK_KEY = "G2owqnL7C3CggWpTVGWQmw6b2JoMnB98DK5kkyibufwR";
    private String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private TipsDialog opyDialog;
    int screenWidth, screenHeight;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;



        initEngine();
        startrequestPermission();


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        surfaceView = findViewById(R.id.camera_sv);
        Camera2Utils.getInstance().setContext(getBaseContext());
        Camera2Utils.getInstance().setTextureView(surfaceView);
        Camera2Utils.getInstance().init();
//        Camera2Utils.getInstance().setPreviewCallback(this);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startrequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissonArray) {
                if (PackageManager.PERMISSION_GRANTED != checkPermission(permission, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(permission);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), ACTION_REQUEST_PERMISSIONS);
            } else {
                try {
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化引擎
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        faceEngine.active(this, APP_ID, SDK_KEY);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : permissonArray) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
        }
    }


    private void showProcessDialog() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                opyDialog = new TipsDialog(MainActivity.this);
                opyDialog.setTitleMsg("照片检测中...");
                opyDialog.show();
            }
        });

    }

    @Override
    public void resizeView(int w, int h) {

    }

    @Override
    public void playVideo() {

    }

    @Override
    public void setVideoPath(String path) {

    }

//    @Override
//    public void onPreviewCallback(byte[] nv21, int width, int height) {
//
//        FaceServer.getInstance().detectNv21(nv21, getBaseContext(), new FaceDetectCallback() {
//            @Override
//            public void detectFinish(int size, long times) {
//
//            }
//
//            @Override
//            public void detectFailed() {
//
//            }
//        }, width, height);
//    }
}
