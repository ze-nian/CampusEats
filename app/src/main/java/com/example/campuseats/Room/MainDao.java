package com.example.campuseats.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Query("DELETE FROM menu WHERE name= :sName")
    void delete(String sName);

    @Query("UPDATE menu SET name = :sName,quantity=:sQuantity WHERE name= :sName")
    void update(String sName,int sQuantity);

    @Query("SELECT * FROM menu a WHERE quantity= (SELECT MAX(quantity) FROM menu b WHERE a.name=b.name) GROUP BY name")
    List<MainData> getAll();

    @Query("SELECT SUM(price * quantity) FROM menu a WHERE quantity= (SELECT MAX(quantity) FROM menu b WHERE a.name=b.name GROUP BY name)")
    int getTotal();

    @Query("SELECT quantity FROM menu WHERE name=:sName ORDER BY quantity DESC")
    int getquantity(String sName) ;

    @Query("DELETE FROM menu")
    void clear() ;
}
