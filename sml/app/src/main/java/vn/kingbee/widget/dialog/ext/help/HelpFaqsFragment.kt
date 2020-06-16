package vn.kingbee.widget.dialog.ext.help

import vn.kingbee.widget.base.fragment.BaseFragmentImpl
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import android.widget.TextView
import vn.kingbee.widget.R
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.utils.FileUtils
import vn.kingbee.widget.base.presenter.BasePresenter
import vn.kingbee.widget.dialog.ext.help.adapter.HelpAdapter
import vn.kingbee.widget.dialog.ext.help.model.HelpResponse

class HelpFaqsFragment: BaseFragmentImpl() {
    override fun getPresenter(): BasePresenter<*>? = null

    @BindView(R.id.help_faq_title)
    lateinit var mTvTitle: TextView

    @BindView(R.id.help_list)
    lateinit var mHelpList: RecyclerView

    private var mLinearLayoutManager: LinearLayoutManager? = null
    protected var disposables: CompositeDisposable? = null

    override fun getLayoutContentView(): Int {
        return R.layout.menu_help_faqs_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mHelpList.layoutManager = mLinearLayoutManager
        doLoadHelpContent(getHelpScreenMode())
    }

    protected fun addOneSubscription(disposable: Disposable) {
        if (this.disposables == null) {
            this.disposables = CompositeDisposable()
        }

        this.disposables?.add(disposable)
    }

    fun onDestroyAllSubscription() {
        if (this.disposables != null) {
            this.disposables?.clear()
        }

    }

    protected fun hasSubscription(): Boolean {
        return this.disposables != null && !this.disposables?.isDisposed!!
    }

    fun doLoadHelpContent(screenName: String) {
        showProgressDialog()
        addOneSubscription(getHelpFromResource(screenName)
            .subscribe({ helpResponse -> updateContent(helpResponse) },
                { e ->
                    hideProgressDialog()
                    Timber.d("error load help: %s", e.message)
                },
                { hideProgressDialog() }))
    }

    protected fun getHelpFromResource(screenName: String): Observable<HelpResponse> {
        return FileUtils.getHelpFaqFromResource(MyApp.getInstance()).map {
            it.doAfterGetHelpFinish(screenName)
            return@map it
        }
    }

    fun updateContent(info: HelpResponse?) {
        if (info != null) {
            mTvTitle.text = info.helpTitle

            //list.
            val adapter = HelpAdapter(activity, info.fags) { pos -> mLinearLayoutManager?.scrollToPosition(pos) }

            mHelpList.adapter = adapter
            val posActive = info.getFirstPositionActive(getHelpScreenMode())
            if (posActive >= 1) {
                mLinearLayoutManager?.scrollToPosition(posActive)
            }
        }
    }

    private fun getHelpScreenMode(): String {
        return "default_screen"
    }

    companion object {
        private const val EX_HELP_TYPE = "help_type"

        private fun buildArguments(type: HelpMode): Bundle {
            val bundle = Bundle()
            bundle.putSerializable(EX_HELP_TYPE, type)
            return bundle
        }

        fun newInstance(type: HelpMode): HelpFaqsFragment {
            val fragment = HelpFaqsFragment()
            fragment.arguments = buildArguments(type)
            return fragment
        }
    }
}