package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import javax.inject.Inject

class GetEmergencyCallListUseCase
    @Inject constructor(
    private val emergencyCallRepository: EmergencyCallRepository
):UseCase {
    suspend operator fun invoke(): List<EmergencyCallEntity> {
        return emergencyCallRepository.getEmergencyCallList()
    }
}