package com.example.simpletodo;

import java.util.Date;

public class TodoItem {
    private String name;

    public TodoItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
