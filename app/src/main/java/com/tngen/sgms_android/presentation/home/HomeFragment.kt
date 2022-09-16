package com.tngen.sgms_android.presentation.home

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.tngen.sgms_android.BuildConfig
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.databinding.FragmentHomeBinding
import com.tngen.sgms_android.model.home.HomeSensorModel
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseFragment
import com.tngen.sgms_android.presentation.main.MainViewModel
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import com.tngen.sgms_android.widget.adapter.listview.HomeSensorListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor

@AndroidEntryPoint
class HomeFragment(
    private val loadingDialog: LoadingDialog
): BaseFragment<HomeViewModel, FragmentHomeBinding>() {

//    override val viewModel by viewModel<HomeViewModel>()
    override val viewModel : HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    var homeSensorListAdapter = activity?.let { HomeSensorListAdapter(mutableListOf<HomeSensorModel>(), loadingDialog) }
//    private val homeSensorListAdapter by lazy {
//        ModelRecyclerAdapter<HomeSensorModel, HomeViewModel>(
//            listOf(),
//            viewModel,
//            adapterListener = object : HomeSensorItemClickListener {
//                override fun onClickItem(model: HomeSensorModel) {
//
//                }
//
//            },
//        )
//    }

    override fun initViews()= with(binding) {
//        recyclerView.adapter = homeSensorListAdapter
        super.initViews()
    }

    override fun observeData() = viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is HomeState.UnInitialized -> {
                initViews()
            }
            is HomeState.Loading -> {
                handleLoadingState()
            }
            is HomeState.GetSensorListSuccess -> {
                handleSensorListSuccessState(it)
            }
            is HomeState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun handleLoadingState() {

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleSensorListSuccessState(it: HomeState.GetSensorListSuccess) = with(binding) {
        homeSensorListAdapter = HomeSensorListAdapter(it.sensorList, loadingDialog)
        listView.adapter = homeSensorListAdapter

        GlobalScope.launch(Dispatchers.Main) {
            homeSensorListAdapter!!.sharedFlowScroll.collect() { position ->
                listView.smoothScrollToPosition(position)
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            homeSensorListAdapter!!.sharedFlowCalibration.collect() { serialEntity ->
                viewModel.settingCalibration(serialEntity)
            }
        }

//        homeSensorListAdapter.submitList(it.sensorList)
    }

    private fun handleErrorState() {

    }

    fun getSerialData(serialEntity: SerialEntity) {
        Log.d("HomeFragment", "serialEntity 수신함")
        CoroutineScope(Dispatchers.Main).launch {
            homeSensorListAdapter?.receivedSerialData(serialEntity)
        }
    }
    fun closeLoadingDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            loadingDialog.dismiss()
            Preferences.showToast(context, "캘리브레이션 설정 메세지를 송신했습니다.")
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden) {
            viewModel.checkDataSetChange()
        }
    }
    companion object {
        const val TAG = "HomeFragment"
    }
}