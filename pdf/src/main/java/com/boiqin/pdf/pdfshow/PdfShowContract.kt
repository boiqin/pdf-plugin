package com.boiqin.pdf.pdfshow

import android.content.Context
import com.boiqin.pdf.base.BasePresenter
import com.boiqin.pdf.base.BaseView
import java.io.File

/**
 * This specifies the contract between the view and the presenter.
 */
interface PdfShowContract {
    interface View : BaseView<Presenter?> {
        val context: Context?
        val url: String?
        fun displayFromFile(file: File?)
        fun showLoading()
        fun hideLoading()
        val isActive: Boolean
    }

    interface Presenter : BasePresenter {
        fun complete()
    }
}