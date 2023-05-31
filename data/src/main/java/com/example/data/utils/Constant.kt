package com.example.data.utils

import androidx.security.crypto.MasterKeys
import org.intellij.lang.annotations.Language

object Constant {
    const val BASE_URL = "http://16.170.222.118:3500/"
    val MASTER_KEY_ALIAS = MasterKeys.AES256_GCM_SPEC
    const val KEY_STORE_NAME = "bank_preference"
    const val LOGIN = "login"

    const val AUTH = "auth"
    const val FULL_NAME = "full_name"
    const val PHONE = "phone"
    const val EMAIL = "email"
    const val IMAGE = "image"
    const val FINGER_PRINT = "finger_print"
    const val LANGUAGE = "language"

}