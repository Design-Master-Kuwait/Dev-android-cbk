package com.example.newbankingproject.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.biometric.BiometricManager
import com.example.data.utils.KeyStorePreference
import java.util.Locale

/*Utility is used to get all necessary code*/
class Utility {
    companion object {
        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        infix fun String?.toastMessage(context: Context?) {
            Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
        }

        /**changeLanguage is used to change the language and store in shared preference*/
        fun Context.changeLanguage(
            context: Activity,
            preference: KeyStorePreference,
            language: String,
        ) {
            var locale: Locale? = null
            locale = Locale(language)
            preference.storeLanguage(language)
            if (locale != null) {
                Locale.setDefault(locale)
            }
            val config = Configuration()
            config.locale = locale
            resources?.updateConfiguration(config, null)
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            context.finish()
        }

        fun isBiometricAvailable(context: Context): Boolean {
            val biometricManager = BiometricManager.from(context)
            return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        }
    }
}