package raf.console.counter

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import raf.console.counter.databinding.ActivityMainBinding
import raf.console.counter.util.SharedPreferencesUtils

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration: AppBarConfiguration

    var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val nightIcon: Int =
            SharedPreferencesUtils.getInteger(this, "nightIcon", R.drawable.vectornightpress)

        //val contrast = SharedPreferencesUtils.getFloat(this, "contrastLevel", 0f)

        App.instance?.setNightMode()
        //App.instance?.updateContrast(contrast)

        if (SharedPreferencesUtils.getBoolean(
                this,
                "useDynamicColors"
            )
        ) DynamicColors.applyToActivityIfAvailable(
            this
        )

        if (SharedPreferencesUtils.getBoolean(this, "addFollowSystemIcon")) flag = true

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val navIcon = binding.toolbar.navigationIcon
        navIcon?.setTint(resources.getColor(R.color.white, theme))


        //binding.toolbar.navigationIconTint = R.color.white
        // Инициализируем NavController
        navController = findNavController(this, R.id.nav_host_fragment_content_main)

        // Инициализация AppBarConfiguration для toolbar (если он есть)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        /*appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainSwipeFragment, R.id.appAboutFragment)
        )*/
        //setupWithNavController(binding.navView, navController)

        setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включить кнопку "назад"
        //binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.arrow_back_24, theme) // Установка иконки
        binding.toolbar.navigationIcon?.setTint(getColor(R.color.white))

        if (savedInstanceState == null) {
            navController.navigate(R.id.mainSwipeFragment)
        }
    }

    // Обработчик нажатий на кнопку "назад"
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        /*if (id == R.id.settingsFragment2) {
            val navController = findNavController(this, R.id.nav_host_fragment_content_main)
            val currentDestination = navController.currentDestination
            if (currentDestination != null && currentDestination.id == R.id.settingsFragment) {
                return true
            }
            navController.navigate<Any>(R.id.settingsFragment2)
            return true
        }*/

        //return super.onOptionsItemSelected(item)

        return when (item.itemId) {
            R.id.appAboutFragment -> {
                navController.navigate(R.id.appAboutFragment2) // Навигация при выборе меню
                true
            }

            R.id.mainSwipeFragment -> {
                navController.navigate(R.id.mainSwipeFragment) // Навигация при выборе меню
                true
            }

            R.id.settingsFragment -> {
                navController.navigate(R.id.settingsFragment2) // Навигация при выборе меню
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu) // Подключаем меню из XML

        // Проверяем текущую тему (дневная или ночная)
        val isNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        // Устанавливаем иконки в зависимости от темы
        if (isNightMode) {
            menu?.findItem(R.id.appAboutFragment)?.setIcon(R.drawable.ic_vector_info_night)
            menu?.findItem(R.id.mainSwipeFragment)?.setIcon(R.drawable.baseline_task_white)
            menu?.findItem(R.id.settingsFragment)?.setIcon(R.drawable.settings_tool_night)
        } else {
            menu?.findItem(R.id.appAboutFragment)?.setIcon(R.drawable.ic_vector_info)
            menu?.findItem(R.id.mainSwipeFragment)?.setIcon(R.drawable.baseline_task_black)
            menu?.findItem(R.id.settingsFragment)?.setIcon(R.drawable.settings_tool)
        }

        return true
    }

}