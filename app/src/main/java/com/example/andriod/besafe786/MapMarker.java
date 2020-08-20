package com.example.andriod.besafe786;
public class MapMarker {
    private int spam, agree;
    private double Lat,Long;
    private String comment,format,Stamp,uid;
    public static int count = 0;
    public MapMarker(){
    }
    public void setuid(String uid) {
        this.uid = uid;
    }
    public void setAgree(int agree) {
        this.agree = agree;
    }
    public void setStamp(String Stamp){ this.Stamp=Stamp; }
    public void setFormat(String format){ this.format = format; }
    public void setLat(double lat) {
        Lat = lat;
    }
    public void setLong(double aLong) {
        Long = aLong;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setSpam(int spam){ this.spam = spam; }
    public double getLat() {
        return Lat;
    }
    public double getLong() {
        return Long;
    }
    public String getComment(){
        return comment;
    }
    public int getSpam(){ return spam; }
    public  String getFormat(){return format;}
    public String getStamp(){return Stamp;}
    public String getuid() {
        return uid;
    }
    public int getAgree() {
        return agree;
    }
}
