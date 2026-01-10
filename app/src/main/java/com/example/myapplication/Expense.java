package com.example.myapplication;

public class Expense {
    String description;
    double amount;
    int year, month, day;

    public Expense(String description, double amount, int year, int month, int day) {
        this.description = description;
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean isSameDay(int y, int m, int d) {
        return year == y && month == m && day == d;
    }
}
