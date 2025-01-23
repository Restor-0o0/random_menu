package com.example.random_menu.Data;

public class GroupCheck {
    public final Integer id;
    public final String name;
    public boolean active;


    public GroupCheck(Integer id, String name,int active) {
        this.id = id;
        this.name = name;
        if(active == 1)
        {
            this.active = true;
        }
        else{
            this.active = false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
