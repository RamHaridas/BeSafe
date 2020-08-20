package com.example.andriod.besafe786.admin;
public class AdListItem {
    public String text;
    public int count;
    public AdListItem(String text,int count){
        this.count=count;
        this.text=text;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
