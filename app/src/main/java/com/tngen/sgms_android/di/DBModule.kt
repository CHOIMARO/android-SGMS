package com.tngen.sgms_android.di

import android.content.Context
import androidx.room.Room
import com.tngen.sgms_android.data.db.ApplicationDatabase
import com.tngen.sgms_android.data.db.dao.BaselineDao
import com.tngen.sgms_android.data.db.dao.EmergencyCallDao
import com.tngen.sgms_android.data.db.dao.HistoryDao
import com.tngen.sgms_android.data.db.dao.SensorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : ApplicationDatabase {
        return Room
            .databaseBuilder(
                context,
                ApplicationDatabase::class.java,
                ApplicationDatabase.DB_NAME
            ).build()
    }

    @Singleton
    @Provides
    fun provideSensorDao(database: ApplicationDatabase) : SensorDao {
      return database.sensorDao()
    }

    @Singleton
    @Provides
    fun provideBaselineDao(database: ApplicationDatabase) : BaselineDao {
      return database.baselineDao()
    }

    @Singleton
    @Provides
    fun provideEmergencyCallDao(database: ApplicationDatabase) : EmergencyCallDao {
        return database.emergencyCallDao()
    }

    @Singleton
    @Provides
    fun provideHistoryDao(database: ApplicationDatabase) : HistoryDao {
        return database.historyDao()
    }
}