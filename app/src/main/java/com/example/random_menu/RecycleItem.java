package com.example.random_menu;

public class RecycleItem {
    private String name;
    private boolean active;

    public RecycleItem(String name, boolean isButtonActive) {
        this.name = name;
        this.active = isButtonActive;
    }

    public String getName() {
        return name;
    }

    public boolean isButtonActive() {
        return active;
    }

    public void setButtonActive(boolean buttonActive) {
        active = buttonActive;
    }
}
