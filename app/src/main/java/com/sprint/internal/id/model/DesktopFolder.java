package com.sprint.internal.id.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huguojin
 * @date 2018/4/8
 */

public class DesktopFolder extends DesktopItem {

    private List<DesktopItem> children;

    public void addChild(DesktopItem child) {
        if (null == child) {
            return;
        }
        if (null == children) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public List<DesktopItem> getChildren() {
        return this.children;
    }

    public void removeChild(DesktopItem child) {
        if (null != child && null != children) {
            this.children.remove(child);
        }
    }

    public void setChildren(List<DesktopItem> childrenList) {
        if (null == childrenList) {
            this.children.clear();
            return;
        }
        this.children = childrenList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (null != this.children && !this.children.isEmpty()) {
            for (DesktopItem item : this.children) {
                sb.append("***").append(item.toString());
            }
            sb.append("***");
        }
        return "DesktopFolder{" + super.toString() +
                "children=[" + sb.toString() +
                "]}";
    }
}
