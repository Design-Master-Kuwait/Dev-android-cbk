package com.example.newbankingproject.ui.biometric

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.newbankingproject.databinding.ActivityFingerPrintBinding
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.login.LoginActivity
import com.example.newbankingproject.util.Constant
import com.example.newbankingproject.util.Utility
import java.util.concurrent.Executor

class FingerPrintActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var binding: ActivityFingerPrintBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeBiometric()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnCancel.setOnClickListener {
            this.finishAffinity()
        }
    }

    /**initializeBiometric is used to initialize the biometric*/
    private fun initializeBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = createBiometricPrompt()
        promptInfo = createPromptInfo()
        if (Utility.isBiometricAvailable(context = this)) {
            requestBiometricPermission()
        } else {
            Toast.makeText(this, Constant.BIOMETRIC_UNAVAILABLE, Toast.LENGTH_SHORT)
                .show()
        }
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
    private fun showBiometricPrompt() = biometricPrompt.authenticate(promptInfo)

    /**createPromptInfo is used to create prompt info*/
    private fun createPromptInfo() = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Authenticate using your fingerprint or face")
        .setNegativeButtonText("Cancel")
        .build()

    private val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            Toast.makeText(applicationContext, "Authentication succeeded", Toast.LENGTH_SHORT)
                .show()
            Intent(this@FingerPrintActivity, MainActivity::class.java).apply { startActivity(this) }
            finish()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            Toast.makeText(
                applicationContext,
                "Authentication error: $errString",
                Toast.LENGTH_SHORT
            ).show()
            Intent(
                this@FingerPrintActivity,
                LoginActivity::class.java
            ).apply { startActivity(this) }
        }

        override fun onAuthenticationFailed() {
            Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            Intent(
                this@FingerPrintActivity,
                LoginActivity::class.java
            ).apply { startActivity(this) }
            finishAffinity()
        }
    }

}