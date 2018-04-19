package com.sprint.internal.id;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;

import com.sprint.internal.id.model.Desktop;
import com.sprint.internal.id.model.DesktopFolder;
import com.sprint.internal.id.model.DesktopItem;
import com.sprint.internal.id.model.IconType;
import com.sprint.internal.id.model.ItemType;
import com.sprint.internal.id.model.LauncherInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public class LauncherFacade {

    public static final int API_VERSION = 2;

    private static final String TAG = "LauncherFacade";
    private static final String PERMISSION = "com.sprint.internal.id.permission.LAUNCHERFACADE";

    private Context mContext;
    private LauncherInfo mLauncherInfo;

    public LauncherFacade(Context context) {
        mContext = context;
        mLauncherInfo = new LauncherInfo(context);
    }

    /**
     * insert the desktop item into db
     *
     * @param desktopItem Desktop item
     * @return uri from db
     * @throws LauncherFacadeException permission check
     */
    public Uri addDesktopItemNoNotify(DesktopItem desktopItem) throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "addDesktopItemNoNotify: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            Log.d(TAG, "addDesktopItemNoNotify: " + desktopItem);
            return addDesktopItemNoNotifyImpl(desktopItem);
        }
    }

    /**
     * delete desktop item by id
     *
     * @param ids desktop item which need to be delete
     * @throws LauncherFacadeException permission check
     */
    public void deleteDesktopItemsByIDNoNotify(long[] ids) throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "deleteDesktopItemsByIDNoNotify: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            if (null == ids) {
                Log.e(TAG, "deleteDesktopItemsByIDNoNotify: ids is null!");
                return;
            }
            Log.d(TAG, "deleteDesktopItemsByIDNoNotify: " + Arrays.toString(ids));
            ContentResolver resolver = mContext.getContentResolver();
            for (long id : ids) {
                String[] selectionArgs = new String[1];
                selectionArgs[0] = String.valueOf(id);
                String idSelection = "_id = ?";
                String containerSelection = "container = ?";
                resolver.delete(LauncherSettings.Favorites.CONTENT_URI, idSelection, selectionArgs);
                resolver.delete(LauncherSettings.Favorites.CONTENT_URI, containerSelection, selectionArgs);
            }
        }
    }

    /**
     * delete widget by ids
     *
     * @param widgetIds widgets which need to be delete
     * @throws LauncherFacadeException permission check
     */
    public void deleteWidgetsFromHostByID(int[] widgetIds) throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "deleteWidgetsFromHostByID: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            if (null == widgetIds) {
                Log.e(TAG, "deleteWidgetsFromHostByID: widgetIds is null!");
                return;
            }
            ContentResolver resolver = mContext.getContentResolver();
            Bundle bundle = new Bundle();
            bundle.putIntArray(LauncherSettings.AppWidgets.EXTRA_APPWIDGET_IDS, widgetIds);
            resolver.call(LauncherSettings.Favorites.CONTENT_URI,
                    LauncherSettings.AppWidgets.METHOD_REMOVE_APPWIDGET_IDS, null, bundle);
        }
    }

    /**
     * get the desktop from db at this time
     *
     * @return desktop
     * @throws LauncherFacadeException permission check
     */
    public Desktop getDesktop() throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "getDesktop: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            Desktop desktop = new Desktop();
            desktop.setDesktopItems(getCurrentFavorites());
            Log.d(TAG, "getDesktop: " + desktop);
            return desktop;
        }
    }

    /**
     * get the launcher info
     *
     * @return launcher info
     * @throws LauncherFacadeException permission check
     */
    public LauncherInfo getLauncherInfo() throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "getLauncherInfo: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            Log.d(TAG, "getLauncherInfo:" + mLauncherInfo);
            return mLauncherInfo;
        }
    }

    /**
     * notify data change
     *
     * @throws LauncherFacadeException permission check
     */
    public void notifyFavoritesChanges() throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "notifyFavoritesChanges: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            Log.d(TAG, "notifyFavoritesChanges");
            ContentResolver resolver = mContext.getContentResolver();
            if (null == resolver) {
                Log.d(TAG, "notifyFavoritesChanges: Failed:Couldn't get ContentResolver");
                return;
            }
            resolver.notifyChange(LauncherSettings.Favorites.CONTENT_URI, null);
        }
    }

    /**
     * restore desktop
     *
     * @param desktop Desktop which will be restored
     * @return uri list form db
     * @throws LauncherFacadeException permission check
     */
    public List<Uri> restoreDesktop(Desktop desktop) throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "restoreDesktop: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            return setDesktop(desktop);
        }
    }

    /**
     * set new desktop
     *
     * @param desktop Desktop which will be set
     * @return uri list form db
     * @throws LauncherFacadeException permission check
     */
    public List<Uri> setNewDesktop(Desktop desktop) throws LauncherFacadeException {
        if (mContext.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "setNewDesktop: Permission is not granted!");
            throw new LauncherFacadeException("Permission is not granted!");
        } else {
            return setDesktop(desktop);
        }
    }

    /**
     * set db from desktop given
     *
     * @param desktop Desktop which is given
     * @return uri list from db
     */
    private List<Uri> setDesktop(Desktop desktop) {
        List<Uri> uriList = new ArrayList<>();
        wipeDesktop();
        List<DesktopItem> desktopItems = desktop.getDesktopItems();
        desktopItems.sort(Comparator.comparingInt(DesktopItem::getScreenPriority));

        for (DesktopItem desktopItem : desktopItems) {
            Log.d(TAG, "setDesktop: set desktop item:" + desktopItem);
            Uri itemUri = addDesktopItemNoNotifyImpl(desktopItem);
            if (null != itemUri) {
                Log.d(TAG, "setDesktop: set desktop item:" + desktopItem);
                uriList.add(itemUri);
            } else {
                Log.d(TAG, "setDesktop: couldn`t get uri at insert db for desktopitem : " + desktopItem);
            }
            if (desktopItem instanceof DesktopFolder) {
                DesktopFolder folder = (DesktopFolder) desktopItem;
                List<DesktopItem> children = folder.getChildren();
                long containerId = ContentUris.parseId(itemUri);
                if (!children.isEmpty()) {
                    for (DesktopItem child : children) {
                        Log.d(TAG, "setDesktop: set folder item:" + child);
                        child.setContainer(containerId);
                        Uri childUri = addDesktopItemNoNotifyImpl(child);
                        if (null != childUri) {
                            Log.d(TAG, "setDesktop: set folder item:" + child);
                            uriList.add(childUri);
                        } else {
                            Log.d(TAG, "setDesktop: couldn`t get uri at insert db for folderitem : " + child);
                        }
                    }
                } else {
                    Log.w(TAG, "setDesktop: children for folder:" + folder.getTitle() + " is empty!");
                }
            }
        }
        return uriList;
    }

    /**
     * wipe the old desktop
     */
    private void wipeDesktop() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(LauncherSettings.Favorites.CONTENT_URI, null, null);
        resolver.delete(LauncherSettings.WorkspaceScreens.CONTENT_URI, null, null);
    }

    /**
     * get current favorite item from db
     *
     * @return favorite items
     */
    private List<DesktopItem> getCurrentFavorites() {
        Log.d(TAG, "getCurrentFavorites: start");
        List<DesktopItem> desktopItemList = new ArrayList<>();
        LongSparseArray<DesktopFolder> desktopFolders = new LongSparseArray();
        List<DesktopItem> tempList = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor;
        if (null != resolver) {
            cursor = resolver.query(LauncherSettings.Favorites.CONTENT_URI, null, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    ColumnIndex index = new ColumnIndex(cursor);
                    DesktopItem item = toDesktopItem(cursor, index);
                    if (item instanceof DesktopFolder) {
                        desktopFolders.append(item.getId(), (DesktopFolder) item);
                    }
                    tempList.add(item);
                }
            } else {
                if (null != cursor) {
                    cursor.close();
                }
            }
            Log.d(TAG, "getCurrentFavorites: total size from db:" + tempList.size());
            for (DesktopItem desktopItem : tempList) {
                Long id = desktopItem.getId();
                if (desktopFolders.indexOfKey(id) != -1) {
                    Log.d(TAG, "getCurrentFavorites: filter folder child:" + id);
                    desktopFolders.get(id).addChild(desktopItem);
                } else {
                    desktopItemList.add(desktopItem);
                }
            }
            Log.d(TAG, "getCurrentFavorites: single desktopItem size:" + desktopItemList.size());
            Log.d(TAG, "getCurrentFavorites: folder desktopItem size:" + desktopFolders.size());
            for (int i = 0; i < desktopFolders.size(); i++) {
                desktopItemList.add(desktopFolders.get(desktopFolders.keyAt(i)));
            }
        }
        return desktopItemList;
    }

    /**
     * impl of add desktop item
     *
     * @param desktopItem desktop item
     * @return uri insert from db
     */
    private Uri addDesktopItemNoNotifyImpl(DesktopItem desktopItem) {
        if (null == desktopItem) {
            return null;
        }
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = toContentValues(desktopItem);
        return resolver.insert(LauncherSettings.Favorites.CONTENT_URI_EXTERNAL_ADD, values);
    }

    /**
     * convert desktop to contentvalues
     *
     * @param desktopItem DesktopItem
     * @return ContentValues
     */
    private ContentValues toContentValues(DesktopItem desktopItem) {
        ContentValues values = new ContentValues();
        long container = desktopItem.getContainer();
        values.put(LauncherSettings.Favorites.CONTAINER, container);
        if (container == DesktopItem.CONTAINER_HOTSEAT) {
            values.put(LauncherSettings.Favorites.CELLX, desktopItem.getScreenPriority());
            values.put(LauncherSettings.Favorites.CELLY, 0);
            values.put(LauncherSettings.Favorites.SCREEN, desktopItem.getScreenPriority());
        } else if (container == DesktopItem.CONTAINER_DESKTOP) {
            values.put(LauncherSettings.Favorites.CELLX, desktopItem.getCellX());
            values.put(LauncherSettings.Favorites.CELLY, desktopItem.getCellY());
            values.put(LauncherSettings.Favorites.SCREEN, desktopItem.getScreenPriority());
        } else {
            int rank = desktopItem.getCellY() * mLauncherInfo.getNumFolderColumns() + desktopItem.getCellX();
            values.put(LauncherSettings.Favorites.CELLX, desktopItem.getCellX());
            values.put(LauncherSettings.Favorites.CELLY, desktopItem.getCellY());
            values.put(LauncherSettings.Favorites.RANK, rank);
            values.put(LauncherSettings.Favorites.SCREEN, desktopItem.getScreenPriority());
        }
        values.put(LauncherSettings.Favorites.ICON, desktopItem.getIcon());
        values.put(LauncherSettings.Favorites.ICON_PACKAGE, desktopItem.getIconPackage());
        values.put(LauncherSettings.Favorites.ICON_RESOURCE, desktopItem.getIconResource());
        values.put(LauncherSettings.Favorites.ICON_TYPE, desktopItem.getIconType().getType());
        values.put(LauncherSettings.Favorites._ID, desktopItem.getId());
        values.put(LauncherSettings.Favorites.INTENT, desktopItem.getIntent());
        values.put(LauncherSettings.Favorites.ITEM_TYPE, desktopItem.getItemType().getType());
        values.put(LauncherSettings.Favorites.SPANX, desktopItem.getSpanX());
        values.put(LauncherSettings.Favorites.SPANY, desktopItem.getSpanY());
        values.put(LauncherSettings.Favorites.TITLE, desktopItem.getTitle());
        ItemType type = desktopItem.getItemType();
        if (ItemType.APPWIDGET.equals(type)) {
            if (desktopItem.getAppWidgetId() != -1L) {
                values.put(LauncherSettings.Favorites.APPWIDGET_ID, desktopItem.getAppWidgetId());
            }
            values.put(LauncherSettings.Favorites.APPWIDGET_PROVIDER, desktopItem.getAppWidgetProvider().flattenToString());
        }

        return values;
    }

    /**
     * read from launcher db
     *
     * @param cursor Cursor from db
     * @param index  cursor column index
     * @return desktopItem
     */
    private DesktopItem toDesktopItem(Cursor cursor, ColumnIndex index) {
        DesktopItem desktopItem;
        int itemTypeValue = cursor.getInt(index.get(LauncherSettings.Favorites.ITEM_TYPE));
        if (itemTypeValue == ItemType.FOLDER.getType()) {
            desktopItem = new DesktopFolder();
        } else {
            desktopItem = new DesktopItem();
        }
        desktopItem.setId(cursor.getLong(index.get(LauncherSettings.Favorites._ID)));
        String title = cursor.getString(index.get(LauncherSettings.Favorites.TITLE));
        if (null != title) {
            desktopItem.setTitle(title);
        }
        String intent = cursor.getString(index.get(LauncherSettings.Favorites.INTENT));
        if (null != intent) {
            desktopItem.setIntent(intent);
        }
        desktopItem.setContainer(cursor.getLong(index.get(LauncherSettings.Favorites.CONTAINER)));
        desktopItem.setScreenPriority(cursor.getInt(index.get(LauncherSettings.Favorites.SCREEN)));
        desktopItem.setCellX(cursor.getInt(index.get(LauncherSettings.Favorites.CELLX)));
        desktopItem.setCellY(cursor.getInt(index.get(LauncherSettings.Favorites.CELLY)));
        ItemType itemType = findItemType(itemTypeValue);
        if (itemType == ItemType.APPWIDGET) {
            desktopItem.setAppWidgetId(cursor.getInt(index.get(LauncherSettings.Favorites.APPWIDGET_ID)));
            String provider = cursor.getString(index.get(LauncherSettings.Favorites.APPWIDGET_PROVIDER));
            if (null != provider) {
                desktopItem.setAppWidgetProvider(ComponentName.unflattenFromString(provider));
            }
        } else if (itemType == ItemType.SHORTCUT) {
            String iconPackage = cursor.getString(index.get(LauncherSettings.Favorites.ICON_PACKAGE));
            String iconResource = cursor.getString(index.get(LauncherSettings.Favorites.ICON_RESOURCE));
            if (null != iconPackage) {
                desktopItem.setIconPackage(iconPackage);
            }
            if (null != iconResource) {
                desktopItem.setIconResource(iconResource);
            }
        }
        desktopItem.setItemType(itemType);
        int iconValue = 0;
        if (index.hasKey(LauncherSettings.Favorites.ICON_TYPE)) {
            iconValue = cursor.getInt(index.get(LauncherSettings.Favorites.ICON_TYPE));
        }
        IconType iconType = findIconType(iconValue);
        desktopItem.setIconType(iconType);
        byte[] icon = cursor.getBlob(index.get(LauncherSettings.Favorites.ICON));
        if (null != icon) {
            desktopItem.setIcon(icon);
        }
        return desktopItem;
    }

    private ItemType findItemType(int type) {
        ItemType itemType = ItemType.APPLICATION;
        ItemType[] values = ItemType.values();
        for (ItemType value : values) {
            if (value.getType() == type) {
                itemType = value;
            }
        }
        return itemType;
    }

    private IconType findIconType(int type) {
        IconType iconType = IconType.RESOURCE;
        IconType[] values = IconType.values();
        for (IconType value : values) {
            if (value.getType() == type) {
                iconType = value;
            }
        }
        return iconType;
    }


    static class ColumnIndex {
        Map<String, Integer> mColumnTable;

        public ColumnIndex(Cursor cursor) {
            if (cursor == null) {
                return;
            }
            mColumnTable = new HashMap();
            for (String indexKey : cursor.getColumnNames()) {
                mColumnTable.put(indexKey, cursor.getColumnIndex(indexKey));
            }
        }

        public int get(String indexKey) {
            if (!mColumnTable.containsKey(indexKey)) {
                throw new IllegalArgumentException("Invalid column name: " + indexKey);
            }
            return mColumnTable.get(indexKey);
        }

        public boolean hasKey(String indexKey) {
            return mColumnTable.containsKey(indexKey);
        }
    }
}
