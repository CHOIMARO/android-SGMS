package com.tngen.sgms_android.presentation.history

import com.tngen.sgms_android.model.history.HistoryModel

sealed class HistoryState {
    object UnInitialized: HistoryState()

    object Loading: HistoryState()

    data class GetHistoryListSuccess(
        val historyList: List<HistoryModel>
    ): HistoryState()

    object Error: HistoryState()
}