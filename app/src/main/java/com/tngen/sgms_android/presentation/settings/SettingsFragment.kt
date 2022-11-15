package com.tngen.sgms_android.presentation.settings

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.databinding.FragmentSettingsBinding
import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.model.settings.SettingsSensorModel
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseFragment
import com.tngen.sgms_android.presentation.main.MainViewModel
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import com.tngen.sgms_android.widget.adapter.ModelRecyclerAdapter
import com.tngen.sgms_android.widget.adapter.listener.settings.EmergencyCallItemClickListener
import com.tngen.sgms_android.widget.adapter.listener.settings.SensorItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.cachapa.expandablelayout.ExpandableLayout

@AndroidEntryPoint
class SettingsFragment(
    private val loadingDialog: LoadingDialog
) : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

//    override val viewModel by viewModel<SettingsViewModel>()
    override val viewModel : SettingsViewModel by viewModels()

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    private val sensorAdapter by lazy {
        ModelRecyclerAdapter<SettingsSensorModel, SettingsViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : SensorItemClickListener {
                override fun onClickItem(model: SettingsSensorModel) {
//                    Preferences.showToast(requireContext(), "${model.sensorId}")
                    AlertDialog.Builder(requireContext())
                        .setTitle("삭제")
                        .setMessage("센서 ID : ${model.sensorId}, 센서 위치 : ${model.sensorLocation}\r\n해당 센서를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", DialogInterface.OnClickListener { _, _ ->
                            viewModel.removeSensorItem(model.id)
                        })
                        .setNegativeButton("아니오", DialogInterface.OnClickListener { _, _ ->

                        })
                        .show()
                }

            })
    }
    private val emergencyCallAdapter by lazy {
        ModelRecyclerAdapter<SettingsEmergencyCallModel, SettingsViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : EmergencyCallItemClickListener {
                override fun onClickItem(model: SettingsEmergencyCallModel) {

                }

            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.settingSharedFlow.collect { settingState ->
                handleEvent(settingState)
            }
//            }
        }
    }

    private fun handleEvent(settingState: SettingsState) {
        when (settingState) {
            is SettingsState.UnInitialized -> {
                initViews()
            }
            is SettingsState.Loading -> {
                handleLoadingState()
            }
            is SettingsState.GetSensorSuccess -> {
                handleRegistSensorSuccessState(settingState)
            }
            is SettingsState.GetBaselineSuccess -> {
                handleGetBaselineSuccessState(settingState)
            }
            is SettingsState.GetEmergencyCallSuccess -> {
                handleGetEmergencyCallSuccessState(settingState)
            }
            is SettingsState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun handleGetBaselineSuccessState(it: SettingsState.GetBaselineSuccess) {
        with(binding) {
            if (it.baseline != null) {
                o2Value.setText(it.baseline.o2.toString())
                coValue.setText(it.baseline.co.toString())
                lelValue.setText(it.baseline.lel.toString())
                co2Value.setText(it.baseline.co2.toString())
                h2sValue.setText(it.baseline.h2s.toString())
            }
        }
    }

    private fun handleRegistSensorSuccessState(it: SettingsState.GetSensorSuccess) {
        sensorAdapter.submitList(it.sensorList)
    }
    private fun handleGetEmergencyCallSuccessState(it: SettingsState.GetEmergencyCallSuccess) {
        emergencyCallAdapter.submitList(it.emergencyCallList)
    }

    private fun handleErrorState() {

    }

    private fun handleLoadingState() {

    }

    override fun initViews() = with(binding) {
        super.initViews()
        initClickBar()
        initSensorRegistrationButton()
        initEmergencyCallButton()
        initBaselineSaveButton()
        initArrowButton()
        initSwitch()
        sensorRegistrationRecyclerView.adapter = sensorAdapter
        emergencyCallRecyclerView.adapter = emergencyCallAdapter
    }

    private fun initSwitch() = with(binding) {
        if (SgmsApplication.prefs.getString("alarmSettingSwitch", "") == "on") {
            alarmSettingSwitch.isChecked = true
        }else if (SgmsApplication.prefs.getString("alarmSettingSwitch", "") == "") {
            Log.d("SettingFragment", SgmsApplication.prefs.getString("alarmSettingSwitch", ""))
            SgmsApplication.prefs.setString("alarmSettingSwitch", "on")
            alarmSettingSwitch.isChecked = true
        }


        if (SgmsApplication.prefs.getString("emergencyCallSwitch", "") == "on") {
            emergencyCallSwitch.isChecked = true
        }

        alarmSettingSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SgmsApplication.prefs.setString("alarmSettingSwitch", "on")
                Preferences.showToast(requireContext(), "기준치 이탈 시 알람 설정 On")
            } else {
                SgmsApplication.prefs.setString("alarmSettingSwitch", "off")
                Preferences.showToast(requireContext(), "기준치 이탈 시 알람 설정 Off")
            }
        }

        emergencyCallSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SgmsApplication.prefs.setString("emergencyCallSwitch", "on")
                Preferences.showToast(requireContext(), "기준치 이탈 시 메세지 전송 On")
            } else {
                SgmsApplication.prefs.setString("emergencyCallSwitch", "off")
                Preferences.showToast(requireContext(), "기준치 이탈 시 메세지 전송 Off")
            }
        }
    }

    private fun initEmergencyCallButton() = with(binding){
        emergencyCallImageButton.isEnabled = false
        emergencyCallImageButton.setOnClickListener {
            it.hideKeyboard()
            if(emergencyCallNameEditText.text.isEmpty() || emergencyCallNumEditText.text.isEmpty()) {
                Preferences.showToast(requireContext(), "사용자 이름 또는 전화 번호가 비어있습니다.")
                return@setOnClickListener
            }
            viewModel.registrationEmergencyCall(
                emergencyCallNameEditText.text.toString(),
                emergencyCallNumEditText.text.toString()
            )
        }
        emergencyCallNumEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        emergencyCallNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                emergencyCallImageButton.isEnabled =
                    !(emergencyCallNumEditText.text.isEmpty() || emergencyCallNameEditText.text.isEmpty())
            }

        })
        emergencyCallNumEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}


            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                emergencyCallImageButton.isEnabled =
                    !(emergencyCallNumEditText.text.isEmpty() || emergencyCallNameEditText.text.isEmpty())
            }

        })
    }

    private fun initSensorRegistrationButton() = with(binding) {
        sensorRegistrationImageButton.isEnabled = false
        sensorRegistrationImageButton.setOnClickListener {
            it.hideKeyboard()
            if (sensorRegistrationIdEditText.text.isEmpty() || sensorRegistrationLocationEditText.text.isEmpty()) {
                Preferences.showToast(requireContext(), "센서 ID 또는 센서 위치가 비어있습니다.")
                return@setOnClickListener
            }
            if(viewModel.checkDuplicateId(sensorRegistrationIdEditText.text.toString().toLong())) {
                viewModel.registrationSensor(
                    sensorRegistrationIdEditText.text.toString().toLong(),
                    sensorRegistrationLocationEditText.text.toString()
                )
                Preferences.showToast(requireContext(), "센서 ID : ${sensorRegistrationIdEditText.text}, 센서 위치 : ${sensorRegistrationLocationEditText.text} 센서가 등록되었습니다.")
            } else {
                Preferences.showToast(requireContext(), "중복된 센서 ID가 존재합니다.")
            }

        }

        sensorRegistrationIdEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                sensorRegistrationImageButton.isEnabled =
                    !(sensorRegistrationIdEditText.text.isEmpty() || sensorRegistrationLocationEditText.text.isEmpty())
            }

        })
        sensorRegistrationLocationEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}


            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                sensorRegistrationImageButton.isEnabled =
                    !(sensorRegistrationIdEditText.text.isEmpty() || sensorRegistrationLocationEditText.text.isEmpty())
            }

        })
    }

    private fun initBaselineSaveButton() = with(binding) {
        baselineSaveButton.isEnabled = false
        o2Value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                baselineSaveButton.isEnabled =
                    !(o2Value.text.isEmpty() || co2Value.text.isEmpty() || coValue.text.isEmpty() || h2sValue.text.isEmpty() || lelValue.text.isEmpty())
            }
        })
        co2Value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                baselineSaveButton.isEnabled =
                    !(o2Value.text.isEmpty() || co2Value.text.isEmpty() || coValue.text.isEmpty() || h2sValue.text.isEmpty() || lelValue.text.isEmpty())
            }
        })
        coValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                baselineSaveButton.isEnabled =
                    !(o2Value.text.isEmpty() || co2Value.text.isEmpty() || coValue.text.isEmpty() || h2sValue.text.isEmpty() || lelValue.text.isEmpty())
            }
        })
        h2sValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                baselineSaveButton.isEnabled =
                    !(o2Value.text.isEmpty() || co2Value.text.isEmpty() || coValue.text.isEmpty() || h2sValue.text.isEmpty() || lelValue.text.isEmpty())
            }
        })
        lelValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                baselineSaveButton.isEnabled =
                    !(o2Value.text.isEmpty() || co2Value.text.isEmpty() || coValue.text.isEmpty() || h2sValue.text.isEmpty() || lelValue.text.isEmpty())
            }
        })

        baselineSaveButton.setOnClickListener {
            it.hideKeyboard()
            viewModel.saveBaselineValue(
                'A',
                o2Value.text.toString(),
                coValue.text.toString(),
                lelValue.text.toString(),
                co2Value.text.toString(),
                h2sValue.text.toString()
            )
            if(Preferences.isMasterLevel) {
                loadingDialog.show()
            }else{
                Preferences.showToast(context, "기준치 알람 설정이 완료되었습니다.")
            }
        }
    }

    private fun initClickBar() = with(binding) {
        sensorRegistrationExpandableLayout.duration = 300
        baselineExpandableLayout.duration = 300
        alarmSettingExpandableLayout.duration = 300
        emergencyCallExpandableLayout.duration = 300
        sensorRegistrationLinearLayout.setOnClickListener {
            it.hideKeyboard()
            if (sensorRegistrationExpandableLayout.isExpanded) {
                sensorRegistrationArrowImageButton.animate().rotation(0f).duration = 300
                sensorRegistrationExpandableLayout.collapse()
            } else {
                sensorRegistrationExpandableLayout.expand()
                sensorRegistrationArrowImageButton.animate().rotation(180f).duration = 300

                sensorRegistrationExpandableLayout.setOnExpansionUpdateListener { _, state ->
                    if(state == 2) {
//                        nestedScrollView.smoothScrollTo(0, sensorRegistrationExpandableLayout.height)
                        nestedScrollView.fullScroll(sensorRegistrationExpandableLayout.height)
                    }
                }
            }
        }
        baselineLinearLayout.setOnClickListener {
            it.hideKeyboard()
            if (baselineExpandableLayout.isExpanded) {
                baselineArrowImageButton.animate().rotation(0f).duration = 300
                baselineExpandableLayout.collapse()
            } else {
                baselineArrowImageButton.animate().rotation(180f).duration = 300
                baselineExpandableLayout.expand()

                baselineExpandableLayout.setOnExpansionUpdateListener { _, state ->
                    if(state == 2) {
                        nestedScrollView.smoothScrollTo(0, sensorRegistrationExpandableLayout.height + baselineExpandableLayout.height)
                    }
                }
            }
        }
        alarmSettingLinearLayout.setOnClickListener {
            it.hideKeyboard()
            if (alarmSettingExpandableLayout.isExpanded) {
                alarmSettingArrowImageButton.animate().rotation(0f).duration = 300
                alarmSettingExpandableLayout.collapse()
            } else {
                alarmSettingArrowImageButton.animate().rotation(180f).duration = 300
                alarmSettingExpandableLayout.expand()

                alarmSettingExpandableLayout.setOnExpansionUpdateListener { _, state ->
                    if( state == 2) {
                        nestedScrollView.smoothScrollTo(0, sensorRegistrationExpandableLayout.height + baselineExpandableLayout.height + alarmSettingExpandableLayout.height)
                    }
                }
            }
        }
        emergencyCallLinearLayout.setOnClickListener {
            it.hideKeyboard()
            if (emergencyCallExpandableLayout.isExpanded) {
                emergencyCallArrowImageButton.animate().rotation(0f).duration = 300
                emergencyCallExpandableLayout.collapse()
            } else {
                emergencyCallArrowImageButton.animate().rotation(180f).duration = 300
                emergencyCallExpandableLayout.expand()

                emergencyCallExpandableLayout.setOnExpansionUpdateListener { _, state ->
                    if(state == 2) {
                        nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }


            }
        }
    }

    private fun initArrowButton() = with(binding) {
        sensorRegistrationArrowImageButton.setOnClickListener {
            it.hideKeyboard()
            if (sensorRegistrationExpandableLayout.isExpanded) {
                sensorRegistrationArrowImageButton.animate().rotation(0f).duration = 300
                sensorRegistrationExpandableLayout.collapse()
            } else {
                sensorRegistrationExpandableLayout.expand()
                sensorRegistrationArrowImageButton.animate().rotation(180f).duration = 300
            }
        }
        baselineArrowImageButton.setOnClickListener {
            it.hideKeyboard()
            if (baselineExpandableLayout.isExpanded) {
                baselineArrowImageButton.animate().rotation(0f).duration = 300
                baselineExpandableLayout.collapse()
            } else {
                baselineArrowImageButton.animate().rotation(180f).duration = 300
                baselineExpandableLayout.expand()
            }
        }
        alarmSettingArrowImageButton.setOnClickListener {
            it.hideKeyboard()
            if (alarmSettingExpandableLayout.isExpanded) {
                alarmSettingArrowImageButton.animate().rotation(0f).duration = 300
                alarmSettingExpandableLayout.collapse()
            } else {
                alarmSettingArrowImageButton.animate().rotation(180f).duration = 300
                alarmSettingExpandableLayout.expand()
            }
        }
        emergencyCallArrowImageButton.setOnClickListener {
            it.hideKeyboard()
            if (emergencyCallExpandableLayout.isExpanded) {
                emergencyCallArrowImageButton.animate().rotation(0f).duration = 300
                emergencyCallExpandableLayout.collapse()
            } else {
                emergencyCallArrowImageButton.animate().rotation(180f).duration = 300
                emergencyCallExpandableLayout.expand()
            }
        }
    }

    fun closeLoadingDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            loadingDialog.dismiss()
            Preferences.showToast(context, "기준치 알람 설정 메세지를 송신했습니다.")
        }
    }
    companion object {
        const val TAG = "SettingsFragment"

        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}