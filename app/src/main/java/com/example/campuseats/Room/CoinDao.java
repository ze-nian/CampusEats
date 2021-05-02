package com.example.campuseats.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CoinDao {


    @Insert(onConflict = REPLACE)
    void insert(CoinData coinData);


    @Query("SELECT value FROM coin WHERE ID=:sID ORDER BY value DESC")
    int getcoinvalue(int sID);

    @Query("SELECT COUNT(id) FROM coin")
    int getcount();

    @Query("UPDATE coin SET value = :sValue WHERE ID=:sID")
    void update(int sID,int sValue);

    @Query("DELETE FROM coin")
    void clear() ;


}
