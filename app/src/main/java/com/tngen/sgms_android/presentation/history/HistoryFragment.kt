package com.tngen.sgms_android.presentation.history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.databinding.FragmentHistoryBinding
import com.tngen.sgms_android.model.history.HistoryModel
import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseFragment
import com.tngen.sgms_android.presentation.home.HomeState
import com.tngen.sgms_android.presentation.main.MainViewModel
import com.tngen.sgms_android.presentation.settings.SettingsViewModel
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import com.tngen.sgms_android.widget.adapter.ModelRecyclerAdapter
import com.tngen.sgms_android.widget.adapter.listener.history.HistoryItemClickListener
import com.tngen.sgms_android.widget.adapter.listener.settings.EmergencyCallItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment(
    private val loadingDialog: LoadingDialog
) : BaseFragment<HistoryViewModel, FragmentHistoryBinding>() {

//    override val viewModel by viewModel<HistoryViewModel>()
    override val viewModel : HistoryViewModel by viewModels()

    override fun getViewBinding(): FragmentHistoryBinding = FragmentHistoryBinding.inflate(layoutInflater)

    private val historyAdapter by lazy {
        ModelRecyclerAdapter<HistoryModel, HistoryViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : HistoryItemClickListener {
                override fun onClickItem(model: HistoryModel) {

                }

            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.historySharedFlow.collect { historyState ->
                handleEvent(historyState)
            }
//            }
        }

    }

    fun handleEvent(historyState: HistoryState) {
        when (historyState) {
            is HistoryState.UnInitialized -> {
                initViews()
            }
            is HistoryState.Loading -> {
                handleLoadingState()
            }
            is HistoryState.GetHistoryListSuccess -> {
                handleSensorListSuccessState(historyState)
            }

            is HistoryState.Error -> {
                handleErrorState()
            }
        }

    }

    override fun initViews() = with(binding){
        recyclerView.adapter = historyAdapter
        super.initViews()
    }

    private fun handleLoadingState() {
        TODO("Not yet implemented")
    }

    private fun handleSensorListSuccessState(it: HistoryState.GetHistoryListSuccess) = with(binding) {
        Log.d("HistoryFragment", "handleSensorListSuccessState")
        historyAdapter.submitList(it.historyList)
        val smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = it.historyList.size
//        smoothScroller.targetPosition = 0
        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun handleErrorState() {

    }

    companion object {
        const val TAG = "HistoryFragment"
    }
    fun getSerialData(serialEntity: SerialEntity) {
        Log.d("HistoryFragment", "serialEntity 수신함")
        if(Preferences.baselineEntity != null) {
            if (serialEntity.o2 < Preferences.baselineEntity!!.o2 ||
                serialEntity.co2 > Preferences.baselineEntity!!.co2 ||
                serialEntity.co > Preferences.baselineEntity!!.co ||
                serialEntity.h2s > Preferences.baselineEntity!!.h2s ||
                serialEntity.lel > Preferences.baselineEntity!!.lel
            ) {
                Log.d("HistoryFragment", "fetchDAta")
                viewModel.fetchData()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){
            viewModel.fetchData()
        }
        super.onHiddenChanged(hidden)
    }
}