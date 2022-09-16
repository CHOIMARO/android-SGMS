package com.tngen.sgms_android.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tngen.sgms_android.data.entity.history.HistoryEntity
import com.tngen.sgms_android.domain.db.GetHistoryListUseCase
import com.tngen.sgms_android.model.history.HistoryModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryListUseCase: GetHistoryListUseCase
) : BaseViewModel() {

    private var _historyStateLiveData = MutableLiveData<HistoryState>(HistoryState.UnInitialized)
    val historyStateLiveData: LiveData<HistoryState> = _historyStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        val historyList = getHistoryListUseCase()
        _historyStateLiveData.postValue(HistoryState.GetHistoryListSuccess(historyList.map {
            HistoryModel(
                createdAt = it.createdAt,
                sensorId = it.sensorId,
                o2 = it.o2,
                co2 = it.co2,
                co = it.co,
                h2s = it.h2s,
                lel = it.lel,
                id = it.id
            )
        }))

    }
}