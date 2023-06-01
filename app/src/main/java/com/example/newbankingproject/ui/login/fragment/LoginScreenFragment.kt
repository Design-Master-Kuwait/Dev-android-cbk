package com.example.newbankingproject.ui.login.fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.data.utils.KeyStorePreference
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.FragmentLoginBinding
import com.example.newbankingproject.listener.AlertDialogInterface
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.login.LoginActivity
import com.example.newbankingproject.ui.login.viewModel.LoginViewModel
import com.example.newbankingproject.util.Dialogs
import com.example.newbankingproject.util.Utility.Companion.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

    @Inject
    lateinit var keyStorePreference: KeyStorePreference

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!
    private val viewModel: LoginViewModel
        get() = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        setOnClickListener()
        setObservable()
    }

    /**initializeView is used to initialize the view*/
    private fun initializeView() {
        binding.tvLanguage.text = keyStorePreference.getLanguage()
    }


    /** validation is used to validate the phone and password field*/
    private fun validation(phone: String, password: String): Boolean {
        binding.progress.setVisibility(false)
        if (phone.isEmpty()) {
            getString(R.string.code_error).toastMessage(context)
            return false
        }
        if (password.isEmpty()) {
            getString(R.string.phone_error).toastMessage(context)
            return false
        }
        return true
    }

    /**setOnClickListener is used to call setOnCLickListeners for views*/
    private fun setOnClickListener() {
        binding.btnContinue.setOnClickListener {
            binding.progress.setVisibility(true)
            val phone = binding.etPhoneNumber.text.toString()
            val password = binding.etPassword.text.toString()
            if (validation(phone, password)) {
                viewModel.postAuthApi(phone, password)
            }
        }

        binding.tvLanguage.setOnClickListener {
            showChangeLanguageAlert()
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    /**showChangeLanguageAlert is used to show alert box for change the language*/
    private fun showChangeLanguageAlert() {
        Dialogs.showCustomAlert(
            activity = requireActivity(),
            title = getString(R.string.select_alert_title_lang),
            msg = resources.getString(R.string.select_alert_language),
            yesBtn = resources.getString(R.string.eng_lang),
            noBtn = resources.getString(R.string.arabic_lang),
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    changeLanguage("en")
                }

                override fun onNoClick() {
                    changeLanguage("ar")
                }
            })
    }


    /**changeLanguage is used to change the language*/
    fun changeLanguage(language: String) {
        var locale: Locale? = null
        if (language == "en") {
            locale = Locale("en")
            keyStorePreference.storeLanguage("en")
        } else if (language.equals("ar")) {
            locale = Locale("ar")
            keyStorePreference.storeLanguage("ar")
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity?.resources?.updateConfiguration(config, null)
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    /**setObservable is used to set the observer*/
    private fun setObservable() {
        viewModel._loginData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress setVisibility false
                    if (it.message?.contains("Unauthorized", true) == true) {
                        context?.getString(R.string.invalid) toastMessage context
                    } else {
                        it.message toastMessage context
                    }
                }

                is Resource.Success -> {
                    binding.progress.setVisibility(false)
                    if (it.data?.accessToken != null) {
                        keyStorePreference.storeAuth(it.data!!.accessToken)
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }.apply { startActivity(this) }
                        activity?.finish()
                    }
                }

                is Resource.Loading -> {
                    binding.progress setVisibility true
                }
            }
        }
    }

    private infix fun View.setVisibility(isVisible: Boolean) {
        this.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG = LoginScreenFragment::class.java.simpleName
    }
}