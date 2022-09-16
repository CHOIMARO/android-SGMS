package com.tngen.sgms_android.domain.db

import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.domain.repository.SensorRepository
import javax.inject.Inject

class GetSensorListUseCase
    @Inject constructor(
    private val sensorRepository: SensorRepository
) : UseCase {
    suspend operator fun invoke(): List<SensorEntity> {
        return sensorRepository.getSensorList()
    }
}