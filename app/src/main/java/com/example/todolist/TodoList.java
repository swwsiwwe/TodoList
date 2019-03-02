package com.example.todolist;

public class TodoList {
    private String text;
    private int id;
    private boolean check;
    public TodoList(String text,int id,boolean check){
        this.text = text;
        this.id = id;
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
    public boolean getCheck(){
        return check;
    }
}
