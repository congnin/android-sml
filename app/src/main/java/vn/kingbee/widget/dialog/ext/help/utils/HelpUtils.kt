package vn.kingbee.widget.dialog.ext.help.utils

import org.apache.commons.lang3.StringUtils
import vn.kingbee.widget.dialog.ext.help.model.HelpCategory
import vn.kingbee.widget.dialog.ext.help.model.HelpConfig
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ
import vn.kingbee.widget.dialog.ext.help.model.HelpPosActiveConfig
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

object HelpUtils {

    fun isMapContainKey(map: Map<String, *>?, key: String): Boolean {
        return map != null && map.containsKey(key)
    }

    /**
     * convert list config to map.
     */
    fun listPosActiveToMap(items: List<HelpPosActiveConfig>?): Map<String, HelpPosActiveConfig> {
        val mapPosActives = HashMap<String, HelpPosActiveConfig>()
        if (items != null) {
            for (item in items) {
                item.listPosActiveToSet()
                mapPosActives[item.categoryId!!] = item
            }
        }
        return mapPosActives
    }

    /**
     * convert list config to map.
     */
    fun listToMapHelpConfigs(items: List<HelpConfig>?): Map<String, HelpConfig> {
        val mapHelpConfigs = HashMap<String, HelpConfig>()
        if (items != null) {
            for (item in items) {
                //convert list position active to map.
                item.listPosActiveToMap()
                //put to map.
                mapHelpConfigs[item.screenName!!] = item
            }
        }
        return mapHelpConfigs
    }

    /**
     * @param categories
     * @param configs
     * @param screenName
     * @return list help will display on the screen.
     */
    fun mergeHelpList(categories: List<HelpCategory>,
                      configs: Map<String, HelpConfig>, screenName: String): List<HelpFAQ> {
        val helps = ArrayList<HelpFAQ>()
        for (category in categories) {
            helps.addAll(getListHelpByCategory(category, configs, screenName))
        }
        return helps
    }


    /**
     * @param category
     * @param mapHelpConfigs
     * @param viewName
     * @return list help by category.
     */
    fun getListHelpByCategory(category: HelpCategory,
                              mapHelpConfigs: Map<String, HelpConfig>,
                              viewName: String): List<HelpFAQ> {
        val helpsFromResponse = category.faqs
        val helps = ArrayList<HelpFAQ>()
        if (helpsFromResponse == null || helpsFromResponse.isEmpty()) {
            return helps
        }
        //get list position active
        val setPosActiveConfigs = getSetPosActiveByCategory(category.categoryId!!, mapHelpConfigs, viewName)
        var isAllItemInactive = true
        //if is empty config: only add category for the first item
        if (setPosActiveConfigs == null || setPosActiveConfigs.isEmpty()) {
            //add all list category
            helps.addAll(helpsFromResponse)
        } else {
            //otherwise: need sort to move active item to the top
            val helpInActives = ArrayList<HelpFAQ>()
            //add item active
            for (i in helpsFromResponse.indices) {
                val helpFAQ = helpsFromResponse.get(i)
                if (isHelpActive(setPosActiveConfigs, i)) {
                    helpFAQ.isActive = true
                    helps.add(helpFAQ)
                    //add helpFAQ answer
                    helps.add(HelpFAQ(helpFAQ.answer))
                } else {
                    helpInActives.add(helpFAQ)
                }
            }

            //add list inactive to end
            helps.addAll(helpInActives)
            isAllItemInactive = helpInActives.size == helps.size
        }

        val itemCategory = HelpFAQ()
        itemCategory.category = category.categoryName
        val faqs = ArrayList<HelpFAQ>()
        faqs.addAll(helps)
        itemCategory.faqs = faqs

        //reset to only view only category.
        if (isAllItemInactive) {
            helps.clear()
            helps.add(itemCategory)
        } else {
            itemCategory.isActive = true
            helps.add(0, itemCategory)
        }

        return helps
    }

    fun getSetPosActiveByCategory(category: String,
                                  mapHelpConfigs: Map<String, HelpConfig>,
                                  screenName: String): Set<Int>? {
        val posActiveConfigMap = getMapPosActivesByScreenName(mapHelpConfigs, screenName)
        return getPosActiveConfigByCategory(posActiveConfigMap, category).setPosActive
    }

    /**
     * check is help at position active or not.
     *
     * @param posActiveConfigs
     * @param pos
     * @return true: help active. Otherwise: false.
     */
    fun isHelpActive(posActiveConfigs: Set<Int>?, pos: Int): Boolean {
        return posActiveConfigs != null && posActiveConfigs.contains(pos)
    }

    /**
     * convert list config to map.
     */
    fun listPosActiveToSet(items: List<Int>?): Set<Int> {
        val set = HashSet<Int>()
        if (items != null) {
            for (item in items) {
                set.add(item)
            }
        }
        return set
    }

    fun getPosActiveConfigByCategory(
        mapPositionActives: Map<String, HelpPosActiveConfig>, category: String): HelpPosActiveConfig {
        return if (isMapContainKey(mapPositionActives, category)) {
            mapPositionActives[category]!!
        } else HelpPosActiveConfig()
    }

    /**
     * @param mapHelpConfigs
     * @param screenName
     * @return map position active by view name.
     */
    fun getMapPosActivesByScreenName(
        mapHelpConfigs: Map<String, HelpConfig>, screenName: String): Map<String, HelpPosActiveConfig> {
        return getHelpConfigByScreenName(mapHelpConfigs, screenName).mapPosActives!!
    }

    /**
     * @param mapHelpConfigs
     * @param screenName
     * @return help config by name.
     */
    fun getHelpConfigByScreenName(mapHelpConfigs: Map<String, HelpConfig>,
                                  screenName: String): HelpConfig {
        return if (isMapContainKey(mapHelpConfigs, screenName)) {
            mapHelpConfigs[screenName]!!
        } else HelpConfig()
    }

    /**
     * @param helpFAQList
     * @param helpFAQItem
     * @return return the index of HelpFAQ item in list parent
     */
    fun findIndexCategoryOfFAQParent(helpFAQList: List<HelpFAQ>?, helpFAQItem: HelpFAQ?): Int {
        if (helpFAQList == null || helpFAQItem == null || helpFAQList.isEmpty())
            return -1

        for (i in helpFAQList.indices) {
            val item = helpFAQList[i]
            if (!StringUtils.isEmpty(item.category) && item.faqs!!.indexOf(helpFAQItem) > -1) {
                return i
            }
        }

        return -1
    }

    /**
     * @param helpFAQList
     * @param strAnswer
     * @return return the index of strAnswer in list parent
     */
    fun findIndexAnswerOfFAQParent(helpFAQList: List<HelpFAQ>?, strAnswer: String): Int {
        if (helpFAQList == null || StringUtils.isEmpty(strAnswer) || helpFAQList.isEmpty())
            return -1

        for (i in helpFAQList.indices) {
            val item = helpFAQList[i]
            if (StringUtils.isEmpty(item.category) && StringUtils.isEmpty(item.question) && item.answer.equals(strAnswer, true)) {
                return i
            }
        }
        return -1
    }
}