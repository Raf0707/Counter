package raf.console.counter.ui.counter.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import raf.console.counter.R
import raf.console.counter.databinding.FragmentGestureCounterBinding
import raf.console.counter.domain.models.CounterItem
import raf.console.counter.ui.counter.viewmodel.CounterViewModel
import raf.console.counter.util.OnSwipeTouchListener

class GestureCounterFragment : Fragment() {

    private lateinit var binding: FragmentGestureCounterBinding
    private lateinit var handler: Handler
    private var counter = 0
    private lateinit var counterItem: CounterItem
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var vibrator: Vibrator
    private lateinit var navController: NavController

    private val defaultValue = "10"
    private var maxValue: Int = 0

    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGestureCounterBinding.inflate(inflater, container, false)

        // ViewModel initialization
        counterViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(CounterViewModel::class.java)

        // NavController initialization
        navController = findNavController()

        arguments?.let {
            val title = it.getString("title")
            val target = it.getInt("target")
            val progress = it.getInt("progress")
            val id = it.getInt("id")

            binding.counterTitle.setText(title)
            binding.counterTarget.setText(target.toString())
            counter = progress
            binding.gestureCounter.text = counter.toString()

            counterItem = CounterItem(id, title ?: "", target, progress)
        }

        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        handler = Handler()

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.openSettingsBtn.setOnClickListener {
            navController.navigate(R.id.action_gestureCounterFragment_to_appAboutFragment2)
            saveCounterItem()
            counterViewModel.update(counterItem)
        }

        binding.deleteCounter.setOnClickListener {
            removeCounterAlert()
        }

        binding.openCounterListBtn.setOnClickListener {
            navController.navigate(R.id.action_gestureCounterFragment_to_mainSwipeFragment)
            saveCounterItem()
            counterViewModel.update(counterItem)
        }

        binding.openTutorialBtn.setOnClickListener {
            tutorialCounterAlert()
            saveCounterItem()
            counterViewModel.update(counterItem)
        }

