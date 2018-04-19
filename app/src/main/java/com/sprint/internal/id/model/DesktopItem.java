package com.sprint.internal.id.model;

import android.content.ComponentName;

import com.sprint.internal.id.LauncherSettings;

import java.util.Arrays;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public class DesktopItem {

    public static final long CONTAINER_DESKTOP = -100L;
    public static final long CONTAINER_HOTSEAT = -101L;
    private long _id;
    private long appWidgetId = -1L;
    private ComponentName appWidgetProvider;
    private int cellX;
    private int cellY;
    private Long container = Long.valueOf(-100L);
    private byte[] extras;
    private byte[] icon;
    private String iconPackage;
    private String iconResource;
    private IconType iconType = IconType.RESOURCE;
    private String intent;
    @Deprecated
    private boolean isShortcut;
    private ItemType itemType = ItemType.APPLICATION;
    private int screenPriority;
    private int spanX;
    private int spanY;
    private String title;
    @Deprecated
    private String uri;

    public long getAppWidgetId() {
        return appWidgetId;
    }

    public ComponentName getAppWidgetProvider() {
        return appWidgetProvider;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public Long getContainer() {
        return container;
    }

    public byte[] getExtras() {
        return extras;
    }

    public byte[] getIcon() {
        return icon;
    }

    public String getIconPackage() {
        return iconPackage;
    }

    public String getIconResource() {
        return iconResource;
    }

    public IconType getIconType() {
        return iconType;
    }

    public long getId() {
        return _id;
    }

    public String getIntent() {
        return intent;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getScreenPriority() {
        return screenPriority;
    }

    public int getSpanX() {
        return spanX;
    }

    public int getSpanY() {
        return spanY;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public boolean isShortcut() {
        return isShortcut;
    }


    public void setAppWidgetId(long appWidgetId) {
        this.appWidgetId = appWidgetId;
    }

    public void setAppWidgetProvider(ComponentName appWidgetProvider) {
        this.appWidgetProvider = appWidgetProvider;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public void setContainer(Long container) {
        this.container = container;
    }

    public void setExtras(byte[] extras) {
        this.extras = extras;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setIconPackage(String iconPackage) {
        this.iconPackage = iconPackage;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
    }

    public void setIconType(IconType iconType) {
        this.iconType = iconType;
    }

    public void setId(long id) {
        this._id = id;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setShortcut(boolean isShortcut) {
        this.isShortcut = isShortcut;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setScreenPriority(int screenPriority) {
        this.screenPriority = screenPriority;
    }

    public void setSpanX(int spanX) {
        this.spanX = spanX;
    }

    public void setSpanY(int spanY) {
        this.spanY = spanY;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "DesktopItem{" +
                "_id=" + _id +
                ", appWidgetId=" + appWidgetId +
                ", appWidgetProvider=" + appWidgetProvider +
                ", cellX=" + cellX +
                ", cellY=" + cellY +
                ", container=" + LauncherSettings.Favorites.containerToString(container.intValue()) +
                ", extras=" + Arrays.toString(extras) +
                ", icon=" + Arrays.toString(icon) +
                ", iconPackage='" + iconPackage + '\'' +
                ", iconResource='" + iconResource + '\'' +
                ", iconType=" + iconType.getType() +
                ", intent='" + intent + '\'' +
                ", itemType=" + LauncherSettings.Favorites.itemTypeToString(itemType.getType()) +
                ", screenPriority=" + screenPriority +
                ", spanX=" + spanX +
                ", spanY=" + spanY +
                ", title='" + title + '\'' +
                '}';
    }
}
