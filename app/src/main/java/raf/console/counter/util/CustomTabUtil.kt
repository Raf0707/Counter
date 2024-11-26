package raf.console.counter.util

import android.content.Context
import android.net.Uri


import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class CustomTabUtil {
    fun openCustomTab(context: Context?, URL: String?, colorInt: Int) {
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent = builder.build()
        if (context != null) {
            customTabsIntent.launchUrl(context, Uri.parse(URL))
        }

        //Set tab color
        val defaultColors: CustomTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        builder /*intentBuilder*/.setDefaultColorSchemeParams(defaultColors)

        //Set animation
        //builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        //builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
    }
}