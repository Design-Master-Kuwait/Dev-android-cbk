package com.example.newbankingproject.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.data.utils.KeyStorePreference
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**LoginActivity is class for elaborate login screen*/
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var preference: KeyStorePreference
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_login) as NavHostFragment
        navHostFragment.navController
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp()
    }
}