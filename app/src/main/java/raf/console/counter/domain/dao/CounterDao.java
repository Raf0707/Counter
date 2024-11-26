package raf.console.counter.domain.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import raf.console.counter.domain.models.CounterItem;


@Dao
public interface CounterDao {
    @Query("SELECT * FROM counters")
    LiveData<List<CounterItem>> getAllCounters();

    @Query("SELECT * FROM counters WHERE id IN (:userIds)")
    List<CounterItem> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM counters WHERE title LIKE :title LIMIT 1")
    CounterItem findByName(String title);

    @Query("SELECT * FROM counters WHERE title LIKE :title")
    List<CounterItem> findByNames(String title);

    @Query("SELECT * FROM counters WHERE title LIKE :title")
    LiveData<List<CounterItem>> findByTitle(String title);

    @Query("SELECT * FROM counters WHERE id = :id")
    List<CounterItem> getCounterID(long id);

    @Insert
    void insertAll(CounterItem...counterItems);

    @Insert
    void insertCounter(CounterItem counterItem);

    @Update
    void updateCounter(CounterItem counterItem);

    @Delete
    void deleteCounter(CounterItem counterItem);
}
