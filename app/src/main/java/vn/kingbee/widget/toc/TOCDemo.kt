package vn.kingbee.widget.toc

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import vn.kingbee.utils.UIUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R

class TOCDemo : BaseActivity() {
    lateinit var webViewCustomerStatement: WebView
    lateinit var webViewTermCondition: WebView
    lateinit var upScrollImage: ImageView
    lateinit var downScrollImage: ImageView
    lateinit var scrollView: ScrollView
    lateinit var btnAgree: Button
    lateinit var collapseIndicator: ImageView
    lateinit var termConditionContainer: View

    internal var isTermConditionExpand = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_of_condition)

        initViews()
        initEvents()
    }

    fun initViews() {
        webViewCustomerStatement = findViewById(R.id.web_view_customer_statement)
        webViewTermCondition = findViewById(R.id.web_view_term_condition)
        upScrollImage = findViewById(R.id.up_scroll)
        downScrollImage = findViewById(R.id.down_scroll)
        scrollView = findViewById(R.id.scroll_view)
        btnAgree = findViewById(R.id.btn_read_and_agree)
        collapseIndicator = findViewById(R.id.collapse_indicator)
        termConditionContainer = findViewById(R.id.web_view_layout)
    }

    fun initEvents() {
        webViewCustomerStatement.isVerticalScrollBarEnabled = false
        webViewCustomerStatement.isHorizontalScrollBarEnabled = false
        webViewCustomerStatement.loadUrl(PATH_CUSTOMER_STATEMENT_FILE)

        webViewTermCondition.isVerticalScrollBarEnabled = false
        webViewTermCondition.isHorizontalFadingEdgeEnabled = false

        webViewTermCondition.loadUrl(PATH_TERM_AND_CONDITION_FILE)
        webViewTermCondition.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgressDialog()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideProgressDialog()
                termConditionContainer.visibility = View.VISIBLE
            }
        }

        upScrollImage.setOnClickListener {
            val scrollY = scrollView.scrollY
            if (scrollY > STEP_SCROLL) {
                scrollView.scrollY = scrollY - STEP_SCROLL
            } else {
                scrollView.scrollY = 0
            }
        }

        downScrollImage.setOnClickListener {
            scrollView.scrollY = scrollView.scrollY + STEP_SCROLL
        }

        collapseIndicator.setOnClickListener {
            isTermConditionExpand = !isTermConditionExpand
            if (isTermConditionExpand) {
                collapseTermCondition()
            } else {
                expandTermCondition()
            }
        }

        btnAgree.setOnClickListener {
            if (isTermConditionExpand) {
                isTermConditionExpand = false
                collapseTermCondition()
                finish()
            } else {
                expandTermCondition()
            }
        }
    }

    fun collapseTermCondition() {
        UIUtils.linearCollapse(webViewTermCondition, ANIMATION_DURATION)
        val unwiseClockRotate = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        unwiseClockRotate.duration = ANIMATION_DURATION.toLong()
        unwiseClockRotate.fillAfter = true
        collapseIndicator.startAnimation(unwiseClockRotate)
    }

    fun expandTermCondition() {
        UIUtils.linearExpand(webViewTermCondition, ANIMATION_DURATION)
        val wiseClockRotate = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        wiseClockRotate.duration = ANIMATION_DURATION.toLong()
        wiseClockRotate.fillAfter = true
        collapseIndicator.startAnimation(wiseClockRotate)
    }

    companion object {
        private const val STEP_SCROLL = 50
        private const val ANIMATION_DURATION = 500
        private const val PATH_CUSTOMER_STATEMENT_FILE = "file:///android_asset/customer_statement.html"
        private const val PATH_TERM_AND_CONDITION_FILE = "file:///android_asset/term_condition.html"
    }
}