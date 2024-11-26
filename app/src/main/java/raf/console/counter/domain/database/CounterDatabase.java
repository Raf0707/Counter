package raf.console.counter.domain.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import raf.console.counter.domain.dao.CounterDao;
import raf.console.counter.domain.models.CounterItem;


@Database(entities = {CounterItem.class}, version = 1)
public abstract class CounterDatabase extends RoomDatabase {
    public abstract CounterDao counterDao();
    private static CounterDatabase INSTANCE;
    public static synchronized CounterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CounterDatabase.class, "counter_database")
                    .allowMainThreadQueries().build(); //.fallbackToDestructiveMigration()
        }
        return INSTANCE;
    }
}

