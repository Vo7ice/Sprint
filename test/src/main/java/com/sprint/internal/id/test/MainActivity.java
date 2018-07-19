package com.sprint.internal.id.test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.sprint.internal.id.LauncherFacade;
import com.sprint.internal.id.LauncherFacadeException;
import com.sprint.internal.id.model.Desktop;
import com.sprint.internal.id.model.LauncherInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huguojin
 */
public class MainActivity extends AppCompatActivity {

    private Button mGetFavorites, mDeleteItems, mDeleteWidgets, mGetLauncherInfo, mTriggerMobileID;
    private LauncherFacade mLauncherFacade;

    private static final String TAG = "MainActivity";

    private static final String PERMISSION = "com.sprint.internal.id.permission.LAUNCHERFACADE";
    private static final String READ_PERMISSION = "com.android.launcher3.permission.READ_SETTINGS";
    private static final String WRITE_PERMISSION = "com.android.launcher3.permission.WRITE_SETTINGS";
    private static final String MOBILEID_PERMISSION = "com.sprint.permission.RECEIVE_SW_START";
    private static String[] sNeedPermission;

    static {
        sNeedPermission = new String[]{PERMISSION, READ_PERMISSION, WRITE_PERMISSION};
    }

    private List<String> mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGetFavorites = findViewById(R.id.get_favorite);
        mDeleteItems = findViewById(R.id.delete_items);
        mDeleteWidgets = findViewById(R.id.delete_widgets);
        mGetLauncherInfo = findViewById(R.id.get_launcher_info);
        mTriggerMobileID = findViewById(R.id.send_to_mobile_id);
        mPermissions = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLauncherFacade = new LauncherFacade(getBaseContext());
        mPermissions.clear();
        for (String permission : sNeedPermission) {
            Log.d(TAG, "onResume: permission check:" + permission + ",result = " +
                    ActivityCompat.checkSelfPermission(MainActivity.this, permission));
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissions.add(permission);
            }
        }
        mGetFavorites.setOnClickListener(view -> {
            if (mPermissions.isEmpty()) {
                readDesktopFromLauncher();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, mPermissions.toArray(new String[mPermissions.size()]), 1);
            }
        });

        mDeleteItems.setOnClickListener(view -> {
            if (mPermissions.isEmpty()) {
                deleteDesktopItems();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, mPermissions.toArray(new String[mPermissions.size()]), 2);
            }
        });

        mDeleteWidgets.setOnClickListener(view -> {
            if (mPermissions.isEmpty()) {
                deleteWidgets();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, mPermissions.toArray(new String[mPermissions.size()]), 3);
            }
        });

        mGetLauncherInfo.setOnClickListener(view -> {
            if (mPermissions.isEmpty()) {
                getLauncherInfo();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, mPermissions.toArray(new String[mPermissions.size()]), 4);
            }
        });

        mTriggerMobileID.setOnClickListener(view -> {
            Log.d(TAG, "onResume: ActivityCompat.checkSelfPermission(MainActivity.this, MOBILEID_PERMISSION) = " +
                    ActivityCompat.checkSelfPermission(MainActivity.this, MOBILEID_PERMISSION));
            if (ActivityCompat.checkSelfPermission(MainActivity.this, MOBILEID_PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, mPermissions.toArray(new String[mPermissions.size()]), 5);
            } else {
                triggerMobileID();
            }
        });
    }

    private void triggerMobileID() {
        Intent intent = new Intent();
        intent.setAction("com.sprint.intent.action.SW_START");
        intent.setClassName("com.sprint.psdg.sw", "com.sprint.psdg.sw.receiver.SWStartReceiver");
        String permission = "com.sprint.permission.RECEIVE_SW_START";
        MainActivity.this.sendBroadcast(intent, permission);
    }

    private void getLauncherInfo() {
        try {
            LauncherInfo launcherInfo = mLauncherFacade.getLauncherInfo();
            Log.d(TAG, "onResume: mLauncherInfo = " + launcherInfo.toString());
        } catch (LauncherFacadeException e) {
            e.printStackTrace();
        }
    }

    private void deleteWidgets() {
        int[] widgets = new int[1];
        widgets[0] = 5;
        try {
            mLauncherFacade.deleteWidgetsFromHostByID(widgets);
            mLauncherFacade.notifyFavoritesChanges();
        } catch (LauncherFacadeException e) {
            e.printStackTrace();
        }
    }

    private void deleteDesktopItems() {
        long[] ids = new long[1];
        ids[0] = 19L;
        try {
            mLauncherFacade.deleteDesktopItemsByIDNoNotify(ids);
            mLauncherFacade.notifyFavoritesChanges();
        } catch (LauncherFacadeException e) {
            e.printStackTrace();
            Log.e(TAG, "onResume: permission error");
        }
    }

    private void readDesktopFromLauncher() {
        try {
            Desktop desktop = mLauncherFacade.getDesktop();
            Log.d(TAG, "onResume: Desktop = " + desktop);
        } catch (LauncherFacadeException e) {
            e.printStackTrace();
            Log.e(TAG, "onResume: permission error");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            Log.d(TAG, "onRequestPermissionsResult: grantResults[0] = " + grantResults[0]);
            switch (requestCode) {
                case 1:
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mPermissions.remove(permissions[i]);
                        }
                    }
                    if (mPermissions.isEmpty()) {
                        readDesktopFromLauncher();
                    }
                    break;
                case 2:
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mPermissions.remove(permissions[i]);
                        }
                    }
                    if (mPermissions.isEmpty()) {
                        deleteDesktopItems();
                    }
                    break;
                case 3:
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mPermissions.remove(permissions[i]);
                        }
                    }
                    if (mPermissions.isEmpty()) {
                        deleteWidgets();
                    }
                    break;
                case 4:
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mPermissions.remove(permissions[i]);
                        }
                    }
                    if (mPermissions.isEmpty()) {
                        getLauncherInfo();
                    }
                    break;
                case 5:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        triggerMobileID();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
