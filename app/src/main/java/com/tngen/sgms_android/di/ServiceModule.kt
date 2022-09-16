package com.tngen.sgms_android.di

import android.content.Context
import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.utility.loading_dialog.LoadingDialog
import com.tngen.sgms_android.utility.prodiver.DefaultResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context) : DefaultResourcesProvider {
        return DefaultResourcesProvider(context)
    }

    @Singleton
    @Provides
    fun provideLoadingDialog(@ApplicationContext context: Context) : LoadingDialog {
        return LoadingDialog(context)
    }
    //Serial Service Module
    @Singleton
    @Provides
    fun provideUSBSerialService() = USBSerialService()

    //Retrofit Service Module
}