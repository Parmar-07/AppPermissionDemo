package com.permissiondemo.abstractView;

public class Item {

    private String name;


    Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        Item itemCompare = (Item) obj;
        return itemCompare.getName().equals(this.getName());

    }

}