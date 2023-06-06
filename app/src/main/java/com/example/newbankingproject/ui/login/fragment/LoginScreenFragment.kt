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
import com.example.newbankingproject.util.Constant.ARABIC_LANG
import com.example.newbankingproject.util.Constant.ENGLISH_LANG
import com.example.newbankingproject.util.Dialogs
import com.example.newbankingproject.util.Utility.toastMessage
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
    fun validation(phone: String, password: String): Boolean {
        if (phone.isEmpty()) {
            return false
        }
        if (password.isEmpty()) {
            return false
        }
        return true
    }

    /**setOnClickListener is used to call setOnCLickListeners for views*/
    private fun setOnClickListener() {
        binding.btnContinue.setOnClickListener {

            val phone = binding.etPhoneNumber.text.toString()
            val password = binding.etPassword.text.toString()
            if (validation(phone, password)) {
                binding.progress setVisibility true
                viewModel.postAuthApi(phone, password)
            } else {
                getString(R.string.empty_field) toastMessage context
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
                    changeLanguage(ENGLISH_LANG)
                }

                override fun onNoClick() {
                    changeLanguage(ARABIC_LANG)
                }
            })
    }


    /**changeLanguage is used to change the language*/
    fun changeLanguage(language: String) {
        var locale: Locale? = null
        if (language == ENGLISH_LANG) {
            locale = Locale(ENGLISH_LANG)
            keyStorePreference.storeLanguage(ENGLISH_LANG)
        } else if (language.equals(ENGLISH_LANG)) {
            locale = Locale(ARABIC_LANG)
            keyStorePreference.storeLanguage(ARABIC_LANG)
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