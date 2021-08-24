package com.example.simpletodo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoItem implements Comparable<TodoItem> {
    private String name;
    private Date date;

    static Date parseDateString(String dateString) {
        int month, day, year;
        month = Integer.parseInt(dateString.split("-")[0]) - 1;
        day = Integer.parseInt(dateString.split("-")[1]);
        year = Integer.parseInt(dateString.split("-")[2]);

        Calendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }

    public TodoItem(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        String datePattern = "MM-dd-yyyy";
        DateFormat df = new SimpleDateFormat(datePattern);

        return df.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(TodoItem o) {
        return this.getDate().compareTo(o.getDate());
    }
}
