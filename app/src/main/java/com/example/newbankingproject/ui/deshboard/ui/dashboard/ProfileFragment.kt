package com.example.newbankingproject.ui.deshboard.ui.dashboard

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.data.utils.KeyStorePreference
import com.example.domain.model.dashboard.ProfileResponseModel
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.DialogImageBinding
import com.example.newbankingproject.databinding.FragmentProfileBinding
import com.example.newbankingproject.listener.AlertDialogInterface
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.deshboard.viewModel.MainViewModel
import com.example.newbankingproject.ui.login.LoginActivity
import com.example.newbankingproject.util.Dialogs
import com.example.newbankingproject.util.Utility
import com.example.newbankingproject.util.Utility.Companion.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var preference: KeyStorePreference

    private var _binding: FragmentProfileBinding? = null

    private val viewModel: MainViewModel
        get() = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    private val binding get() = _binding!!

    var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            // Handle the returned Uri

            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (uri != null) {
                context?.contentResolver?.takePersistableUriPermission(uri, flag)
            }
            imageUri = uri
            binding.ivProfileImage.setImageURI(uri)
            preference.storeProfileImage(uri.toString())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getProfileApi()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setOnClickListener()
        callObserver()
    }

    private fun callObserver() {
        viewModel._profileData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    it.message?.toastMessage(context)
                }

                is Resource.Success -> {
                    if (it.data != null) {
                        setRemoteData(it.data)
                    }
                }

                is Resource.Loading -> {
                }
            }
        }
    }

    private fun setRemoteData(data: ProfileResponseModel?) {
        Log.d(TAG, "setRemoteData: $data")

    }

    private fun setData() {
        binding.etFullName.setText(preference.getUserName() ?: "")
        binding.etEmail.setText(preference.getEmail() ?: "")
        binding.etPhoneNumber.setText(preference.getPhoneNumber() ?: "")
        if (preference.getProfileImage() != null) {
            binding.ivProfileImage.setImageURI(Uri.parse(preference.getProfileImage()))
        }
        binding.scLanguage.isChecked = preference.getLanguage() == "en"
        binding.scFingerPrint.isChecked = preference.isFingerPrintEnable()
    }

    private fun setOnClickListener() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        binding.btnContinue.setOnClickListener {
            saveDataInPreference()
        }
        binding.tvLogout.setOnClickListener {
            clearPreferenceAndMoveToLogin()

        }
        binding.ivSelectImage.setOnClickListener {
            choosePhotoFromGallery()
        }
        binding.scLanguage.setOnCheckedChangeListener { compoundButton, isChecked ->
            showChangeLanguageAlert()
        }
        binding.scFingerPrint.setOnCheckedChangeListener { compoundButton, isChecked ->
            checkBiometric(isChecked)
        }

    }

    private fun checkBiometric(isChecked: Boolean) {
        if (Utility.isBiometricAvailable(context = requireContext())) {
            preference.storeFingerPrint(isChecked)
        } else {
            Toast.makeText(context, "Biometric authentication is not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearPreferenceAndMoveToLogin() {
        preference.getClear()
        Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }.apply { startActivity(this) }
        activity?.finish()
    }

    private fun saveDataInPreference() {
        val name = binding.etFullName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhoneNumber.text.toString()
        preference.storePhone(phone)
        preference.storeEmail(email)
        preference.storeFirstName(name)
        if (imageUri != null)
            preference.storeProfileImage(imageUri.toString())
        Toast.makeText(context, context?.getString(R.string.save_data), Toast.LENGTH_SHORT)
            .show()
    }

    private fun showImageDialog() {
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(true)
        }
        val binding = DialogImageBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(binding.root)
        binding.cvCamera.setOnClickListener {
            dialog.dismiss()
        }
        binding.cvGallery.setOnClickListener {
            dialog.dismiss()
        }
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun choosePhotoFromGallery() {
        getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showChangeLanguageAlert() {
        Dialogs.showCustomAlert(
            activity = requireActivity(),
            title = getString(R.string.alert_title_lang),
            msg = resources.getString(R.string.alert_language),
            yesBtn = resources.getString(R.string.yes_lang),
            noBtn = resources.getString(R.string.no_lang),
            reverseFont = true,
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    changeLanguage()
                    Intent(context, MainActivity::class.java).apply { startActivity(this) }
                }

                override fun onNoClick() {
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun changeLanguage() {
        if (preference.getLanguage() == "en") {
            arabicLanguage()
        } else {
            englishLanguage()
        }
    }

    private fun arabicLanguage() {
        preference.storeLanguage("ar")
        setLocale()
    }

    private fun englishLanguage() {
        preference.storeLanguage("en")
        setLocale()
    }

    private fun setLocale() {
        val locale: Locale = resources.configuration.locale

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        conf.setLocales(localeList)

        conf.setLayoutDirection(locale)
        res.updateConfiguration(conf, dm)
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        const val GALLERY_CODE = 1001
        val PHOTO_CODE = 1000
    }
}