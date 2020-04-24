package com.example.notes;

public class Note {

    private int id;
    private String mTitle;
    private String mText;

    public Note(int id, String title, String text){
        this.id = id;
        mTitle = title;
        mText = text;
    }

    public int GetId(){
        return id;
    }

    public String GetTitle(){
        return mTitle;
    }

    public String GetText() {
        return mText;
    }
}
