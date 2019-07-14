package vn.kingbee.widget.viewpager.viewpager2

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter

//https://github.com/googlesamples/android-viewpager2
class BrowseActivity : ListActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = SimpleAdapter(this, getData(),
            android.R.layout.simple_list_item_1, arrayOf("title"),
            intArrayOf(android.R.id.text1))
    }

    private fun getData(): List<Map<String, Any>> {
        val myData = mutableListOf<Map<String, Any>>()

        myData.add(mapOf("title" to "ViewPager2 with Views",
            "intent" to activityToIntent(CardViewActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with Fragments",
            "intent" to activityToIntent(CardFragmentActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with a Mutable Collection (Views)",
            "intent" to activityToIntent(MutableCollectionViewActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with a Mutable Collection (Fragments)",
            "intent" to activityToIntent(MutableCollectionFragmentActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with a TabLayout (Views)",
            "intent" to activityToIntent(CardViewTabLayoutActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with Fake Dragging",
            "intent" to activityToIntent(FakeDragActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with PageTransformers",
            "intent" to activityToIntent(PageTransformerActivity::class.java.name)))

        return myData
    }

    private fun activityToIntent(activity: String): Intent =
        Intent(Intent.ACTION_VIEW).setClassName(this.packageName, activity)

    override fun onListItemClick(listView: ListView, view: View, position: Int, id: Long) {
        val map = listView.getItemAtPosition(position) as Map<*, *>

        val intent = Intent(map["intent"] as Intent)
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE)
        startActivity(intent)
    }
}
