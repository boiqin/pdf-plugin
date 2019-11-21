package com.boiqin.pdf.pdfshow

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boiqin.pdf.PdfActivity
import com.boiqin.pdf.R
import com.boiqin.pdf.utils.Objects
import com.boiqin.pdf.utils.PdfUtils
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument
import java.io.File

/**
 * Created by chenbo on 2017/7/2.
 */
class PdfShowFragment : Fragment(),
    PdfShowContract.View,
    OnPageChangeListener,
    OnLoadCompleteListener {
    private var mPresenter: PdfShowContract.Presenter? = null
    private var mPdfView: PDFView? = null
    private var pageNumber = 0
    override val isActive: Boolean
        get() = isAdded

    override fun onResume() {
        super.onResume()
        mPresenter!!.start()
    }

    override fun getContext(): Context? {
        return super.getContext()
    }

    override val url: String
        get() = activity!!.intent.getStringExtra(PdfActivity.EXTRA_URL)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(R.layout.pdf__show_frag, container, false)
        mPdfView =
            root.findViewById<View>(R.id.pdf_pdfview) as PDFView
        mPdfView!!.setBackgroundColor(Color.LTGRAY)
        return root
    }

    override fun showLoading() {
        if (getContext() == null) return
        PdfUtils.showLoading(getContext(), "加载中...")
    }

    override fun hideLoading() {
        if (getContext() == null) return
        PdfUtils.hideLoading()
    }

    override fun setPresenter(presenter: PdfShowContract.Presenter) {
        mPresenter =
            Objects.checkNotNull(
                presenter
            )
    }

    override fun loadComplete(nbPages: Int) {
        val meta = mPdfView!!.documentMeta
        Log.i(TAG, "title = " + meta.title)
        Log.i(TAG, "author = " + meta.author)
        Log.i(TAG, "subject = " + meta.subject)
        Log.i(TAG, "keywords = " + meta.keywords)
        Log.i(TAG, "creator = " + meta.creator)
        Log.i(TAG, "producer = " + meta.producer)
        Log.i(
            TAG,
            "creationDate = " + meta.creationDate
        )
        Log.i(TAG, "modDate = " + meta.modDate)
        printBookmarksTree(mPdfView!!.tableOfContents, "-")
    }

    fun printBookmarksTree(
        tree: List<PdfDocument.Bookmark>,
        sep: String
    ) {
        for (b in tree) {
            Log.i(
                TAG,
                String.format("%s %s, p %d", sep, b.title, b.pageIdx)
            )
            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }

    override fun displayFromFile(file: File?) {
        mPdfView!!.fromFile(file)
            .defaultPage(pageNumber)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this.activity))
            .spacing(10) // in dp
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
    }

    companion object {
        private val TAG = PdfShowFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): PdfShowFragment {
            val arguments = Bundle()
            val fragment = PdfShowFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}