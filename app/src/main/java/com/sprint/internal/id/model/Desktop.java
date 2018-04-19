package com.sprint.internal.id.model;

import android.util.Log;

import com.sprint.internal.id.LauncherFacadeException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public class Desktop {

    private static final String TAG = "LauncherFacade";

    private List<DesktopItem> mDesktopItems;
    private byte[] mExtras;

    public void addDesktopItem(DesktopItem desktopItem) throws LauncherFacadeException {
        if (null == desktopItem) {
            Log.e(TAG, "Failed : addDesktopItem() - desktopItem is null");
            return;
        }

        if (null == mDesktopItems) {
            mDesktopItems = new ArrayList<>();
        }

        ItemType type = desktopItem.getItemType();
        if (null == type) {
            Log.w(TAG, "Failed : addDesktopItem() - unknown type");
            desktopItem.setItemType(ItemType.APPLICATION);
        }

        if (type != null && type.equals(ItemType.APPWIDGET) && null == desktopItem.getAppWidgetProvider()) {
            throw new LauncherFacadeException("App Widgets MUST have the AppWidgetProvider set");
        }

        mDesktopItems.add(desktopItem);
    }

    public DesktopItem findDesktopItemById(long id) {
        DesktopItem desktopItem = null;
        if (null != mDesktopItems) {
            for (int i = 0; i < mDesktopItems.size(); i++) {
                DesktopItem item = mDesktopItems.get(i);
                if (item instanceof DesktopFolder) {
                    DesktopFolder folder = (DesktopFolder) item;
                    for (int j = 0; j < folder.getChildren().size(); j++) {
                        if (folder.getChildren().get(j).getId() == id) {
                            desktopItem = folder.getChildren().get(j);
                        }
                    }
                } else {
                    if (item.getId() == id) {
                        desktopItem = item;
                    }
                }
            }
            return desktopItem;
        }
        return null;
    }

    public List<DesktopItem> getDesktopItems() {
        return null != mDesktopItems ? mDesktopItems : new ArrayList<>();
    }

    public byte[] getExtras() {
        return null != mExtras ? mExtras : new byte[0];
    }

    public void removeDesktopItem(DesktopItem desktopItem) {
        if (null != mDesktopItems && null != desktopItem) {
            mDesktopItems.remove(desktopItem);
        }
    }

    public void setDesktopItems(List<DesktopItem> desktopItems) {
        if (null != desktopItems) {
            for (DesktopItem item : desktopItems) {
                if (item.getItemType() == ItemType.APPWIDGET && item.getAppWidgetProvider() == null) {
                    throw new IllegalArgumentException("App Widgets MUST have the AppWidgetProvider set: " + item.toString());
                }
            }
        }
        mDesktopItems = desktopItems;
    }

    public void setExtras(byte[] extras) {
        mExtras = extras;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Desktop {mDesktopItems.size = ");
        if (null == mDesktopItems) {
            sb.append("null");
        } else {
            sb.append(mDesktopItems.size()).append("}");
        }
        sb.append(",(mExtras == null) = ");
        if (null == mExtras) {
            sb.append("false");
        } else {
            sb.append("true");
        }
        return sb.toString();
    }
}
