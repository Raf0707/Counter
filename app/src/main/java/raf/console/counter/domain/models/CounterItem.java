package raf.console.counter.domain.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "counters")
public class CounterItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "target")
    public int target;

    @ColumnInfo(name = "progress")
    public int progress;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "counterType")
    public CounterType counterType;

    @Ignore
    public CounterItem(String title, int target, int progress) {
        this.title = title;
        this.target = target;
        this.progress = progress;
    }

    @Ignore
    public CounterItem(String title, int target, int progress, CounterType counterType) {
        this.title = title;
        this.target = target;
        this.progress = progress;
        this.counterType = counterType;
    }

    //@Ignore
    public CounterItem(int id, String title, int target, int progress) {
        this.id = id;
        this.title = title;
        this.target = target;
        this.progress = progress;
    }

    @Ignore
    public CounterItem(int id, String title, int target, int progress, CounterType counterType) {
        this.id = id;
        this.title = title;
        this.target = target;
        this.progress = progress;
        this.counterType = counterType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public CounterType getCounterType() {
        return counterType;
    }

    public void setCounterType(CounterType counterType) {
        this.counterType = counterType;
    }

    @Override
    public String toString() {
        return "CounterItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", target=" + target +
                ", progress=" + progress +
                ", completed=" + completed +
                ", counterType=" + counterType +
                '}';
    }
}
