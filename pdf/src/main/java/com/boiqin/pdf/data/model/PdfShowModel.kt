package com.boiqin.pdf.data.model

import com.boiqin.pdf.data.DownloadHelper
import com.boiqin.pdf.data.DownloadHelper.OnDownloadListener
import com.boiqin.pdf.data.IModel
import com.boiqin.pdf.utils.PdfUtils

/**
 * Created by chenbo on 2017/7/2.
 */
class PdfShowModel : IModel {
    override fun loadData() {}
    fun loadData(url: String?, onDownloadListener: OnDownloadListener) {
        DownloadHelper.download(
            url,
            PdfUtils.PDF_DOWNLOAD_DIR,
            DownloadHelper.getNameFromUrl(url),
            onDownloadListener
        )
    }
}