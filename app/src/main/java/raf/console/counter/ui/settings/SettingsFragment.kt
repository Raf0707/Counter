package raf.console.counter.ui.settings


import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.color.DynamicColors
import com.google.android.material.switchmaterial.SwitchMaterial
import raf.console.counter.R
import raf.console.counter.databinding.FragmentSettingsBinding
import raf.console.counter.domain.models.CounterItem
import raf.console.counter.ui.counter.viewmodel.CounterViewModel
import raf.console.counter.util.SharedPreferencesUtils


class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    private var switchMaterial: SwitchMaterial? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        switchMaterial = binding!!.dynamicColorsSwitch
        binding!!.appThemeRadioGroup.check(
            SharedPreferencesUtils.getInteger(
                requireContext(),
                "checkedButton",
                R.id.setFollowSystemTheme
            )
        )
        binding!!.dynamicColorsSwitch.isEnabled = DynamicColors.isDynamicColorAvailable()
        switchMaterial!!.isChecked =
            SharedPreferencesUtils.getBoolean(requireContext(), "useDynamicColors")


        loadRadioButtonIcons()

        /*

        // Инициализация SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("app_config", Context.MODE_PRIVATE)

        // Загружаем сохраненное значение контраста
        val savedContrast: Float =
            SharedPreferencesUtils.getFloat(requireContext(), "contrastLevel", 1.0f)


        // Устанавливаем значение слайдера
        binding?.contrastSlider?.setValue(savedContrast)


        // Обработчик изменения слайдера
        binding?.contrastSlider?.addOnChangeListener { slider, value, fromUser ->
            val contrastLevel =
                Math.round(value).toFloat() // Преобразуем в целое значение
            updateContrast(contrastLevel)
            saveContrastLevel(contrastLevel)
        }


        // Обработка выбора темы через RadioGroup
        val appThemeRadioGroup = binding?.appThemeRadioGroup
        appThemeRadioGroup?.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            if (checkedId == R.id.setFollowSystemTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else if (checkedId == R.id.setLightTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else if (checkedId == R.id.setNightTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

         */

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.appThemeRadioGroup.check(
            SharedPreferencesUtils.getInteger(
                requireContext(),
                "checkedButton",
                R.id.setFollowSystemTheme
            )
        )

        // Слушатель для изменения темы
        binding!!.appThemeRadioGroup.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.setFollowSystemTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    SharedPreferencesUtils.saveInteger(
                        requireContext(),
                        "checkedButton",
                        R.id.setFollowSystemTheme
                    )
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 1)
                    requireActivity().recreate()
                }

                R.id.setLightTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    SharedPreferencesUtils.saveInteger(
                        requireContext(),
                        "checkedButton",
                        R.id.setLightTheme
                    )
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 2)
                    requireActivity().recreate()
                }

                R.id.setNightTheme -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    SharedPreferencesUtils.saveInteger(
                        requireContext(),
                        "checkedButton",
                        R.id.setNightTheme
                    )
                    SharedPreferencesUtils.saveInteger(requireContext(), "nightMode", 3)
                    requireActivity().recreate()
                }
            }
            loadRadioButtonIcons()
            //updateRadioButtonIcons(checkedId)
        }

        // Слушатель для динамических цветов
        switchMaterial!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                DynamicColors.applyToActivitiesIfAvailable(
                    requireActivity().application
                )
            } else {
                DynamicColors.applyToActivitiesIfAvailable(
                    requireActivity().application,
                    R.style.Theme_Counter
                )
            }
            SharedPreferencesUtils.saveBoolean(requireContext(), "useDynamicColors", isChecked)
            requireActivity().recreate()
        }
    }

    private fun loadRadioButtonIcons() {
        // Получаем сохраненный идентификатор выбранной радиокнопки
        val selectedId = SharedPreferencesUtils.getInteger(requireContext(), "checkedButton", R.id.setFollowSystemTheme)

        val radioButtons = listOf(
            binding!!.setFollowSystemTheme,
            binding!!.setLightTheme,
            binding!!.setNightTheme
        )

        radioButtons.forEach { radioButton ->
            when (radioButton.id) {
                R.id.setFollowSystemTheme -> {
                    if (selectedId == R.id.setFollowSystemTheme) {
                        // Проверяем текущую тему через UiModeManager
                        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                        when (currentNightMode) {
                            Configuration.UI_MODE_NIGHT_YES -> {
                                // Тема темная
                                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_night, 0)
                            }
                            Configuration.UI_MODE_NIGHT_NO -> {
                                // Тема светлая
                                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_light, 0)
                            }
                            else -> {
                                // Неизвестный режим (например, системный)
                                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            }
                        }
                    } else {
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)  // Скрываем галочку
                    }
                }
                R.id.setLightTheme -> {
                    if (selectedId == R.id.setLightTheme) {
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_light, 0)
                    } else {
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)  // Скрываем галочку
                    }
                }
                R.id.setNightTheme -> {
                    if (selectedId == R.id.setNightTheme) {
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_night, 0)
                    } else {
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)  // Скрываем галочку
                    }
                }
            }
        }
    }

    private fun updateContrast(contrastLevel: Float) {
        val isNightModeActive = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false // По умолчанию предполагаем светлый режим
        }

        when (contrastLevel) {
            0f ->
                if (isNightModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

            1f ->                 // Средний контраст
                applyContrastTheme(R.style.ThemeOverlay_AppTheme_MediumContrast)

            2f ->                 // Высокий контраст
                applyContrastTheme(R.style.ThemeOverlay_AppTheme_HighContrast)
        }
    }

    private fun applyContrastTheme(themeResId: Int) {
        // Применяем тему с нужным контрастом
        requireActivity().setTheme(themeResId)
        requireActivity().recreate() // Перезагружаем активность для применения темы
    }

    private fun saveContrastLevel(contrastLevel: Float) {
        val editor: SharedPreferences.Editor = sharedPreferences?.edit()!!
        editor.putFloat("contrastLevel", contrastLevel)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