        binding.editCounter.setOnClickListener {
            if (isEditing) {
                // Сохраняем изменения
                binding.counterTarget.setText(
                    binding.counterTarget.text.toString().replace("[\\.\\-,\\s]+", "")
                )

                binding.counterTarget.isCursorVisible = false
                binding.counterTarget.isFocusableInTouchMode = false
                binding.counterTarget.isEnabled = false

                binding.counterTitle.isCursorVisible = false
                binding.counterTitle.isFocusableInTouchMode = false
                binding.counterTitle.isEnabled = false

                if (binding.counterTarget.text.toString().isEmpty()) {
                    binding.counterTarget.setText(defaultValue)
                    maxValue = binding.counterTarget.text.toString().toInt()

                    Snackbar.make(
                        requireView(),
                        "Вы не ввели цель. По умолчанию: $defaultValue",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    if (binding.counterTarget.text.toString().toInt() <= 0) {
                        Snackbar.make(
                            requireView(),
                            "Введите число больше нуля!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        Snackbar.make(
                            requireView(),
                            "Цель установлена",
                            Snackbar.LENGTH_LONG
                        ).show()

                        maxValue = binding.counterTarget.text.toString().toInt()
                    }
                }

                counterItem.title = binding.counterTitle.text.toString()
                counterItem.target = binding.counterTarget.text.toString().toInt()
                counterViewModel.update(counterItem)

                // Меняем иконку на карандаш и переключаем флаг
                binding.editCounter.setBackgroundResource(R.drawable.ic_baseline_edit_24) // Укажите ID ресурса карандаша
                isEditing = false
            } else {
                // Включаем режим редактирования
                binding.counterTarget.isCursorVisible = true
                binding.counterTarget.isFocusableInTouchMode = true
                binding.counterTarget.isEnabled = true

                binding.counterTitle.isCursorVisible = true
                binding.counterTitle.isFocusableInTouchMode = true
                binding.counterTitle.isEnabled = true

                binding.counterTarget.requestFocus()
                binding.counterTarget.setSelection(binding.counterTarget.text!!.length)

                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                )
                requireActivity().window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                )

                val imm = requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.showSoftInput(binding.counterTarget, InputMethodManager.SHOW_FORCED)

                // Меняем иконку на галочку и переключаем флаг
                binding.editCounter
                    .setBackgroundResource(R.drawable.ic_baseline_check_24) // Укажите ID ресурса галочки
                isEditing = true
            }
        }

        binding.counterGestureView.setOnTouchListener(object : OnSwipeTouchListener(binding.counterGestureView.context) {
            @SuppressLint("MissingPermission")
            override fun onClick() {
                vibrator.vibrate(50)
                counter++
                binding.gestureCounter.text = counter.toString()
                saveCounterItem()

                if (counter == binding.counterTarget.text.toString().toInt()) {
                    vibrator.vibrate(1000)
                    Snackbar.make(requireView(), "Цель достигнута! Да вознаградит вас Аллах!", Snackbar.LENGTH_LONG).show()
                }
                counterViewModel.update(counterItem)
            }

            @SuppressLint("MissingPermission")
            override fun onSwipeDown() {
                vibrator.vibrate(50)
                counter--
                binding.gestureCounter.text = counter.toString()
                saveCounterItem()
                counterViewModel.update(counterItem)
            }

            @SuppressLint("MissingPermission")
            override fun onLongClick() {
                vibrator.vibrate(200)
                if (counter != 0) onResetCounterAlert()
                saveCounterItem()
                counterViewModel.update(counterItem)
            }
        })
    }

    private fun saveCounterItem() {
        /*counterItem = CounterItem(
            binding.counterTitle.text.toString(),
            binding.counterTarget.text.toString().toInt(),
            counter
        )*/
        counterItem.apply {
            title = binding.counterTitle.text.toString()
            target = binding.counterTarget.text.toString().toInt()
            progress = counter
        }

        // saveText()

        counterViewModel.update(counterItem)
    }

    @SuppressLint("MissingPermission")
    private fun onResetCounterAlert() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Reset")
            .setMessage("Вы уверены, что хотите обновить счетчик?")
            .setPositiveButton("Да") { _, _ ->
                vibrator.vibrate(200)
                counter = 0
                binding.gestureCounter.text = "0"
            }
            .setNeutralButton("Отмена") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun tutorialCounterAlert() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Жесты счетчика")
            .setMessage("Нажатие на экран: +1 \n Свайп вниз: -1 \n Долгое нажатие на экран: обновление счетчика до 0")
            .setPositiveButton("Понял") { _, _ ->
                vibrator.vibrate(200)
                counter = 0
                binding.gestureCounter.text = "0"
                Snackbar.make(binding.root, "Я рад за тебя", Snackbar.LENGTH_SHORT).show()
            }
            .setNeutralButton("Не понял") { dialog, _ ->
                Snackbar.make(binding.root, "Прочитай еще раз", Snackbar.LENGTH_LONG)
                    .setAction("Хорошо", View.OnClickListener {
                        tutorialCounterAlert()
                    })
                    .show()
                dialog.cancel()
            }
            .show()
    }

    private fun removeCounterAlert() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Remove")
            .setMessage("Вы уверены, что хотите удалить счетчик? Чтобы удалить счетчик, вернитесь на главную страницу")
            .setPositiveButton("Удалить") { _, _ ->
                counterViewModel.delete(counterItem)
                navController.navigate(R.id.action_gestureCounterFragment_to_mainSwipeFragment)
            }
            .setNeutralButton("Отмена") { dialog, _ -> dialog.cancel() }
            .show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        saveCounterItem()
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        saveCounterItem()
        super.onStop()

    }

    override fun onPause() {
        saveCounterItem()
        super.onPause()

    }

    override fun onDestroyView() {
        saveCounterItem()
        super.onDestroyView()

    }

    override fun onDestroy() {
        saveCounterItem()
        super.onDestroy()

    }

    override fun onDetach() {
        saveCounterItem()
        super.onDetach()

    }
}
