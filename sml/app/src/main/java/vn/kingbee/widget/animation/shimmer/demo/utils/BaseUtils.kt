package vn.kingbee.widget.animation.shimmer.demo.utils

import android.content.Context
import vn.kingbee.widget.animation.shimmer.demo.models.ItemCard
import androidx.annotation.StringRes
import android.content.res.Resources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import vn.kingbee.widget.R
import vn.kingbee.widget.animation.shimmer.demo.utils.view.CardPaddingItemDecoration
import java.util.*

class BaseUtils {
    companion object {
        const val TYPE_LIST = 0
        const val TYPE_GRID = 1
        const val TYPE_SECOND_LIST = 2
        const val TYPE_SECOND_GRID = 3

        private fun getListCards(resources: Resources): List<ItemCard> {
            val ndtvCard = createItemCard(
                resources,
                R.string.ndtv_titletext,
                R.string.ndtv_image_url,
                R.string.ndtv_subtext,
                R.string.ndtv_summarytext
            )

            val opCard = createItemCard(
                resources,
                R.string.op_titletext,
                R.string.op_image_url,
                R.string.op_subtext,
                R.string.op_summarytext
            )

            val gotCard = createItemCard(
                resources,
                R.string.got_titletext,
                R.string.got_image_url,
                R.string.got_subtext,
                R.string.got_summarytext
            )

            val jetCard = createItemCard(
                resources,
                R.string.jet_titletext,
                R.string.jet_image_url,
                R.string.jet_subtext,
                R.string.jet_summarytext
            )

            return Arrays.asList(ndtvCard, opCard, gotCard, jetCard)
        }

        private fun getGridCards(resources: Resources): List<ItemCard> {
            val on7 = createItemCard(
                resources,
                R.string.on7_titletext,
                R.string.on7_image_url,
                R.string.on7_subtext,
                R.string.on7_summarytext
            )

            val note5 = createItemCard(
                resources,
                R.string.note5_titletext,
                R.string.note5_image_url,
                R.string.note5_subtext,
                R.string.note5_summarytext
            )

            val pixel = createItemCard(
                resources,
                R.string.pix_titletext,
                R.string.pix_image_url,
                R.string.pix_subtext,
                R.string.pix_summarytext
            )

            val iphone6 = createItemCard(
                resources,
                R.string.i6_titletext,
                R.string.i6_image_url,
                R.string.i6_subtext,
                R.string.i6_summarytext
            )

            val moto = createItemCard(
                resources,
                R.string.moto_titletext,
                R.string.moto_image_url,
                R.string.moto_subtext,
                R.string.moto_summarytext
            )

            val s7 = createItemCard(
                resources,
                R.string.s7_titletext,
                R.string.s7_image_url,
                R.string.s7_subtext,
                R.string.s7_summarytext
            )

            return Arrays.asList(on7, note5, pixel, iphone6, s7, moto)
        }

        fun getCards(resources: Resources, type: Int): List<ItemCard>? {
            val itemCards: List<ItemCard>?

            when (type) {
                TYPE_LIST, TYPE_SECOND_LIST -> itemCards = getListCards(resources)
                TYPE_GRID, TYPE_SECOND_GRID -> itemCards = getGridCards(resources)
                else -> itemCards = null
            }

            return itemCards
        }

        fun getDemoConfiguration(configurationType: Int, context: Context): DemoConfiguration? {
            val demoConfiguration: DemoConfiguration?

            when (configurationType) {
                TYPE_LIST -> {
                    demoConfiguration = DemoConfiguration()
                    demoConfiguration.styleResource = R.style.AppTheme
                    demoConfiguration.layoutResource = R.layout.activity_list
                    demoConfiguration.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    demoConfiguration.titleResource = R.string.ab_list_title
                }
                TYPE_GRID -> {
                    demoConfiguration = DemoConfiguration()
                    demoConfiguration.styleResource = R.style.AppThemeGrid
                    demoConfiguration.layoutResource = R.layout.activity_grid
                    demoConfiguration.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
                    demoConfiguration.titleResource = R.string.ab_grid_title
                }
                TYPE_SECOND_LIST -> {
                    demoConfiguration = DemoConfiguration()
                    demoConfiguration.styleResource = R.style.AppTheme
                    demoConfiguration.layoutResource = R.layout.activity_second_list
                    demoConfiguration.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    demoConfiguration.titleResource = R.string.ab_list_title
                    demoConfiguration.itemDecoration = CardPaddingItemDecoration(context)
                }
                TYPE_SECOND_GRID -> {
                    demoConfiguration = DemoConfiguration()
                    demoConfiguration.styleResource = R.style.AppThemeGrid
                    demoConfiguration.layoutResource = R.layout.activity_second_grid
                    demoConfiguration.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
                    demoConfiguration.titleResource = R.string.ab_grid_title
                }
                else -> demoConfiguration = null
            }

            return demoConfiguration
        }

        private fun createItemCard(resources: Resources, @StringRes title: Int, @StringRes imageUrl: Int, @StringRes description: Int, @StringRes summary: Int): ItemCard {
            val itemCard = ItemCard()

            itemCard.title = resources.getString(title)
            itemCard.thumbnailUrl = resources.getString(imageUrl)
            itemCard.description = resources.getString(description)
            itemCard.summaryText = resources.getString(summary)

            return itemCard
        }
    }

}