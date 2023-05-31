package com.example.newbankingproject.ui.biometric

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.newbankingproject.databinding.ActivityBiometricBinding
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.util.Utility
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {
    lateinit var binding: ActivityBiometricBinding
    private lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun createBiometricPrompt() = BiometricPrompt(this, executor, authenticationCallback)

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

    private fun showBiometricPrompt() {
        // Display the biometric prompt
        biometricPrompt.authenticate(promptInfo)
    }

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
            Intent(this@BiometricActivity, MainActivity::class.java).apply { startActivity(this) }
            finish()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            // Biometric authentication encountered an error
            Toast.makeText(
                applicationContext,
                "Authentication error: $errString",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onAuthenticationFailed() {
            // Biometric authentication failed
            Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }

    companion object{
        val TAG = BiometricActivity::class.java.simpleName
    }

}