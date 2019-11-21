package com.boiqin.pdf.data

import android.util.Log
import androidx.annotation.NonNull
import com.boiqin.pdf.utils.PdfUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


/**
 * Created by chenbo439 on 17/6/27.
 */
object DownloadHelper {

    private val TAG = DownloadHelper::class.java.simpleName

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
        //FileDownloader.setup(Context)
        FileDownloader.getImpl().create(url)
            .setPath(saveDir)
            .setListener(object : FileDownloadListener() {
                override fun pending(
                    task: BaseDownloadTask,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                }

                override fun connected(
                    task: BaseDownloadTask,
                    etag: String,
                    isContinue: Boolean,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                }

                override fun progress(
                    task: BaseDownloadTask,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                    val progress = (soFarBytes * 1.0f / totalBytes * 100).toInt()
                    listener.onDownloading(progress)
                }

                override fun blockComplete(task: BaseDownloadTask) {}
                override fun retry(
                    task: BaseDownloadTask,
                    ex: Throwable,
                    retryingTimes: Int,
                    soFarBytes: Int
                ) {
                }

                override fun completed(task: BaseDownloadTask) {
                    listener.onDownloadSuccess()
                }

                override fun paused(
                    task: BaseDownloadTask,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                }

                override fun error(
                    task: BaseDownloadTask,
                    e: Throwable
                ) {
                    listener.onDownloadFailed()
                }

                override fun warn(task: BaseDownloadTask) {}
            }).start()

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

}