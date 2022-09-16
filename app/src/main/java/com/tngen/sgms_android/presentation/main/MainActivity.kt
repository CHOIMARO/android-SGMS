package com.tngen.sgms_android.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.tngen.sgms_android.R
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.databinding.ActivityMainBinding
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseActivity
import com.tngen.sgms_android.presentation.history.HistoryFragment
import com.tngen.sgms_android.presentation.home.HomeFragment
import com.tngen.sgms_android.presentation.settings.SettingsFragment
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    NavigationBarView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        SgmsApplication.resumed = true
    }

    override fun onPause() {
        super.onPause()
        SgmsApplication.resumed = false
    }


    private var serialEntity: SerialEntity? = null

//    override val viewModel by viewModel<MainViewModel>()
    override val viewModel : MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            is MainState.UnInitialized -> {
                initViews()
            }
            is MainState.Loading -> {
                handleLoadingState()
            }
            is MainState.ReceivedSerialDataSuccess -> {
                handleReceivedSerialDataSuccessState(it)
            }
            is MainState.InsertHistoryItemSuccess -> {
                handleInsertHistoryItemSuccessState(it)
            }
            is MainState.ReceivedSerialCalibrationSettingSuccess -> {
                handleReceivedSerialCalibrationSettingSuccessState(it)
            }
            is MainState.ReceivedSerialBaselineSettingSuccess -> {
                handleReceivedSerialBaselineSettingSuccessState(it)
            }

            is MainState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun handleReceivedSerialDataSuccessState(it: MainState.ReceivedSerialDataSuccess) {
        //시리얼 수신 완료
        Log.d("MainActivity", "시리얼 수신 완료요")
        serialEntity = it.data
        if(it.data.cmd.equals('c')) {
            Preferences.showToast(this, "캘리브레이션 설정 후 첫 수신 데이터입니다.\r\n센서 ID : ${it.data.sensorId}")
        }else if (it.data.cmd.equals('a')) {
            Preferences.showToast(this, "기준치 알람 설정 후 첫 수신 데이터입니다.\r\n센서 ID : ${it.data.sensorId}")
        }
        if (Preferences.baselineEntity != null) {
            if (it.data.o2 < Preferences.baselineEntity!!.o2 ||
                it.data.co2 > Preferences.baselineEntity!!.co2 ||
                it.data.co > Preferences.baselineEntity!!.co ||
                it.data.h2s > Preferences.baselineEntity!!.h2s ||
                it.data.lel > Preferences.baselineEntity!!.lel
            ) {
                Preferences.showToast(this, "기준치를 이탈한 값이 발생했습니다.")

                if (SgmsApplication.prefs.getString("alarmSettingSwitch", "").equals("on")) {
                    val uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val ringtone = RingtoneManager.getRingtone(this, uriRingtone)
                    ringtone.play()

                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    val vibratorEffect = VibrationEffect.createOneShot(2000, 100)
                    vibrator.vibrate(vibratorEffect)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.insertHistoryItem(it.data)
                }
            }else{ //정상
                val findHomeFragment =
                    supportFragmentManager.findFragmentByTag("HomeFragment")?.let {
                        it as HomeFragment
                    }
                serialEntity?.let { it -> findHomeFragment?.getSerialData(it) }

                val findHistoryFragment =
                    supportFragmentManager.findFragmentByTag("HistoryFragment")?.let {
                        it as HistoryFragment
                    }
                serialEntity?.let { it -> findHistoryFragment?.getSerialData(it) }
            }
        } else {
            Preferences.showToast(this, "센서로부터 수신된 값이 있으나 설정된 기준치가 없습니다.")
        }
    }
    private fun handleReceivedSerialCalibrationSettingSuccessState(it: MainState.ReceivedSerialCalibrationSettingSuccess) {
        val findHomeFragment =
            supportFragmentManager.findFragmentByTag("HomeFragment")?.let {
                it as HomeFragment
            }
        findHomeFragment?.closeLoadingDialog()
    }
    private fun handleReceivedSerialBaselineSettingSuccessState(it: MainState.ReceivedSerialBaselineSettingSuccess) {
        val findSettingFragment =
            supportFragmentManager.findFragmentByTag("SettingsFragment")?.let {
                it as SettingsFragment
            }
        findSettingFragment?.closeLoadingDialog()
    }
    private fun handleInsertHistoryItemSuccessState(it: MainState.InsertHistoryItemSuccess) {
        val findHomeFragment =
            supportFragmentManager.findFragmentByTag("HomeFragment")?.let {
                it as HomeFragment
            }
        serialEntity?.let { it -> findHomeFragment?.getSerialData(it) }

        val findHistoryFragment =
            supportFragmentManager.findFragmentByTag("HistoryFragment")?.let {
                it as HistoryFragment
            }
        serialEntity?.let { it -> findHistoryFragment?.getSerialData(it) }
    }

    private fun handleErrorState() {

    }

    private fun handleLoadingState() {


    }

    override fun initViews(): Unit = with(binding) {
        super.initViews()
        bottomNav.setOnItemSelectedListener(this@MainActivity)
        bottomNav.selectedItemId = R.id.menu_home
//        showFragment(HomeFragment(), HomeFragment.TAG)

    }


    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val loadingDialog : LoadingDialog = LoadingDialog(this)
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment(loadingDialog), HomeFragment.TAG)
                true
            }
            R.id.menu_history -> {
                showFragment(HistoryFragment(loadingDialog), HistoryFragment.TAG)
                true
            }
            R.id.menu_settings -> {
                showFragment(SettingsFragment(loadingDialog), SettingsFragment.TAG)
                true
            }
            else -> false
        }
    }
}
