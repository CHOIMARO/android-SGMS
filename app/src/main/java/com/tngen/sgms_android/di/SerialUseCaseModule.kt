package com.tngen.sgms_android.di

import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.domain.serial.ConnectSerialUseCase
import com.tngen.sgms_android.domain.serial.ReceivedSerialUseCase
import com.tngen.sgms_android.domain.serial.SendSerialUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerialUseCaseModule {
    @Singleton
    @Provides
    fun provideConnectSerialUseCase(usbSerialService: USBSerialService) = ConnectSerialUseCase(usbSerialService)

    @Singleton
    @Provides
    fun provideReceivedSerialUseCase(usbSerialService: USBSerialService) = ReceivedSerialUseCase(usbSerialService)

    @Singleton
    @Provides
    fun provideSendSerialUseCase(usbSerialService: USBSerialService) = SendSerialUseCase(usbSerialService)
}