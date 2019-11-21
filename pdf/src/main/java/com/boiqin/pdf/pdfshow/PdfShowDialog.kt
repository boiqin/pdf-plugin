package com.boiqin.pdf.pdfshow

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.TextView
import com.boiqin.pdf.R
import com.boiqin.pdf.data.model.PdfParams
import com.boiqin.pdf.utils.Objects
import com.boiqin.pdf.utils.PdfUtils
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.shockwave.pdfium.PdfDocument
import java.io.File

/**
 * Created by chenbo on 2018/4/26.
 * 此类是在当前页面弹出dialog方式来加载pdf
 */
class PdfShowDialog : Dialog(),
    PdfShowContract.View,
    OnPageChangeListener,
    OnLoadCompleteListener {
    private var mTimeTick = 0
    private var mPresenter: PdfShowContract.Presenter? = null
    private var mPdfView: PDFView? = null
    private var mCloseFl: ViewGroup? = null
    private var mTitleTv: TextView? = null
    private var mConfirmTv: TextView? = null
    private var mLabelCb: CheckBox? = null
    private var mLabelTv: TextView? = null
    private var mLabelContainer: ViewGroup? = null
    private val mParams: PdfParams? = null
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            var msg = msg
            super.handleMessage(msg)
            when (msg.what) {
                MSG_TIME_TICK -> if (mTimeTick == 0) { //                        SharedPreferencesUtil.saveBooleanVal(PdfImpl.SPF_NAME, DownloadHelper.getNameFromUrl(getUrl()), true);
                    setBtnActive()
                } else {
                    mConfirmTv!!.text = mParams!!.btnTitle + "(" + mTimeTick + "s)"
                    msg = Message.obtain()
                    msg.what = MSG_TIME_TICK
                    sendMessageDelayed(msg, 1000)
                    mTimeTick--
                }
            }
        }
    }

    private fun setBtnActive() {
        mConfirmTv!!.setTextColor(getContext().resources.getColor(R.color.pdf_status_text))
        mConfirmTv!!.text = mParams!!.btnTitle
        mConfirmTv!!.setOnClickListener {
            executeCallBackByWebView(true)
            dismiss()
        }
    }

    private fun executeCallBackByWebView(isCancel: Boolean) { //        String callback = mParams.getCallback();
//
//        if (!TextUtils.isEmpty(callback) && null != mWebView) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("agreement", String.valueOf(isCancel));
//            } catch (Exception e) {
//                Debuger.logE(e.getMessage());
//            }
//            AladdinJSExecutor.callJSONObjectToJS(mWebView, callback, jsonObject, null);
//        }
    }

    override val isActive: Boolean
        get() = isShowing

    //    public PdfShowDialog(@NonNull AladdinWebView webView, @NonNull PdfParams params) {
//        super(webView.getContext(), R.style.pdf_dialog);
//        mParams = params;
//        mWebView = webView;
//    }
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.pdf_show_dialog);
        val window = window
        if (null != window) {
            val layoutParams = window.attributes
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            //layoutParams.width = (int) (Tools.getScreenWidth(getContext())) - WindowUtils.getInstance().dip2px(40);
//layoutParams.height = (int) (Tools.getScreenHeight(getContext())) - WindowUtils.getInstance().dip2px(160);
            window.attributes = layoutParams
        }
        setCancelable(false)
        mPdfView =
            findViewById<View>(R.id.pdf_pdfview) as PDFView
        mTitleTv =
            findViewById<View>(R.id.tv_pdf_header_title) as TextView
        mTitleTv!!.text = mParams!!.title
        mConfirmTv =
            findViewById<View>(R.id.tv_pdf_confirm) as TextView
        mLabelContainer =
            findViewById<View>(R.id.ll_pdf_label) as ViewGroup
        mLabelCb =
            findViewById<View>(R.id.cb_pdf_label) as CheckBox
        mLabelTv =
            findViewById<View>(R.id.tv_pdf_label) as TextView
        if (mParams.showChoice == java.lang.Boolean.TRUE.toString()) {
            mLabelContainer!!.visibility = View.VISIBLE
            mLabelTv!!.text = mParams.choiceLabel
        } else {
            mLabelContainer!!.visibility = View.GONE
        }
        mTimeTick = try {
            mParams.time!!.toInt()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            0
        }
        if (mTimeTick == 0) {
            mTimeTick = DEFAULT_TIME_OUT
        }
        mCloseFl =
            findViewById<View>(R.id.fl_pdf_header_close) as ViewGroup
        mCloseFl!!.setOnClickListener {
            executeCallBackByWebView(false)
            dismiss()
        }
        mPresenter!!.start()
    }

    override val url: String?
        get() = mParams!!.url

    override fun showLoading() {
        PdfUtils.showLoading(getContext(), "加载中...")
    }

    override fun hideLoading() {
        PdfUtils.hideLoading()
    }

    override fun setPresenter(presenter: PdfShowContract.Presenter) {
        mPresenter =
            Objects.checkNotNull(
                presenter
            )
    }

    override fun loadComplete(nbPages: Int) {
        val msg = Message.obtain()
        msg.what = MSG_TIME_TICK
        mHandler.sendMessage(msg)
        val meta = mPdfView!!.documentMeta
        Log.i(TAG, "title = " + meta.title)
        Log.i(TAG, "author = " + meta.author)
        Log.i(TAG, "subject = " + meta.subject)
        Log.i(TAG, "keywords = " + meta.keywords)
        Log.i(TAG, "creator = " + meta.creator)
        Log.i(TAG, "producer = " + meta.producer)
        Log.i(TAG, "creationDate = " + meta.creationDate)
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

    override fun show() {
        super.show()
        mConfirmTv!!.text = mParams!!.loadingText
        //        boolean isShowed = SharedPreferencesUtil.getBooleanVal(PdfImpl.SPF_NAME, DownloadHelper.getNameFromUrl(getUrl()), false);
//        if(isShowed){
//            setBtnActive();
//        }else {
//        Message msg = Message.obtain();
//        msg.what = MSG_TIME_TICK;
//        mHandler.sendMessage(msg);
//        }
    }

    override fun dismiss() {
        super.dismiss()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun displayFromFile(file: File?) {
        mPdfView!!.fromFile(file)
            .defaultPage(0)
            .onPageChange(this)
            .onPageScroll { page, positionOffset -> }
            .enableAnnotationRendering(true)
            .onLoad(this) //.scrollHandle(new DefaultScrollHandle(getContext()))
            .spacing(10) // in dp
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) { //
    }

    companion object {
        private val TAG = PdfShowDialog::class.java.simpleName
        private const val MSG_TIME_TICK = 0x1
        private const val DEFAULT_TIME_OUT = 20
    }
}