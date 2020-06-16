package vn.kingbee.widget.recyclerview.alphabet.demo

import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import android.os.Bundle
import vn.kingbee.widget.recyclerview.alphabet.IndexFastScrollRecyclerView
import butterknife.BindView
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R


class IndexFastScrollExample : BaseActivity() {

    @BindView(R.id.fast_scroller_recycler)
    lateinit var mRecyclerView: IndexFastScrollRecyclerView

    private var mDataArray: List<String>? = null
    private var mAlphabetItems: MutableList<AlphabetItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_scroll_recyclerview_demo)
        ButterKnife.bind(this)

        initialiseData()
        initialiseUI()
    }

    protected fun initialiseData() {
        //Recycler view data
        mDataArray = DataHelper.alphabetData

        //Alphabet fast scroller data
        mAlphabetItems = ArrayList()
        val strAlphabets = ArrayList<String>()
        for (i in mDataArray!!.indices) {
            val name = mDataArray!![i]
            if (name.trim { it <= ' ' }.isEmpty())
                continue

            val word = name.substring(0, 1)
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word)
                mAlphabetItems!!.add(AlphabetItem(i, word, false))
            }
        }
    }

    protected fun initialiseUI() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = AlphabetAdapter(mDataArray as MutableList<String>?)

        mRecyclerView.setIndexTextSize(12)
        mRecyclerView.setIndexBarColor("#33334c")
        mRecyclerView.setIndexBarCornerRadius(0)
        mRecyclerView.setIndexBarTransparentValue(0.4.toFloat())
        mRecyclerView.setIndexbarMargin(0f)
        mRecyclerView.setIndexbarWidth(40f)
        mRecyclerView.setPreviewPadding(0)
        mRecyclerView.setIndexBarTextColor("#FFFFFF")

        mRecyclerView.setPreviewTextSize(60)
        mRecyclerView.setPreviewColor("#33334c")
        mRecyclerView.setPreviewTextColor("#FFFFFF")
        mRecyclerView.setPreviewTransparentValue(0.6f)

        mRecyclerView.setIndexBarVisibility(true)
        mRecyclerView.setIndexbarHighLateTextColor("#33334c")
        mRecyclerView.setIndexBarHighLateTextVisibility(true)
    }
}