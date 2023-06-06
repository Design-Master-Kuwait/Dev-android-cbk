package com.example.newbankingproject.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.data.utils.KeyStorePreference
import com.example.newbankingproject.databinding.ActivitySplashBinding
import com.example.newbankingproject.ui.biometric.FingerPrintActivity
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**SplashActivity is activity for splash screen*/
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var preference: KeyStorePreference
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
    }

    /**initializeView is used to initialize the views*/
    private fun initializeView() {
        lifecycleScope.launch {
            delay(4500)
            setLocale()
            if (preference.isFingerPrintEnable())
                openFingerPrintActivity()
            else
                checkLoginStatus()
        }
    }

    /**setBiometric is used to set biometric */
    private fun openFingerPrintActivity() {
        val intent = Intent(this@SplashActivity, FingerPrintActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.startActivity(intent)
        this@SplashActivity.finishAffinity()
    }

    /**checkLoginStatus is used to check the status of login screen*/
    private fun checkLoginStatus() {
        Intent(
            this,
            if (preference.isLogin()) MainActivity::class.java else LoginActivity::class.java
        ).apply { startActivity(this) }
        finish()
    }

    /**setLocale is used to set the locale*/
    private fun setLocale() {
        if (preference.getLanguage() != null) {
            val myLocale =
                Locale(preference.getLanguage().toString())
            val res = this.resources
            val conf = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, res.displayMetrics)
        }
    }
}