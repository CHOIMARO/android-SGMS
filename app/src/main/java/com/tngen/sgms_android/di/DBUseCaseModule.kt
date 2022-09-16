package com.tngen.sgms_android.di

import com.tngen.sgms_android.domain.db.*
import com.tngen.sgms_android.domain.repository.BaselineRepository
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import com.tngen.sgms_android.domain.repository.HistoryRepository
import com.tngen.sgms_android.domain.repository.SensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBUseCaseModule {
    @Singleton
    @Provides
    fun provideInsertSensorItemUseCase(sensorRepository: SensorRepository) = InsertSensorItemUseCase(sensorRepository)

    @Singleton
    @Provides
    fun provideDeleteSensorItemUseCase(sensorRepository: SensorRepository) = DeleteSensorItemUseCase(sensorRepository)

    @Singleton
    @Provides
    fun provideGetSensorListUseCase(sensorRepository: SensorRepository) = GetSensorListUseCase(sensorRepository)

    @Singleton
    @Provides
    fun provideInsertBaselineItemUseCase(baselineRepository: BaselineRepository) = InsertBaselineItemUsecase(baselineRepository)

    @Singleton
    @Provides
    fun provideGetBaselineItemUseCase(baselineRepository: BaselineRepository) = GetBaselineItemUseCase(baselineRepository)

    @Singleton
    @Provides
    fun provideInsertHistoryItemUseCase(historyRepository: HistoryRepository) = InsertHistoryItemUseCase(historyRepository)

    @Singleton
    @Provides
    fun provideGetHistoryListUseCase(historyRepository: HistoryRepository) = GetHistoryListUseCase(historyRepository)

    @Singleton
    @Provides
    fun provideInsertEmergencyCallItemUseCase(emergencyCallRepository: EmergencyCallRepository) = InsertEmergencyCallItemUseCase(emergencyCallRepository)

    @Singleton
    @Provides
    fun provideDeleteEmergencyCallItemUseCase(emergencyCallRepository: EmergencyCallRepository) = DeleteEmergecnyCallItemUseCase(emergencyCallRepository)

    @Singleton
    @Provides
    fun provideGetEmergencyCallItemUseCase(emergencyCallRepository: EmergencyCallRepository) = GetEmergencyCallListUseCase(emergencyCallRepository)

}