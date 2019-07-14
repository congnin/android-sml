package vn.kingbee.widget.viewpager.viewpager2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import vn.kingbee.widget.R

class MutableCollectionViewActivity : MutableCollectionBaseActivity() {
    override fun createViewPagerAdapter(): RecyclerView.Adapter<*> {
        val items = items // avoids resolving the ViewModel multiple times
        val clickRegistry: ClickRegistry by viewModels()
        return object : RecyclerView.Adapter<PageViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, type: Int) = PageViewHolder(parent)
            override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
                val itemId = holder.itemId
                val clickHandler = { clickRegistry.registerClick(itemId) }
                val clickCountProvider = { clickRegistry.getClickCount(itemId) }
                holder.bind(items.getItemById(itemId), clickHandler, clickCountProvider)
            }

            override fun getItemCount(): Int = items.size
            override fun getItemId(position: Int): Long = items.itemId(position)
        }.apply { setHasStableIds(true) }
    }
}

class PageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_mutable_collection, parent, false)
    ) {
    private val textViewItemId: TextView = itemView.findViewById(R.id.textViewItemText)
    private val textViewCount: TextView = itemView.findViewById(R.id.textViewCount)
    private val buttonCountIncrease: Button = itemView.findViewById(R.id.buttonCountIncrease)

    fun bind(itemText: String, registerClick: () -> Unit, getClickCount: () -> Int) {
        textViewItemId.text = itemText
        val updateClickText = { textViewCount.text = "${getClickCount()}" }
        updateClickText()

        buttonCountIncrease.setOnClickListener {
            registerClick()
            updateClickText()
        }
    }
}

/**
 * Stores click counts for items. Items are identified by an id.
 */
class ClickRegistry : ViewModel() {
    private val clickCount = mutableMapOf<Long, Int>()
    fun getClickCount(itemId: Long): Int = clickCount[itemId] ?: 0
    fun registerClick(itemId: Long) = clickCount.set(itemId, 1 + getClickCount(itemId))
}