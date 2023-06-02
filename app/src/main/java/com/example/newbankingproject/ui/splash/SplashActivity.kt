package com.example.newbankingproject.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.data.utils.KeyStorePreference
import com.example.newbankingproject.databinding.ActivitySplashBinding
import com.example.newbankingproject.ui.biometric.FingerPrintActivity
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.login.LoginActivity
import com.example.newbankingproject.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.Executor
import javax.inject.Inject

/**SplashActivity is activity for splash screen*/
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var preference: KeyStorePreference
    lateinit var binding: ActivitySplashBinding
    private val DELAY: Long = 4500

    private lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var looper: Looper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
    }

    /**initializeBiometric is used to initialize the biometric authentication*/
    private fun initializeBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = createBiometricPrompt()
        promptInfo = createPromptInfo()
        if (Utility.isBiometricAvailable(context = this)) {
            requestBiometricPermission()
        } else {
            Toast.makeText(this, "Biometric authentication is not available", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**initializeView is used to initialize the views*/
    private fun initializeView() {
        lifecycleScope.launch {
            delay(4500)
            setLocale()
            if (preference.isFingerPrintEnable())
                setBiometric()
            else
                checkLoginStatus()
        }
//        Looper.myLooper()?.let {
//            Handler(it).postDelayed({
//                setLocale()
//                if (preference.isFingerPrintEnable())
//                    setBiometric()
//                else
//                    checkLoginStatus()
//            }, 4500L)
//        }
    }

    private fun setBiometric() {
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


    /**createBiometricPrompt is used to create  the biometric prompt*/
    private fun createBiometricPrompt() = BiometricPrompt(this, executor, authenticationCallback)

    /**requestBiometricPermission is used to request the biometric permissions*/
    private fun requestBiometricPermission() {
        val permission = Manifest.permission.USE_BIOMETRIC
        val requestCode = 123

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            showBiometricPrompt()
        }
    }

    /**showBiometricPrompt is used to show biometric prompt*/
    private fun showBiometricPrompt() {
        // Display the biometric prompt
        biometricPrompt.authenticate(promptInfo)
    }

    /**createPromptInfo is used to create prompt info*/
    private fun createPromptInfo() = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Authenticate using your fingerprint or face")
        .setNegativeButtonText("Cancel")
        .build()

    private val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            // Biometric authentication succeeded
            Toast.makeText(applicationContext, "Authentication succeeded", Toast.LENGTH_SHORT)
                .show()
//            Intent(this@SplashActivity, MainActivity::class.java).apply { startActivity(this) }
            finish()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            // Biometric authentication encountered an error
            Toast.makeText(
                applicationContext,
                "Authentication error: $errString",
                Toast.LENGTH_SHORT
            ).show()
//            Intent(this@SplashActivity, LoginActivity::class.java).apply { startActivity(this) }
        }

        override fun onAuthenticationFailed() {
            Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
//            Intent(this@SplashActivity, LoginActivity::class.java).apply { startActivity(this) }
            finishAffinity()
        }
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