package com.example.newbankingproject.ui.deshboard.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.newbankingproject.util.Utility.Companion.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var preference: KeyStorePreference

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    lateinit var adapter: DashboardMainAdapter
    private val dashBoardData = ObservableArrayList<DashboardData>()

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
        setOnClickListener()
        callObserver()
    }

    private fun setViewData() {
        binding.tvFullName.text = preference.getUserName() ?: context?.getString(R.string.guest)
        adapter = DashboardMainAdapter()
        binding.rvDailyNeeds.adapter = adapter
        binding.rvDailyNeeds.setHasFixedSize(true)
        binding.rvDailyNeeds.layoutManager = LinearLayoutManager(context)
        if (preference.getProfileImage() != null)
            binding.ivImage.setImageURI(Uri.parse(preference.getProfileImage()))
    }

    private fun setOnClickListener() {

    }

    private fun callObserver() {
        viewModel._dashBoardData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    it.message?.toastMessage(context)
                }

                is Resource.Success -> {
                    if (it.data != null) {
                        setData(it.data)
                    }
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun setData(data: DashboardResponseModel?) {
        dashBoardData.clear()
        data?.data?.let { dashBoardData.addAll(it) }
        Log.d(TAG, "setData: $dashBoardData")
        adapter.updateData(dashBoardData)
        adapter.notifyDataSetChanged()
    }

    private fun View.setVisibility(isVisible: Boolean = false) {
        this.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }
}