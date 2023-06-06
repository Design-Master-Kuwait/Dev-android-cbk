package com.example.newbankingproject.ui.deshboard.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.utils.KeyStorePreference
import com.example.domain.model.dashboard.DashboardData
import com.example.domain.model.dashboard.DashboardResponseModel
import com.example.domain.utils.Resource
import com.example.newbankingproject.R
import com.example.newbankingproject.databinding.FragmentHomeBinding
import com.example.newbankingproject.ui.deshboard.adapter.DashboardMainAdapter
import com.example.newbankingproject.ui.deshboard.viewModel.MainViewModel
import com.example.newbankingproject.util.Utility.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**DashBoardFragment is used to elaborate the home screen preview*/
@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    @Inject
    lateinit var preference: KeyStorePreference

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    lateinit var adapter: DashboardMainAdapter
    private val dashBoardData = ObservableArrayList<DashboardData?>()

    private val viewModel: MainViewModel
        get() = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.getDashBoardApi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewData()
        callObserver()
    }

    /**setViewData is used to set data in view*/
    private fun setViewData() {
        binding.tvFullName.text = preference.getUserName() ?: context?.getString(R.string.guest)
        if (preference.getProfileImage() != null)
            binding.ivImage.setImageURI(Uri.parse(preference.getProfileImage()))
    }

    /**callObserver is used to call observe for retrieve data*/
    private fun callObserver() {
        viewModel._dashBoardData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.progress setVisibility false
                    it.message toastMessage context
                }

                is Resource.Success -> {
                    binding.progress setVisibility false
                    setData(it.data)

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

    /**setData is used to set the data which come from remote api*/
    private fun setData(data: DashboardResponseModel?) {
        dashBoardData.clear()
        data?.data?.forEach { dashBoardData.add(it) }
        adapter = DashboardMainAdapter(dashBoardData)
        binding.rvDailyNeeds.adapter = adapter
        binding.rvDailyNeeds.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = DashBoardFragment::class.java.simpleName
    }

}