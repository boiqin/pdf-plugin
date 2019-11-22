package com.boiqin.pdf

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boiqin.pdf.pdfshow.PdfShowFragment
import com.boiqin.pdf.pdfshow.PdfShowFragment.Companion.newInstance
import com.boiqin.pdf.pdfshow.PdfShowPresenter
import com.boiqin.pdf.utils.ActivityUtils.addFragmentToActivity

/**
 * Created by boiqin on 19/11/21.
 */
class PdfActivity : AppCompatActivity() {
    var mOnClickListener =
        View.OnClickListener { v ->
            val id = v.id
            if (id == R.id.pdf_header_back) {
                finish()
            }
        }
    private var mTitleTv: TextView? = null
    private var mBackBtn: ImageView? = null
    private var mUiHandler: Handler? = null
    private var mUrl: String? = null
    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf__activity)
        findViews()
        setListener()
        mUiHandler = Handler()
        val intent = intent
        if (intent == null) {
            show(this, resources.getString(R.string.pdf__connect_error))
            return
        }
        mUrl = getIntent().getStringExtra(EXTRA_URL)
        if (TextUtils.isEmpty(mUrl)) {
            show(this, resources.getString(R.string.pdf__connect_error))
            return
        }
        val title = intent.getStringExtra(EXTRA_TITLE)
        mTitleTv!!.text = title
        var pdfShowFragment = supportFragmentManager
            .findFragmentById(R.id.pdf_contentframe) as PdfShowFragment?
        if (pdfShowFragment == null) {
            pdfShowFragment = newInstance()
            addFragmentToActivity(
                supportFragmentManager,
                pdfShowFragment, R.id.pdf_contentframe
            )
        }
        PdfShowPresenter(pdfShowFragment)
    }

    private fun setListener() {
        mBackBtn!!.setOnClickListener(mOnClickListener)
    }

    private fun findViews() {
        mTitleTv =
            findViewById<View>(R.id.pdf_header_title) as TextView
        mBackBtn =
            findViewById<View>(R.id.pdf_header_back) as ImageView
    }

    /**
     * 显示toast
     * @param context
     * @param msg
     */
    fun show(context: Context, msg: String?) {
        val toast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.show()
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"
        private val TAG = PdfActivity::class.java.simpleName
    }
}