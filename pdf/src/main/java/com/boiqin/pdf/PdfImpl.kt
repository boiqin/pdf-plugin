package com.boiqin.pdf

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.NonNull
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by chenbo439 on 17/6/26.
 */
class PdfImpl {

    val tag: String?
        get() = null

    fun onInstance() {}
    fun onReady() {}
    fun onActive() {}
    fun onRecycle() {}
    fun startTestActivity(cxt: Context?, args: Bundle?) {}
    fun openPdfWindow(context: Context?, @NonNull params: JSONObject) {
        try { // 新页面打开方式
            params.put("type", "window")
        } catch (e: JSONException) {
            Debuger.logE(e.message)
        }
        val pdfParams: PdfParams = JSON.parseObject(params.toString(), PdfParams::class.java)
        val pdfInitPresenter = PdfInitPresenter(context, pdfParams)
        pdfInitPresenter.start()
    }

    fun openMediaAlert(
        webView: AladdinWebView?,
        params: JSONObject,
        callback: String?
    ) {
        if (null == webView) return
        val context: Context = webView.getContext()
        try { // dialog打开方式
            params.put("type", "dialog")
            params.put("showChoice", "false")
            params.put(
                "choiceLabel",
                context.resources.getString(R.string.pdf_risk_revelation_book)
            )
            params.put("callback", callback)
        } catch (e: JSONException) {
            Debuger.logE(e.message)
        }
        val pdfParams: PdfParams = JSON.parseObject(params.toString(), PdfParams::class.java)
        if (TextUtils.isEmpty(pdfParams.getBtnTitle())) {
            pdfParams.setBtnTitle(context.resources.getString(R.string.pdf_buy_now))
        }
        if (TextUtils.isEmpty(pdfParams.getTitle())) {
            pdfParams.setTitle(context.resources.getString(R.string.pdf_risk_revelation))
        }
        val pdfInitPresenter = PdfInitPresenter(webView, pdfParams)
        pdfInitPresenter.start()
    }

    companion object {
        const val SPF_NAME = "local_spf_pdf"
    }
}