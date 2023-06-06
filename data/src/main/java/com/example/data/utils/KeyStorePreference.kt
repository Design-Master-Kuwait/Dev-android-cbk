package com.example.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.data.utils.Constant.AUTH
import com.example.data.utils.Constant.EMAIL
import com.example.data.utils.Constant.FINGER_PRINT
import com.example.data.utils.Constant.FULL_NAME
import com.example.data.utils.Constant.IMAGE
import com.example.data.utils.Constant.LANGUAGE
import com.example.data.utils.Constant.PHONE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**KeyStorePreference is main class for key store shared preference*/
class KeyStorePreference @Inject constructor(
    @ApplicationContext val context: Context,
    val preferences: SharedPreferences,
) {

    var edit: SharedPreferences.Editor = preferences.edit()

    fun storeAuth(value: String?) {
        edit.putString(AUTH, value)
        edit.commit()
    }

    fun storeFirstName(value: String?) {
        edit.putString(FULL_NAME, value)
        edit.commit()
    }

    fun storePhone(value: String?) {
        edit.putString(PHONE, value)
        edit.commit()
    }

    fun storeEmail(value: String?) {
        edit.putString(EMAIL, value)
        edit.commit()
    }

    fun storeProfileImage(value: String?) {
        edit.putString(IMAGE, value)
        edit.commit()
    }

    fun storeLanguage(isEnglish: String = "en") {
        edit.putString(LANGUAGE, isEnglish)
        edit.commit()
    }

    fun storeFingerPrint(isEnable: Boolean) {
        edit.putBoolean(FINGER_PRINT, isEnable)
        edit.commit()
    }

    fun getClear() {
        edit.clear().commit()
    }

    fun getAuth() = preferences.getString(AUTH, null)

    fun getUserName() = preferences.getString(FULL_NAME, null)

    fun getPhoneNumber() = preferences.getString(PHONE, null)

    fun isLogin() = getAuth() != null

    fun getEmail() = preferences.getString(EMAIL, null)

    fun getProfileImage() = preferences.getString(IMAGE, null)

    fun getLanguage() = preferences.getString(LANGUAGE, "en")

    fun isFingerPrintEnable() = preferences.getBoolean(FINGER_PRINT, false)

}