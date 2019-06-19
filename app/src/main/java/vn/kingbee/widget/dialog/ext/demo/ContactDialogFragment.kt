package vn.kingbee.widget.dialog.ext.demo

import androidx.fragment.app.Fragment
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.ext.BaseMenuContentDialogFragment
import vn.kingbee.widget.dialog.ext.help.HelpFaqsFragment
import vn.kingbee.widget.dialog.ext.help.HelpMode
import vn.kingbee.widget.tabhost.TabPagerAdapter
import java.util.*

class ContactDialogFragment : BaseMenuContentDialogFragment() {
    override fun getListFragment(): List<TabPagerAdapter.TabPagerItem> {
        return Arrays.asList<TabPagerAdapter.TabPagerItem>(
            TabPagerAdapter.TabPagerItem(HelpFaqsFragment.newInstance(HelpMode.DEFAULT_SCREEN),
                getString(R.string.menu_help_label_tab_faqs)),
            TabPagerAdapter.TabPagerItem(Fragment(),
                getString(R.string.menu_contact_label_tab_give_us_feedback)))
    }

    companion object {
        val TAG_NAME = ContactDialogFragment::class.java.simpleName

        fun newInstance(): ContactDialogFragment {
            return ContactDialogFragment()
        }
    }
}