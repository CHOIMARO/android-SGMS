package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.history.HistoryEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryListUseCase
    @Inject constructor(
    private val historyRepository: HistoryRepository
) : UseCase {
    suspend operator fun invoke() : List<HistoryEntity> {
        return historyRepository.getHistoryList()
    }
}