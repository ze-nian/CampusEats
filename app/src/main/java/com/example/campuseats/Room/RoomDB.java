package com.example.campuseats.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={MainData.class,CoinData.class},version=3,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static RoomDB instance;
    private static String DATABASE_NAME="database";
    public synchronized static RoomDB getInstance(Context context){
        if(database==null){
            database= Room.databaseBuilder(context.getApplicationContext()
            ,RoomDB.class,DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();
        }
        return database;
    }


    //test
    RoomDB getDatabase(final Context context){
        if(instance !=null){
            synchronized (RoomDB.class){
                instance=Room.databaseBuilder(context,RoomDB.class,"Database").build();

            }
        }
        return instance;
    }


    public abstract MainDao mainDao();
    public abstract CoinDao coinDao();
}
