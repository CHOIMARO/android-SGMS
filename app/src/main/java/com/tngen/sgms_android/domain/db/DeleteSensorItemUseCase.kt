package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.SensorRepository
import javax.inject.Inject

class DeleteSensorItemUseCase
    @Inject constructor(
    private val sensorRepository: SensorRepository
) : UseCase {
    suspend operator fun invoke(id: Long) {
        sensorRepository.deleteSensorItem(id)
    }
}