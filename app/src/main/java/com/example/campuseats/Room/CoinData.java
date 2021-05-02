package com.example.campuseats.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName="coin")
public class CoinData implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int ID;

    @ColumnInfo(name="value")
    private int value;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID=ID;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value=value;
    }

}

