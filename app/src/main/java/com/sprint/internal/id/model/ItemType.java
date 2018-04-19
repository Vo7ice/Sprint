package com.sprint.internal.id.model;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public enum ItemType {
    /**
     * APPLICATION
     */
    APPLICATION(0),
    /**
     * SHORTCUT
     */
    SHORTCUT(1),
    /**
     * FOLDER
     */
    FOLDER(2),
    /**
     * APPWIDGET
     */
    APPWIDGET(4),
    /**
     * WIDGET_CLOCK
     */
    WIDGET_CLOCK(1000),
    /**
     * WIDGET_SEARCH
     */
    WIDGET_SEARCH(1001),
    /**
     * WIDGET_PHOTO_FRAME
     */
    WIDGET_PHOTO_FRAME(1002);

    private int type;

    ItemType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
