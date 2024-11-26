package raf.console.counter.domain.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import raf.console.counter.domain.dao.CounterDao;
import raf.console.counter.domain.database.CounterDatabase;
import raf.console.counter.domain.models.CounterItem;


public class CounterRepository {
    private CounterDao counterDao;
    private LiveData<List<CounterItem>> counterlist;

    public CounterRepository(Application application) {
        CounterDatabase counterDatabase = CounterDatabase.getInstance(application);
        counterDao = counterDatabase.counterDao();
        counterlist = (LiveData<List<CounterItem>>) counterDao.getAllCounters();
    }

    public void insertData(CounterItem counterItem) {
        new InsertTask(counterDao).execute(counterItem);
    }
    public void updateData(CounterItem counterItem) {
        new UpdateTask(counterDao).execute(counterItem);
    }
    public void deleteData(CounterItem counterItem) {
        new DeleteTask(counterDao).execute(counterItem);
    }
    public LiveData<List<CounterItem>> getAllData() {
        return counterlist;
    }
    public List<CounterItem> findByName(String title) {
        return counterDao.findByNames(title);
    }

    public LiveData<List<CounterItem>> findByTitle(String title) {
        return counterDao.findByTitle(title);
    }

    private static class InsertTask extends AsyncTask<CounterItem, Void, Void> {
        private CounterDao сounterDao;

        public InsertTask(CounterDao сounterDao) {
            this.сounterDao = сounterDao;
        }

        @Override
        protected Void doInBackground(CounterItem... counterItems) {
            сounterDao.insertCounter(counterItems[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<CounterItem, Void, Void> {
        private CounterDao сounterDao;

        public UpdateTask(CounterDao сounterDao) {
            this.сounterDao = сounterDao;
        }

        @Override
        protected Void doInBackground(CounterItem... counterItems) {
            сounterDao.updateCounter(counterItems[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<CounterItem, Void, Void> {
        private CounterDao сounterDao;

        public DeleteTask(CounterDao сounterDao) {
            this.сounterDao = сounterDao;
        }

        @Override
        protected Void doInBackground(CounterItem... counterItems) {
            сounterDao.deleteCounter(counterItems[0]);
            return null;
        }
    }
}
