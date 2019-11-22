package com.boiqin.pdf.pdfinit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.boiqin.pdf.PdfActivity
import com.boiqin.pdf.utils.DownloadHelper.OnDownloadListener
import com.boiqin.pdf.data.model.PdfInitModel
import com.boiqin.pdf.data.model.PdfParams
import com.boiqin.pdf.utils.FileUtils
import com.boiqin.pdf.utils.PdfUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class PdfInitPresenter(
    private val mContext: Context?, // 传过来的参数
    private val mParams: PdfParams?
) :
    PdfInitContract.Presenter {
    private var mABIS: String? = null

    override fun start() {
        openTask()
    }

    private fun openTask() {
        if (mContext == null) return
        mABIS = PdfUtils.supportABI
        val isInit = PdfUtils.isInitedNative(mContext, mABIS)
        if (isInit) { // 本地存在so库
            loadPdfSO()
            goToShowPdf()
        } else {
            openDownload()
        }
    }

    override fun openDownload() { // 不存在去网络下载
        val handler = Handler(Looper.getMainLooper())
        PdfUtils.showLoading(mContext, handler, "初始化...")
        PdfInitModel().loadData(mABIS!!, object : OnDownloadListener {
            override fun onDownloadSuccess() {
                if (mContext == null) return
                val zipFile =
                    File(PdfUtils.SO_DOWNLOAD_DIR + File.separator + mABIS + PdfUtils.ZIP_SUFFIX)
                var unzip = false
                var fileInputStream: FileInputStream? = null
                try {
                    fileInputStream = FileInputStream(zipFile)
                    unzip = FileUtils.decompressZip(
                        fileInputStream,
                        PdfUtils.SO_DOWNLOAD_DIR
                    )
                    fileInputStream.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (unzip) {
                    val oldPath =
                        PdfUtils.SO_DOWNLOAD_DIR + File.separator + mABIS
                    FileUtils.copyFolder(
                        oldPath,
                        mContext.filesDir.toString() + File.separator + PdfUtils.SO_DIR + File.separator + mABIS
                    )
                    // 拷贝完成，删除下载和解压的文件
                    zipFile.delete()
                    //com.pingan.core.happy.utils.FileUtil.deleteDirectory(oldPath);
                    PdfUtils.hideLoading(handler)
                    handler.post {
                        loadPdfSO()
                        goToShowPdf()
                    }
                }
            }

            override fun onDownloading(progress: Int) {}
            override fun onDownloadFailed() {
                if (mContext == null) return
                PdfUtils.hideLoading(handler)
                // 下载失败
                handler.post {
                    //ToastUtils.makeText(mContext, TransferManger.getInstance().getMessage(PABankErrorCode.TRANSFER_PDF_DOWNLOAD_FAILED), Toast.LENGTH_SHORT).show();
                }
            }
        })
    }

    private fun goToShowPdf() {
        if (mParams == null) return
        val type = mParams.type
        if ("window" == type) {
            goToPdfShowFragment()
        } else if ("dialog" == type) {
            showDialog()
        } else {
            goToPdfShowFragment()
        }
    }

    private fun goToPdfShowFragment() {
        if (mContext != null) {
            val title = mParams!!.title
            val filePath = mParams.file
            val data = Bundle()
            data.putString(PdfActivity.EXTRA_URL, filePath)
            data.putString(PdfActivity.EXTRA_TITLE, title)
            val intent = Intent(mContext, PdfActivity::class.java)
            intent.putExtras(data)
            mContext.startActivity(intent)
        }
    }

    private fun showDialog() {
        if (mParams != null) { //            PdfShowDialog pdfShowDialog = new PdfShowDialog(mWebView, mParams);
//            new PdfShowPresenter(pdfShowDialog);
//            pdfShowDialog.show();
        }
    }

    private fun loadPdfSO() {
        if (mContext == null) return
        PdfUtils.loadPdfSO(
            mContext.filesDir.toString() + File.separator + PdfUtils.SO_DIR + File.separator + mABIS,
            PdfUtils.SO_ARRAY
        )
    }

}