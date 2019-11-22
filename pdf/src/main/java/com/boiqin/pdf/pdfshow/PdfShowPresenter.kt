package com.boiqin.pdf.pdfshow

import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.boiqin.pdf.utils.DownloadHelper.Companion.buildDir
import com.boiqin.pdf.utils.DownloadHelper.Companion.getNameFromUrl
import com.boiqin.pdf.utils.DownloadHelper.OnDownloadListener
import com.boiqin.pdf.data.model.PdfShowModel
import com.boiqin.pdf.utils.Objects
import com.boiqin.pdf.utils.PdfUtils
import java.io.File
import java.io.IOException

/**
 * Listens to user actions from the UI ([PdfShowFragment]), retrieves the data and updates
 * the UI as required.
 */
class PdfShowPresenter(pdfShowView: PdfShowContract.View) :
    PdfShowContract.Presenter {
    private val mPdfShowView: PdfShowContract.View
    override fun start() {
        openTask()
    }

    private fun openTask() {
        val url = mPdfShowView.url
        val f = File(
            Environment.getExternalStorageDirectory().absolutePath
                    + File.separator + PdfUtils.PDF, getNameFromUrl(url)
        )
        if (f.exists()) {
            mPdfShowView.displayFromFile(f)
        } else { // 不存在则去网络下载
            mPdfShowView.showLoading()
            PdfShowModel().loadData(url, object : OnDownloadListener {
                override fun onDownloadSuccess() {
                    if (!mPdfShowView.isActive) {
                        return
                    }
                    mPdfShowView.hideLoading()
                    Handler(Looper.getMainLooper()).post {
                        var f: File? = null
                        try {
                            f = File(
                                buildDir(
                                    Environment.getExternalStorageDirectory().absolutePath
                                            + File.separator + PdfUtils.PDF
                                ), getNameFromUrl(url)
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        mPdfShowView.displayFromFile(f)
                    }
                }

                override fun onDownloading(progress: Int) {}
                override fun onDownloadFailed() {
                    if (!mPdfShowView.isActive) {
                        return
                    }
                    mPdfShowView.hideLoading()
                    Handler(Looper.getMainLooper())
                        .post {
                            //ToastUtils.makeText(mPdfShowView.getContext(), TransferManger.getInstance().getMessage(PABankErrorCode.TRANSFER_PDF_DOWNLOAD_FAILED), Toast.LENGTH_SHORT).show();
                        }
                }
            })
        }
    }

    override fun complete() { // 释放资源
    }

    init {
        mPdfShowView =
            Objects.checkNotNull(
                pdfShowView,
                "pdfShowView cannot be null!"
            )
        mPdfShowView.setPresenter(this)
    }
}