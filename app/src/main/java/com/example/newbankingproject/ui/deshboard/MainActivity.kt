package com.example.newbankingproject.ui.deshboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.data.utils.KeyStorePreference
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.ActivityMainBinding
import com.example.newbankingproject.ui.deshboard.viewModel.MainViewModel
import com.example.newbankingproject.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**MainActivity is class used to elaborate the profile and profile*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preference: KeyStorePreference

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (preference.getAuth() == null) {
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.apply { startActivity(this) }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val navView = binding.navView
        navView.setupWithNavController(navController)
    }
}