package com.example.newbankingproject.ui.deshboard.ui.profile

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.newbankingproject.databinding.FragmentProfileBinding
import com.example.newbankingproject.listener.AlertDialogInterface
import com.example.newbankingproject.ui.deshboard.MainActivity
import com.example.newbankingproject.ui.deshboard.viewModel.MainViewModel
import com.example.newbankingproject.ui.login.LoginActivity
import com.example.newbankingproject.util.Constant
import com.example.newbankingproject.util.Constant.BIOMETRIC_UNAVAILABLE
import com.example.newbankingproject.util.Dialogs
import com.example.newbankingproject.util.Utility
import com.example.newbankingproject.util.Utility.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

/**ProfileFragment is fragment class which elaborate the profile*/
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var preference: KeyStorePreference

    private var _binding: FragmentProfileBinding? = null

    private val viewModel: MainViewModel
        get() = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    private val binding get() = _binding!!

    var imageUri: Uri? = null

    /** getContent is use to return registerForActivityResult */
    private val getContent =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
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
        binding.progress setVisibility true
        viewModel.getProfileApi()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setOnClickListener()
        callObserver()
    }

    /**callObserver is used to call the observers*/
    private fun callObserver() {
        viewModel._profileData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress setVisibility false
                    it.message toastMessage context
                }

                is Resource.Success -> {
                    if (it.data != null) setRemoteData(it.data)
                    binding.progress setVisibility false
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

    /**setRemoteData is used to set the data which come from remote*/
    private fun setRemoteData(data: ProfileResponseModel?) {
        binding.etFullName.setText(data?.data?.name ?: preference.getUserName() ?: "")
        binding.etEmail.setText(data?.data?.email ?: preference.getEmail() ?: "")
        binding.etPhoneNumber.setText(data?.data?.phone ?: preference.getPhoneNumber() ?: "")
        if (preference.getUserName() == null) preference.storeFirstName(data?.data?.name)
        if (preference.getEmail() == null) preference.storeEmail(data?.data?.email)
        if (preference.getPhoneNumber() == null) preference.storePhone(data?.data?.phone)
    }

    /**setData is used to set the data from shared preference*/
    private fun setData() {
        if (preference.getProfileImage() != null)
            binding.ivProfileImage.setImageURI(Uri.parse(preference.getProfileImage()))
        binding.scLanguage.isChecked = preference.getLanguage() == "en"
        binding.scFingerPrint.isChecked = preference.isFingerPrintEnable()
    }

    /**setOnClickListener is used to setup click listeners*/
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
        binding.scLanguage.setOnCheckedChangeListener { _, _ ->
            showChangeLanguageAlert(requireActivity())
        }
        binding.scFingerPrint.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(
                context,
                if (isChecked) Constant.FINGER_PRINT_ENABLE else Constant.FINGER_PRINT_DISABLE,
                Toast.LENGTH_SHORT
            ).show()
            checkBiometric(isChecked)
        }

    }

    private fun showChangeLanguageAlert(activity: Activity) {
        Dialogs.showCustomAlert(
            activity = activity,
            title = activity.getString(R.string.select_alert_title_lang),
            msg = activity.resources.getString(R.string.select_alert_language),
            yesBtn = activity.resources.getString(R.string.eng_lang),
            noBtn = activity.resources.getString(R.string.arabic_lang),
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    changeLanguage(Constant.ENGLISH_LANG)
                }

                override fun onNoClick() {
                    changeLanguage(Constant.ARABIC_LANG)
                }
            })
    }

    /**changeLanguage is used to change the language*/
    fun changeLanguage(language: String) {
        val locale: Locale?
        locale = Locale(language)
        preference.storeLanguage(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity?.resources?.updateConfiguration(config, null)
        refreshActivity()
    }

    private fun refreshActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    /**checkBiometric is used to check the biometric functionality
     * - is it support your device or not
     * */
    private fun checkBiometric(isChecked: Boolean) {
        if (Utility.isBiometricAvailable(context = requireContext())) {
            preference.storeFingerPrint(isChecked)
        } else {
            binding.scFingerPrint.isChecked = false
            binding.scFingerPrint.isEnabled = false
            Toast.makeText(context, BIOMETRIC_UNAVAILABLE, Toast.LENGTH_SHORT).show()
        }
    }

    /**clearPreferenceAndMoveToLogin is used clear the shared preference and move to login page*/
    private fun clearPreferenceAndMoveToLogin() {
        preference.getClear()
        Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }.apply { startActivity(this) }
        activity?.finish()
    }

    /**saveDataInPreference is used to save data inside the shared preference*/
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

    /**choosePhotoFromGallery is used to choose the photo from gallery*/
    private fun choosePhotoFromGallery() {
        getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
    }
}