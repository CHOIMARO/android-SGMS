package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import javax.inject.Inject


class DeleteEmergecnyCallItemUseCase
    @Inject constructor(
    private val emergencyCallRepository: EmergencyCallRepository
): UseCase {
    suspend operator fun invoke(id: Long) {
        emergencyCallRepository.deleteEmergencyCallItem(id)
    }
}