package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.data.utils.Constant
import com.example.data.utils.KeyStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
/**KeyStoreModule class is module for key store preference*/
class KeyStoreModule {

    @Provides
    @Singleton
    fun provideEncryptionSharedPreference(
        masterKey: MasterKey,
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            Constant.KEY_STORE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideMasterKey(
        @ApplicationContext context: Context,
    ): MasterKey {
        return MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    @Provides
    @Singleton
    fun providePreference(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
    ): KeyStorePreference {
        return KeyStorePreference(context, preferences = sharedPreferences)
    }

}