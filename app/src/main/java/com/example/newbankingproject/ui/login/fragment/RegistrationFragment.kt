package com.example.newbankingproject.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.data.utils.KeyStorePreference
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.FragmentRegisterBinding
import com.example.newbankingproject.ui.login.viewModel.LoginViewModel
import com.example.newbankingproject.util.Constant
import com.example.newbankingproject.util.Utility.Companion.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*RegistrationFragment is used to elaborate the registration page screen*/
@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    @Inject
    lateinit var preference: KeyStorePreference

    private val binding get() = _binding!!

    private val viewModel: LoginViewModel
        get() = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservable()
        setOnClickListener()
    }

    /** setOnClickListener is used to call set on click listeners for views
     */
    private fun setOnClickListener() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        binding.btnContinue.setOnClickListener {
            binding.progress.setVisibility(true)
            val phone = binding.etPhoneNumber.text.toString()
            val name = binding.etFullName.text.toString()
            val password = binding.etPassword.text.toString()
            if (validation(name, phone, password)) {
                preference.storeFirstName(name)
                preference.storePhone(phone)
                viewModel.postRegisterApi(name, phone, password)
            }
        }
    }

    /**setObservable is used to call observer*/
    private fun setObservable() {
        viewModel._registerData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress.setVisibility(false)
                    it.message?.toastMessage(context)
                }

                is Resource.Success -> {
                    binding.progress.setVisibility(false)

                    if (it.data?.success != null) {
                        setData(it.data?.success)
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

    /**setData is used to set data which come from api*/
    private fun setData(success: String?) {
        when (success) {
            Constant.NEW_USER_CREATED -> {
                success.toastMessage(context)
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }

            Constant.USERNAME_ALREADY_TAKEN -> {
                success.toastMessage(context)
            }
        }
    }


    /**validation is used to validate the fields*/
    private fun validation(name: String, phone: String, password: String): Boolean {
        if (phone.isEmpty()) {
            getString(R.string.code_error).toastMessage(context)
            return false
        }
        if (name.isEmpty()) {
            getString(R.string.code_error).toastMessage(context)
            return false
        }
        if (password.isEmpty()) {
            getString(R.string.phone_error).toastMessage(context)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}