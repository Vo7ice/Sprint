package com.sprint.internal.id.model;

import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.sprint.internal.id.LauncherSettings;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public class LauncherInfo {

    private static final String TAG = "LauncherFacade";
    private static final String LAUNCHER_PKGNAME = "com.android.launcher3";
    private static final String LAUNCHER_CLASSNAME = "com.android.launcher3.Launcher";
    private Context mContext;
    private ComponentName mLauncher;
    private int mActiveScreens;
    private int mHomescreenColumns;
    private int mHomescreenRows;
    private int mMaxHomeScreens;
    private int mMaxHotseatCells;

    private int mNumFolderColumns;
    private int mNumFolderRows;

    public LauncherInfo(Context context) {
        mContext = context;
        mActiveScreens = getWorkspaceScreenCount();
        mMaxHomeScreens = 5;
        mMaxHotseatCells = 5;
        mHomescreenColumns = 5;
        mHomescreenRows = 5;
        mLauncher = new ComponentName(LAUNCHER_PKGNAME, LAUNCHER_CLASSNAME);
        mNumFolderColumns = 4;
        mNumFolderRows = 4;
    }

    public int getNumFolderColumns() {
        return mNumFolderColumns;
    }

    public int getNumFolderRows() {
        return mNumFolderRows;
    }

    public int getHomescreenColumns() {
        return mHomescreenColumns;
    }

    public int getHomescreenRows() {
        return mHomescreenRows;
    }

    public ComponentName getLauncher() {
        return mLauncher;
    }

    public int getMaxHomeScreens() {
        return mMaxHomeScreens;
    }

    public int getActiveHomeScreens() {
        return mActiveScreens;
    }

    public int getMaxHotseatCells() {
        return mMaxHotseatCells;
    }

    private int getWorkspaceScreenCount() {
        int workspaceScreenCount = 5;
        Cursor cursor = mContext.getContentResolver().query(LauncherSettings.WorkspaceScreens.CONTENT_URI,
                null, null, null, null);
        if (null != cursor) {
            try {
                workspaceScreenCount = cursor.getCount();
            } catch (SQLException e) {
                Log.e(TAG, "getWorkspaceScreenCount: cannot read screen info");
                return workspaceScreenCount;
            } finally {
                cursor.close();
            }
        }
        return workspaceScreenCount;
    }

    @Override
    public String toString() {
        return "LauncherInfo{" +
                "mContext=" + mContext +
                ", mLauncher=" + mLauncher +
                ", mActiveScreens=" + mActiveScreens +
                ", mHomescreenColumns=" + mHomescreenColumns +
                ", mHomescreenRows=" + mHomescreenRows +
                ", mMaxHomeScreens=" + mMaxHomeScreens +
                ", mMaxHotseatCells=" + mMaxHotseatCells +
                '}';
    }
}
