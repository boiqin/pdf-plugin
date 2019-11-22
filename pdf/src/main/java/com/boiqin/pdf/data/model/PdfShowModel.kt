package com.boiqin.pdf.data.model

import com.boiqin.pdf.data.IModel
import com.boiqin.pdf.utils.DownloadHelper
import com.boiqin.pdf.utils.PdfUtils

/**
 * Created by chenbo on 2017/7/2.
 */
class PdfShowModel : IModel {
    override fun loadData() {}
    fun loadData(url: String?, onDownloadListener: DownloadHelper.OnDownloadListener) {
        DownloadHelper.download(
            url,
            PdfUtils.PDF_DOWNLOAD_DIR,
            //DownloadHelper.getNameFromUrl(url),
            "",//TODO
            onDownloadListener
        )
    }
}