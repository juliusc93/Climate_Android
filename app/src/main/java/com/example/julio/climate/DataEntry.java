package com.example.julio.climate;

/**
 * Created by JULIO on 24/04/2015.
 */
import java.io.Serializable;

@SuppressWarnings("serial")
public class DataEntry implements Serializable {

    private String temp;

    public DataEntry() {

    }
    public DataEntry(String main, String description, String temp, String humidity, String day) {
        this.temp = temp;
    }
    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }
}
