package com.boiqin.pdf.data

import android.util.Log
import com.boiqin.pdf.utils.PdfUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by chenbo439 on 17/6/27.
 */
class DownloadHelper private constructor() {
    private val mOkHttpClient: OkHttpClient
    /**
     * @param url 下载链接
     * @param saveDir 储存下载文件目录
     * @param saveName 储存下载文件名
     * @param listener 下载监听
     */
    fun download(
        url: String?,
        saveDir: String?,
        saveName: String?,
        listener: OnDownloadListener
    ) {
        val request: Request = Builder().addHeader("Accept-Encoding", "identity").url(url).build()
        mOkHttpClient.newCall(request).enqueue(object : Callback() {
            fun onFailure(call: Call?, e: IOException?) { // 下载失败
                listener.onDownloadFailed()
            }

            @Throws(IOException::class)
            fun onResponse(call: Call?, response: Response) {
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                var fos: FileOutputStream? = null
                // 储存下载文件的目录
                val savePath = buildDir(saveDir)
                try {
                    `is` = response.body().byteStream()
                    val total: Long = response.body().contentLength()
                    val file = File(savePath, saveName)
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (`is`.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        // 下载中
                        listener.onDownloading(progress)
                    }
                    fos.flush()
                    // 下载完成
                    listener.onDownloadSuccess()
                } catch (e: Exception) {
                    val file = File(savePath, saveName)
                    if (file.exists()) {
                        file.delete()
                    }
                    listener.onDownloadFailed()
                } finally {
                    try {
                        `is`?.close()
                    } catch (e: IOException) {
                        Log.e(TAG, e.message)
                    }
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        Log.e(TAG, e.message)
                    }
                }
            }
        })
    }

    interface OnDownloadListener {
        /**
         * 下载成功
         */
        fun onDownloadSuccess()

        /**
         * @param progress
         * 下载进度
         */
        fun onDownloading(progress: Int)

        /**
         * 下载失败
         */
        fun onDownloadFailed()
    }

    companion object {
        private val TAG = DownloadHelper::class.java.simpleName
        @Volatile
        private var mDownloadUtil: DownloadHelper? = null

        fun get(): DownloadHelper? {
            if (mDownloadUtil == null) {
                synchronized(DownloadHelper::class.java) {
                    if (mDownloadUtil == null) {
                        mDownloadUtil = DownloadHelper()
                    }
                }
            }
            return mDownloadUtil
        }

        /**
         * @param saveDir
         * @return
         * @throws IOException
         * 判断下载目录是否存在
         */
        @JvmStatic
        @Throws(IOException::class)
        fun buildDir(saveDir: String?): String { // 下载位置
            val downloadFile = File(saveDir)
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile()
            }
            return downloadFile.absolutePath
        }

        /**
         * @param url
         * @return
         * 从下载连接中解析出文件名
         */
        @JvmStatic
        @NonNull
        fun getNameFromUrl(@NonNull url: String?): String {
            val urlMD5: String = MD5.GetMD5Code(url)
            return urlMD5 + PdfUtils.PDF_SUFFIX
        }
    }

    init {
        mOkHttpClient = OkHttpClient()
    }
}