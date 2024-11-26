package raf.console.counter.ui.about_app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import raf.console.counter.BuildConfig
import raf.console.counter.R
import raf.console.counter.databinding.FragmentAppAboutBinding
import raf.console.counter.util.CustomTabUtil


class AppAboutFragment : Fragment() {
    lateinit var binding: FragmentAppAboutBinding
    private val iconId = 0
    private val sPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            selectTheme = savedInstanceState.getString("theme")
            //iconId = savedInstanceState.getInt("iconTheme");
            loadTheme(selectTheme)
            //binding.themesBtn.setIcon(getResources().getDrawable(iconId));
            Log.d("onCreate", "load " + selectTheme)
        }
    }

    @SuppressLint("IntentReset")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppAboutBinding
            .inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appVersionBtn?.setText(
            StringBuilder()
                .append(getString(R.string.version))
                //.append(getString(R.string.str_dv))
                .append(BuildConfig.VERSION_NAME)
                //.append(getString(R.string.val_str_sk_right))
                .append(BuildConfig.VERSION_CODE)
                //.append(getString(R.string.val_str_sk_left))
                .toString()
        )

        binding.appVersionBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.version_copied),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    StringBuilder()
                        .append(getString(R.string.Tabiin_str_Version))
                        .append(getString(R.string.version))
                        .append(getString(R.string.str_dv))
                        .append(BuildConfig.VERSION_NAME)
                        .append(getString(R.string.val_str_sk_right))
                        .append(BuildConfig.VERSION_CODE)
                        .append(getString(R.string.val_str_sk_left))
                        .toString()
                )
            )
            true
        }

        binding.sourceCodeBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.link_to_source_copied),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.source_code_url)
                )
            )
            true
        }

        binding.donateBtn.setOnLongClickListener { v ->
            addOnClick(
                v, "donate link copied",
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    "https://www.donationalerts.com/r/raf0707"
                )
            )
            true
        }

        binding.rafailBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.raf_git_copylink),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.rafail_url)
                )
            )
            true
        }

        binding.mailRafBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.my_email_copylink),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.mail_raf)
                )
            )
            true
        }

        binding.rateBtn.setOnLongClickListener { v ->
            addOnClick(
                v, "RuStore link rate copied",
                ClipData.newPlainText(
                    "https://apps.rustore.ru/app/ibn.rustum.arabistic",
                    "https://apps.rustore.ru/app/ibn.rustum.arabistic"
                )
            )
            true
        }

        binding.vkGroupBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.vk_tabiin_coyplink),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.tabiin)
                )
            )
            true
        }

        binding.tgGroupBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.tg_tabiin_coyplink),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.tgLink)
                )
            )
            true
        }

        binding.otherAppsBtn.setOnLongClickListener { v ->
            addOnClick(
                v, "Tabiin's Apps article link copied",
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    "https://apps.rustore.ru/developer/ZPBnoCoBczpBFPZK0munW8NSpRTEayCj"
                )
            )
            true
        }

        binding.sourceCodeBtn.setOnClickListener { v ->
            CustomTabUtil()
                .openCustomTab(
                    activity,
                    getString(R.string.source_code_url),
                    R.color.purple_300
                )
        }

        binding.rafailBtn.setOnClickListener { v ->
            CustomTabUtil()
                .openCustomTab(
                    activity,
                    getString(R.string.rafail_url),
                    R.color.purple_300
                )
        }


        binding.mailRafBtn.setOnClickListener { v ->
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.setData(Uri.parse(getString(R.string.mailto)))
                .setType(getString(R.string.text_plain))
                .putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf<String>(getString(R.string.mail_raf))
                )
                .putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                .putExtra(
                    Intent.EXTRA_TEXT,
                    StringBuilder()
                        .append(getString(R.string.app_name))
                        .append(getString(R.string.semicolon))
                        .append(getString(R.string.version))
                        .append(getString(R.string.str_dv))
                        .append(BuildConfig.VERSION_NAME)
                        .append(getString(R.string.val_str_sk_right))
                        .append(BuildConfig.VERSION_CODE)
                        .append(getString(R.string.val_str_sk_left))
                        .toString()
                )

            emailIntent.setType(getString(R.string.text_plain))

            // setType("message/rfc822")
            try {
                startActivity(
                    Intent.createChooser(
                        emailIntent,
                        getString(R.string.email_client)
                    )
                )
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity,
                    R.string.no_email_client, Toast.LENGTH_SHORT
                ).show()
            }
        }



        binding.rateBtn.setOnClickListener { v ->
            CustomTabUtil()
                .openCustomTab(
                    activity,
                    "https://apps.rustore.ru/app/ibn.rustum.arabistic",
                    R.color.purple_300
                )
        }


        binding.vkGroupBtn.setOnClickListener { v ->
            CustomTabUtil()
                .openCustomTab(
                    activity,
                    getString(R.string.tabiin),
                    R.color.purple_300
                )
        }

        binding.otherAppsBtn.setOnClickListener { v ->
            val url =
                "https://apps.rustore.ru/developer/ZPBnoCoBczpBFPZK0munW8NSpRTEayCj"
            val uri = Uri.parse(url)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome") // замените на пакет вашего предпочитаемого браузера, если это не Chrome
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Если браузер не найден, откройте ссылку в стандартном браузере
                intent.setPackage(null)
                startActivity(intent)
            }
        }

        binding.settingsBtn.setOnClickListener { v ->
            findNavController().navigate(R.id.action_appAboutFragment2_to_settingsFragment2)
        }

        binding.donateBtn.setOnClickListener { v ->
            CustomTabUtil().openCustomTab(
                activity,
                "https://www.donationalerts.com/r/raf0707", R.color.md_theme_onSecondary
            )
        }

        binding.tgGroupBtn.setOnClickListener { v ->
            CustomTabUtil()
                .openCustomTab(
                    activity, "https://t.me/+OoI8UWDVVm0yMDNi",
                    R.color.md_theme_onSecondary
                )
        }

        binding.tgGroupBtn.setOnLongClickListener { v ->
            addOnClick(
                v, getString(R.string.tg_tabiin_coyplink),
                ClipData.newPlainText(
                    getString(R.string.getContext),
                    getString(R.string.tgLink)
                )
            )
            true
        }
    }

    fun addOnClick(view: View?, text: String?, clipData: ClipData?) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipboardManager.setPrimaryClip(clipData!!)
        Snackbar.make(requireView(), text!!, Snackbar.LENGTH_LONG).show()
    }

    fun saveTheme(selectTheme: String?) {
        val tranBundle = Bundle()
        val fragmentManager = fragmentManager
        val appAboutFragment = AppAboutFragment()
        tranBundle.putString("thm", selectTheme)
        appAboutFragment.arguments = tranBundle
    }

    fun loadTheme(selectTheme: String?) {
        var selectTheme = selectTheme
        val bundle = arguments
        if (bundle != null) {
            val selectThm = bundle.getString("thm")
            selectTheme = selectThm
            if (selectTheme == "system") {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }

            //saveTheme(selectTheme);
            requireActivity().recreate()
        } else if (selectTheme == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            requireActivity().recreate()
        } else if (selectTheme == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            requireActivity().recreate()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("theme", selectTheme)
        Log.d("onSaveInstanceState", "save " + selectTheme)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("onViewStateRestored", "restore " + selectTheme)
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        saveTheme(selectTheme)
        Log.d("onDestroy", "save " + selectTheme)
        super.onDestroy()
    }


    companion object {
        var selectTheme: String? = "system"
    }
}