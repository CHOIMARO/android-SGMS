package com.tngen.sgms_android.di

import com.tngen.sgms_android.data.db.dao.BaselineDao
import com.tngen.sgms_android.data.db.dao.EmergencyCallDao
import com.tngen.sgms_android.data.db.dao.HistoryDao
import com.tngen.sgms_android.data.db.dao.SensorDao
import com.tngen.sgms_android.data.repository.BaselineRepositoryImpl
import com.tngen.sgms_android.data.repository.EmergencyCallRepositoryImpl
import com.tngen.sgms_android.data.repository.HistoryRepositoryImpl
import com.tngen.sgms_android.data.repository.SensorRepositoryImpl
import com.tngen.sgms_android.domain.repository.BaselineRepository
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import com.tngen.sgms_android.domain.repository.HistoryRepository
import com.tngen.sgms_android.domain.repository.SensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideSensorRepository(
        sensorDao: SensorDao,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) : SensorRepository {
        return SensorRepositoryImpl(sensorDao, coroutineDispatcher)
    }

    @Singleton
    @Provides
    fun provideHistoryRepository(
        historyDao: HistoryDao,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) : HistoryRepository {
        return HistoryRepositoryImpl(historyDao, coroutineDispatcher)
    }

    @Singleton
    @Provides
    fun provideBaselineRepository(
        baselineDao: BaselineDao,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) : BaselineRepository {
        return BaselineRepositoryImpl(baselineDao, coroutineDispatcher)
    }

    @Singleton
    @Provides
    fun provideEmergencyCallRepository(
        emergencyCallDao: EmergencyCallDao,
        @CoroutinesQualifiers.IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) : EmergencyCallRepository {
        return EmergencyCallRepositoryImpl(emergencyCallDao, coroutineDispatcher)
    }
}