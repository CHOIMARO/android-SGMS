package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.BaselineRepository
import javax.inject.Inject

class GetBaselineItemUseCase
    @Inject constructor(
    private val baselineRepository: BaselineRepository
) : UseCase {
    suspend operator fun invoke(id: Long): BaselineEntity? {
        return baselineRepository.getBaselineItem(id)
    }
}