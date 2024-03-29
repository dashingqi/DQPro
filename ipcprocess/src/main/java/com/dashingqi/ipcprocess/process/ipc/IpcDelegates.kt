package com.dashingqi.ipcprocess.process.ipc

import android.content.Context
import android.os.Bundle
import androidx.annotation.WorkerThread
import com.dashingqi.ipcprocess.process.ipc.IpcDelegateProvider.Companion.KEY_RESULT_BUNDLE

/**
 * 通过Provider调用到主进程
 */
@WorkerThread
fun callOnMainProcessByProvider(
    context: Context, params: Bundle?, delegate: Class<out AbsDelegateProvider>
): DelegateResult {

    val delegateBundle = runCatching {
        context.contentResolver.call(
            PROVIDER_URI, delegate.name, null, params
        )
    }.onFailure {
        it.printStack()
    }.getOrNull()
    delegateBundle ?: return DelegateResult(resultCode = DelegateResultCode.UN_KNOW_ERROR_CODE, null)
    val resultBundle = delegateBundle.getBundle(KEY_RESULT_BUNDLE)
    return DelegateResult(resultCode = delegateBundle.getInt(KEY_RESULT_CODE), resultBundle)
}