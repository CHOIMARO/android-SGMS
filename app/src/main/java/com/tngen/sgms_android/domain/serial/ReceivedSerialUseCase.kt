package com.tngen.sgms_android.domain.serial

import android.util.Log
import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ReceivedSerialUseCase
    @Inject constructor(
    private val usbSerialService: USBSerialService
): UseCase {
    private val _sharedFlow = MutableSharedFlow<ByteArray>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()
    suspend operator fun invoke() : Unit = coroutineScope {
        launch(Dispatchers.IO) {
            usbSerialService.sharedFlow.collect() {
                receivedMessage(it)

            }
        }
    }

    private suspend fun receivedMessage(dataByte: ByteArray) {
        _sharedFlow.emit(dataByte)
        Log.d("ReceivedSerialUseCase", "메세지 수신중~~")
    }
}