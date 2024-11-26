package raf.console.counter

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import raf.console.counter.util.SharedPreferencesUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setNightMode()  // Apply saved night mode at startup
        //setContrast()   // Apply saved contrast at startup
    }

    // This method applies the selected night mode theme
    fun setNightMode() {
        val nightMode = SharedPreferencesUtils.getInteger(this, "nightMode", 1)

        val modes = intArrayOf(
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )

        AppCompatDelegate.setDefaultNightMode(modes[nightMode])
    }

    // This method applies the selected contrast theme
    fun setContrast() {
        val contrastLevel = SharedPreferencesUtils.getFloat(this, "contrastLevel", 1.0f)

        // Adjust UI elements, such as colors, based on the contrast level
        // For example, you can apply a contrast multiplier to your theme colors
        applyContrast(contrastLevel)
    }

    // Apply contrast to the UI (this can be expanded based on your app's needs)
    fun applyContrast(contrastLevel: Float) {
        // Example of contrast adjustment (you can implement more complex logic here)
        val contrastMultiplier = when (contrastLevel) {
            0f -> 0.7f  // Lower contrast
            1f -> 1.0f  // Normal contrast
            2f -> 1.3f  // High contrast
            else -> 1.0f
        }

        // Apply the contrast multiplier to your theme or specific UI elements
        // This could involve modifying color values or other UI attributes
        // Here, you would apply your logic to adjust the UI appearance (e.g., colors)
    }

    // Update the night mode and save the setting in SharedPreferences
    fun updateNightMode(nightMode: Int) {
        SharedPreferencesUtils.saveInteger(this, "nightMode", nightMode)
        setNightMode()
    }

    // Update the contrast level and save it in SharedPreferences
    fun updateContrast(contrastLevel: Float) {
        SharedPreferencesUtils.saveFloat(this, "contrastLevel", contrastLevel)
        setContrast()  // Apply the new contrast
    }

    companion object {
        internal var instance: App? = null

        fun getInstance(): App {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }
}
