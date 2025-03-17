package com.example.myapplication;

public class Item {
    private String routine;
    private boolean isChecked;

    public Item(String text) {
        this.routine = text;
        this.isChecked = false;
    }

    public String getText()  {
        return this.routine;
    }

    public boolean getChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
