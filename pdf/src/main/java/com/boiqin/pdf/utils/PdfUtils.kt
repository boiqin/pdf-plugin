package com.boiqin.pdf.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.util.*

/**
 * Created by chenbo439 on 17/6/30.
 */
object PdfUtils {
    private val TAG = PdfUtils::class.java.simpleName
    const val ARMEABI = "armeabi"
    const val ARMEABI_V7A = "armeabi-v7a"
    const val SO_DIR = "lib"
    const val PDF = "pdf"
    const val DOWNLOAD = "download"
    const val PDF_SUFFIX = ".$PDF"
    const val ZIP_SUFFIX = ".zip"
    val SO_ARRAY =
        arrayOf("libmodpng.so", "libmodft2.so", "libmodpdfium.so", "libjniPdfium.so")
    const val SO_URL = "https://bank-static.pingan.com.cn/iclientstore/sdklib/pdf/"
    val SO_DOWNLOAD_DIR =
        (Environment.getExternalStorageDirectory().absolutePath
                + File.separator + DOWNLOAD)
    val PDF_DOWNLOAD_DIR =
        (Environment.getExternalStorageDirectory().absolutePath
                + File.separator + PDF)

    fun isInitedNative(context: Context, abis: String): Boolean {
        var fileList: List<*> =
            Arrays.asList(*context.fileList())
        if (fileList.contains(SO_DIR)) {
            val abisDir =
                File(context.filesDir.toString() + File.separator + SO_DIR + File.separator + abis)
            if (abisDir.exists() && abisDir.list() != null) {
                fileList = Arrays.asList(*abisDir.list())
                val soList: List<*> =
                    Arrays.asList(*SO_ARRAY)
                return fileList.containsAll(soList)
            }
        }
        return false
    }

    val supportABI: String
        get() {
            val abis: Array<String>?
            abis = if (Build.VERSION.SDK_INT >= 21) {
                Build.SUPPORTED_ABIS
            } else {
                Log.d(
                    TAG,
                    "[copySo] supported api:" + Build.CPU_ABI + " " + Build.CPU_ABI2
                )
                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
            }
            if (abis != null) {
                for (abi in abis) {
                    if (abi == ARMEABI_V7A) {
                        return ARMEABI_V7A
                    }
                }
                return ARMEABI
            } else {
                Log.e(TAG, "Error: get abis == null")
            }
            return ARMEABI
        }

    fun loadPdfSO(path: String, SOArray: Array<String>) {
        try {
            for (so in SOArray) {
                System.load(path + File.separator + so)
            }
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }

    fun showLoading(context: Context?) {
        Handler(Looper.getMainLooper()).post {
            //LoadingWhiteUtils.show(context, "加载中...", false, null);
        }
    }

    fun showLoading(context: Context?, handler: Handler) {
        handler.post {
            // LoadingWhiteUtils.show(context, "加载中...", false, null);
        }
    }

    fun showLoading(context: Context?, msg: String?) {
        Handler(Looper.getMainLooper()).post {
            // LoadingWhiteUtils.show(context, msg, false, null);
        }
    }

    fun showLoading(
        context: Context?,
        handler: Handler,
        msg: String?
    ) {
        handler.post {
            //LoadingWhiteUtils.show(context, msg, false, null);
        }
    }

    fun hideLoading() {
        Handler(Looper.getMainLooper()).post {
            //LoadingWhiteUtils.dismiss();
        }
    }

    fun hideLoading(handler: Handler) {
        handler.post {
            //LoadingWhiteUtils.dismiss();
        }
    }
}