package vn.kingbee.widget.dialog.ext.help.model

import com.google.gson.annotations.SerializedName
import vn.kingbee.widget.dialog.ext.help.utils.HelpUtils
import timber.log.Timber


class HelpResponse {

    @SerializedName("title")
    var helpTitle: String? = null
    @SerializedName("categories")
    var helpCategories: List<HelpCategory>? = null
    @SerializedName("config")
    var helpConfig: List<HelpConfig>? = null

    var mapHelpConfig: Map<String, HelpConfig>? = null
    var fags: List<HelpFAQ>? = null

    fun getHelpConfig(screenName: String): HelpConfig? {
        return HelpUtils.getHelpConfigByScreenName(mapHelpConfig!!, screenName)
    }

    fun doAfterGetHelpFinish(screenName: String) {
        //convert to Map
        mapHelpConfig = HelpUtils.listToMapHelpConfigs(helpConfig)
        //merge list active and not active
        fags = HelpUtils.mergeHelpList(helpCategories!!, mapHelpConfig!!, screenName)
    }

    /**
     * get first position active.
     *
     * @param screenName
     * @return
     */
    fun getFirstPositionActive(screenName: String): Int {
        val pos: Int
        var posCategory = 0
        val config = getHelpConfig(screenName)
        if (config?.posActiveConfig != null) {
            for (i in 0 until config.posActiveConfig!!.size) {
                val posActiveConfig = config.posActiveConfig!![i]
                if (posActiveConfig.positions != null && posActiveConfig.positions!!.isNotEmpty()) {
                    posCategory = i
                    break
                }
            }
        }

        // the first active helpFAQ item of the category
        pos = posCategory + 1

        Timber.d("HELP: GET FIRST POS: $pos")
        return pos
    }
}

class HelpConfig {
    @SerializedName("screenname")
    var screenName: String? = null
    @SerializedName("activeconfig")
    var posActiveConfig: List<HelpPosActiveConfig>? = null
    var mapPosActives: Map<String, HelpPosActiveConfig>? = null

    fun listPosActiveToMap() {
        //convert list position active to map.
        mapPosActives = HelpUtils.listPosActiveToMap(posActiveConfig)
    }
}

class HelpFAQ {
    @SerializedName("question")
    var question: String? = null
    @SerializedName("answer")
    var answer: String? = null
    @SerializedName("category")
    var category: String? = null
    @SerializedName("isactive")
    var isActive: Boolean = false
    var faqs: List<HelpFAQ>? = null

    constructor()

    constructor(description: String?) {
        answer = description
    }
}

class HelpCategory {
    @SerializedName("categoryid")
    var categoryId: String? = null
    @SerializedName("categoryname")
    var categoryName: String? = null
    @SerializedName("faqs")
    var faqs: List<HelpFAQ>? = null
}

class HelpPosActiveConfig {
    @SerializedName("categoryid")
    var categoryId: String? = null
    @SerializedName("positions")
    var positions: List<Int>? = null
    var setPosActive: Set<Int>? = null

    fun listPosActiveToSet() {
        //convert list position active to set.
        setPosActive = HelpUtils.listPosActiveToSet(positions)
    }
}