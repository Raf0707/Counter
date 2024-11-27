package raf.console.counter.ui.counter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import raf.console.counter.domain.models.CounterItem
import raf.console.counter.domain.repository.CounterRepository

class CounterViewModel(application: Application) : AndroidViewModel(application) {
    private val counterlist: LiveData<List<CounterItem>>
    private val counterRepository: CounterRepository
    val counterlistObserver1 = MutableLiveData<List<CounterItem>>()

    var currentCounter: MutableLiveData<CounterItem>? = null

    init {
        //counterlist = new LiveData<>();
        counterRepository = CounterRepository(application)
        counterlist = counterRepository.allData
    }

    val counterlistObserver: LiveData<List<CounterItem>>
        get() = counterlist

    fun getCurrentCounter(currentCounter: MutableLiveData<CounterItem>): MutableLiveData<CounterItem> {
        return currentCounter
    }

    fun findByNames(title: String?): List<CounterItem> {
        return counterRepository.findByName(title)
    }

    fun findByTitle(title: String?): LiveData<List<CounterItem>> {
        return counterRepository.findByTitle(title)
    }

    val allCounterList: LiveData<MutableList<CounterItem>>?
        get() = counterRepository.allData

    fun insert(counterItem: CounterItem?) {
        counterRepository.insertData(counterItem)
        allCounterList
    }

    fun insert(title: String?, target: Int) {
        //counterRepository.insertData(counterItem);
        val counterItem: CounterItem = CounterItem(title, target, 0)
        counterRepository.insertData(counterItem)
        //counterDatabase.counterDao().insertCounter(counterItem);
        allCounterList
    }

    fun update(counterItem: CounterItem?) {
        counterRepository.updateData(counterItem)
        allCounterList
    }

    fun delete(counterItem: CounterItem?) {
        counterRepository.deleteData(counterItem)
        allCounterList
    }
}