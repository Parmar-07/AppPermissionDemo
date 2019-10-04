package com.permissiondemo.abstractView;

class SelectableItem extends Item {
    private boolean isSelected;

    SelectableItem(Item item, boolean isSelected) {
        super(item.getName());
        this.isSelected = isSelected;
    }

    boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
    }
}