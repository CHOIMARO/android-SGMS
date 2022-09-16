package com.tngen.sgms_android.domain.serial

import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.domain.UseCase
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ConnectSerialUseCase
    @Inject constructor(
    private val usbSerialService: USBSerialService
): UseCase {
    suspend operator fun invoke() = coroutineScope {
        usbSerialService.connect()
    }

}