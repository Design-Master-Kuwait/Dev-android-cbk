package com.example.newbankingproject.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.os.LocaleList
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

    private fun initializeView() {
        binding.tvLanguage.text = keyStorePreference.getLanguage()
    }


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

    private fun showChangeLanguageAlert() {
        Dialogs.showCustomAlert(
            activity = requireActivity(),
            title = getString(R.string.select_alert_title_lang),
            msg = resources.getString(R.string.select_alert_language),
            yesBtn = resources.getString(R.string.eng_lang),
            noBtn = resources.getString(R.string.arabic_lang),
            reverseFont = true,
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    englishLanguage()
                }

                override fun onNoClick() {
                    arabicLanguage()
                }
            })
    }

    fun changeLanguage() {
        if (keyStorePreference.getLanguage() == "en") {
            arabicLanguage()
        } else {
            englishLanguage()
        }
    }

    private fun arabicLanguage() {
        keyStorePreference.storeLanguage("ar")
        setLocale()
    }

    private fun englishLanguage() {
        keyStorePreference.storeLanguage("en")
        setLocale()
    }

    private fun setLocale(removeAllPrevActivities: Boolean = true) {
        val locale: Locale = resources.configuration.locale

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        conf.setLocales(localeList)

        conf.setLayoutDirection(locale)
        res.updateConfiguration(conf, dm)
        if (removeAllPrevActivities) {
            activity?.finishAffinity()
        }
        Intent(context, LoginActivity::class.java).apply { startActivity(this) }
        activity?.finish()
    }

    private fun setObservable() {
        viewModel._loginData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress.setVisibility(false)
                    if (it.message?.contains("Unauthorized", true) == true) {
                        context?.getString(R.string.invalid)?.toastMessage(context)
                    } else {
                        it.message?.toastMessage(context)
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
                    binding.progress.setVisibility(true)
                }
            }
        }
    }

    private fun View.setVisibility(isVisible: Boolean = false) {
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