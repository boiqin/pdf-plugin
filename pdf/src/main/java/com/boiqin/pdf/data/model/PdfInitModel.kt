package com.boiqin.pdf.data.model

import com.boiqin.pdf.data.DownloadHelper
import com.boiqin.pdf.data.DownloadHelper.OnDownloadListener
import com.boiqin.pdf.data.IModel
import com.boiqin.pdf.utils.PdfUtils

/**
 * Created by boiqin on 2017/7/2.
 */
class PdfInitModel : IModel {
    override fun loadData() {}
    fun loadData(abis: String, onDownloadListener: OnDownloadListener?) {
        DownloadHelper.get().download(
            PdfUtils.SO_URL + abis + PdfUtils.ZIP_SUFFIX,
            PdfUtils.SO_DOWNLOAD_DIR,
            abis + PdfUtils.ZIP_SUFFIX,
            onDownloadListener
        )
    }
}