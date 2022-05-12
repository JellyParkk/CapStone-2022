package com.example.Capstone;

public class DataModel {
    String text_title;
    String text_date;

    public String getText_title() {
        return text_title;
    }
    public void setText_title(String title) {
        this.text_title = title;
    }
    public String getText_date() {
        return text_date;
    }
    public void setText_date(String date) {
        this.text_date = date;
    }
    public DataModel(String title,String date) {
        this.text_title = title;
        this.text_date = date;
    }
}
