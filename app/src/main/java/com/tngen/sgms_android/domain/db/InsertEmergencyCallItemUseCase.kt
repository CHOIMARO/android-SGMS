package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import javax.inject.Inject

class InsertEmergencyCallItemUseCase
    @Inject constructor(
    private val emergencyCallRepository: EmergencyCallRepository
): UseCase {
    suspend operator fun invoke(name: String, num: String) {
        emergencyCallRepository.insertEmergencyCallItem(EmergencyCallEntity(
            userName = name,
            userNum = num
        ))
    }
}