package com.example.calendar;

public class Remind {
    int id;
    String title;
    String type;
    int color;
    String isAllDay = "Y";
    int Time[][] = new int[2][5];

    Remind(int id,String title,String type,int color, int[][] Time){
        this.id = id;
        this.title = title;
        this.type = type;
        this.color = color;
        this.Time = Time;
    }
}
