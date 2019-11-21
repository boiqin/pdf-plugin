package com.boiqin.pdf.pdfinit

import com.boiqin.pdf.base.BasePresenter
import com.boiqin.pdf.base.BaseView

/**
 * This specifies the contract between the view and the presenter.
 */
interface PdfInitContract {
    interface View : BaseView<Presenter?>
    interface Presenter : BasePresenter {
        fun openDownload()
    }
}