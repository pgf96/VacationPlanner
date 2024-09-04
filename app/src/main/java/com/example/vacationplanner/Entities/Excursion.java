package com.example.vacationplanner.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;

    private String excursionTitle;
    private String date;
    private int vacationID;

    public Excursion(String excursionTitle, String date, int vacationID) {
        this.excursionTitle = excursionTitle;
        this.date = date;
        this.vacationID = vacationID;
    }

    @Ignore
    public Excursion(int excursionID, String excursionTitle, String date, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.date = date;
        this.vacationID = vacationID;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
