package com.sprint.internal.id.model;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public enum IconType {
    /**
     * RESOURCE
     */
    RESOURCE(0),
    /**
     * BITMAP
     */
    BITMAP(1);

    private int type;

    IconType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
