package raf.console.counter.ui.counter.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import raf.console.counter.R
import raf.console.counter.adapters.CounterAdapter
import raf.console.counter.databinding.FragmentMainSwipeBinding
import raf.console.counter.domain.models.CounterItem
import raf.console.counter.ui.counter.fragment.GestureCounterFragment
import raf.console.counter.ui.counter.viewmodel.CounterViewModel
import java.util.*

class MainSwipeFragment : Fragment(), CounterAdapter.HandleCounterClick {

    private lateinit var binding: FragmentMainSwipeBinding
    private lateinit var counterAdapter: CounterAdapter
    private var counterViewModel: CounterViewModel? = null
    private var counterForEdit: CounterItem? = null
    private var gestureCounterFragment = GestureCounterFragment()
    private var counterItem: CounterItem? = null
    private var title: String = ""
    private var target: Int = 0
    private var progress: Int = 0
    private var id: Int = 0

    private var combinedWord: String = ""
    private var targetValue: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainSwipeBinding.inflate(inflater)

        counterAdapter = CounterAdapter(requireContext(), this)

        counterViewModel = ViewModelProvider(this)
            .get(CounterViewModel::class.java)

        arguments?.let {
            combinedWord = it.getString("wordAndTranslation", "")
            targetValue = it.getInt("value", 200)
        }

        // Проверка, переданы ли аргументы для создания нового счетчика
        if (combinedWord.isNotEmpty()) {
            // Проверить, существует ли уже счетчик с таким title
            val liveData = counterViewModel?.findByTitle(combinedWord)
            liveData?.observe(viewLifecycleOwner, { existingCounters ->
                if (existingCounters.isNullOrEmpty()) {
                    // Счетчика с таким названием нет, создаем новый
                    val newCounterItem = CounterItem(
                        //id = 0, // ID можно будет обновить после сохранения в базе данных, если нужно
                        combinedWord,
                        targetValue,
                        0
                    )

                    // Вставка нового счетчика в базу данных
                    counterViewModel?.insert(newCounterItem)

                    // Устанавливаем новый счетчик для редактирования и открываем Material Alert
                    counterForEdit = newCounterItem
                    onMaterialAlert(true)

                    // Удалить наблюдатель после первого срабатывания
                    liveData.removeObservers(viewLifecycleOwner)
                } else {
                    // Счетчик с таким названием уже существует, выводим уведомление
                    Snackbar.make(binding.root, "Счетчик с таким названием уже существует", Snackbar.LENGTH_SHORT).show()
                }
            })
        }


        arguments?.let {
            title = it.getString("title", counterItem?.title ?: "")
            target = it.getInt("target", counterItem?.target ?: 0)
            progress = it.getInt("progress", counterItem?.progress ?: 0)
            id = it.getInt("id")
            counterItem = CounterItem(id, title, target, progress)
            counterViewModel?.update(counterItem!!)
        }

        binding.recycleCounter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !binding.fabAddCounter.isShown) {
                    binding.fabAddCounter.show()
                } else if (dy > 0 && binding.fabAddCounter.isShown) {
                    binding.fabAddCounter.hide()
                }
            }
        })

        binding.fabAddCounter.setOnClickListener {
            onMaterialAlert(false)
        }

        binding.searchCounters.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { counterViewModel?.findByNames(it) }
                return true
            }
        })

        binding.searchCounters.setOnClickListener {
            binding.searchCounters.clearFocus()
        }

        initRecycleView()
        initViewModel()
        counterViewModel?.allCounterList

        return binding.root
    }

    private fun initRecycleView() {
        binding.recycleCounter.layoutManager = LinearLayoutManager(context)
        binding.recycleCounter.setHasFixedSize(true)
        binding.recycleCounter.adapter = counterAdapter
    }

    private fun initViewModel() {
        counterViewModel?.counterlistObserver?.observe(viewLifecycleOwner, { counterItems ->
            if (counterItems == null) {
                binding.noRes.visibility = View.VISIBLE
                binding.recycleCounter.visibility = View.GONE
            } else {
                counterAdapter.setCounterList(counterItems)
                binding.recycleCounter.visibility = View.VISIBLE
                binding.noRes.visibility = View.GONE
            }
        })
    }

    private fun onMaterialAlert(isForEdit: Boolean) {
        val alert = MaterialAlertDialogBuilder(requireContext())

        val dialogView = layoutInflater.inflate(R.layout.create_counter_dialog, null)
        alert.setTitle(if (isForEdit) "Изменить счетчик" else "Новый счетчик")
        alert.setMessage("Введите название и цель")
        alert.setCancelable(true)

        val counterTitle: EditText = dialogView.findViewById(R.id.counterTitle)
        val counterTarget: EditText = dialogView.findViewById(R.id.counterTarget)
        val counterProgress: TextView = dialogView.findViewById(R.id.counterProgress)

        if (isForEdit) {
            counterTitle.setText(counterForEdit?.title)
            counterTarget.setText(counterForEdit?.target.toString())
        }

        alert.setNegativeButton("Отмена") { _, _ -> }

        alert.setPositiveButton("ОК") { _, _ ->
            val titleText = counterTitle.text.toString().ifEmpty { getRandomString(12) }
            val targetValue = counterTarget.text.toString().toIntOrNull() ?: 10
            val progressValue = counterProgress.text.toString().toIntOrNull() ?: 0

            if (isForEdit) {
                counterForEdit?.title = titleText
                counterForEdit?.target = targetValue
                counterViewModel?.update(counterForEdit!!)
            } else {
                counterViewModel?.insert(titleText, targetValue)
            }
        }

        alert.setView(dialogView)
        alert.show()
    }

    private fun getRandomString(length: Int): String {
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number = random.nextInt(3)
            val result = when (number) {
                0 -> (random.nextInt(26) + 65).toChar()
                1 -> (random.nextInt(26) + 97).toChar()
                else -> random.nextInt(10).toString()
            }
            sb.append(result)
        }
        return sb.toString()
    }

    override fun itemClick(counterItem: CounterItem) {
        val bundle = Bundle().apply {
            putString("title", counterItem.title)
            putInt("target", counterItem.target)
            putInt("progress", counterItem.progress)
            putInt("id", counterItem.id)
        }

        // Используем NavController для навигации
        findNavController().navigate(R.id.action_mainSwipeFragment_to_gestureCounterFragment, bundle)
    }

    override fun deleteItem(counterItem: CounterItem) {
        counterViewModel?.delete(counterItem)
    }

    override fun editItem(counterItem: CounterItem) {
        counterForEdit = counterItem
        onMaterialAlert(true)
    }
}
