package com.example.newbankingproject.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.FragmentLoginBinding
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.login.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

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
        setOnClickListener()
        setObservable()
    }


    private fun validation(phone: String, password: String): Boolean {
        binding.progress.setVisibility(false)
        if (phone.isEmpty()) {
            getString(R.string.code_error).toastMessage()
            return false
        }
        if (password.isEmpty()) {
            getString(R.string.phone_error).toastMessage()
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

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun setObservable() {
        viewModel._loginData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress.setVisibility(false)
                    if (it.message?.contains("Unauthorized", true) == true) {
                        context?.getString(R.string.invalid)?.toastMessage()
                    } else {
                        it.message?.toastMessage()
                    }
                }

                is Resource.Success -> {
                    binding.progress.setVisibility(false)
                    if (it.data?.accessToken != null)
                        Intent(context, MainActivity::class.java).apply {

                        }.apply { startActivity(this) }
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

    private fun String.toastMessage() {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG = LoginScreenFragment::class.java.simpleName
    }
}