package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.BaselineRepository
import javax.inject.Inject

class InsertBaselineItemUsecase
    @Inject constructor(
    private val baselineRepository: BaselineRepository
): UseCase {
    suspend operator fun invoke(o2Value: String, coValue: String, lelValue: String, co2Value: String, h2sValue: String) {
        baselineRepository.insertBaselineItem(BaselineEntity(
            o2 = o2Value.toDouble(),
            co = coValue.toLong(),
            lel = lelValue.toDouble(),
            co2 = co2Value.toLong(),
            h2s = h2sValue.toLong(),
            id = 0
        ))
    }
}