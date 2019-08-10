package vn.kingbee.domain.entity.base

import vn.kingbee.domain.entity.configuration.USBCameraConfiguration
import vn.kingbee.domain.entity.edc.EDCConfiguration

class KioskConfiguration {
    var tokenConfigurations: List<ConfigurationItem>? = null

    var edcConfiguration: EDCConfiguration? = null

    var vsProfileSetting = "_DocDimLarge_5.39" +
            "_DocDimSmall_3.41" +
            "_DoBinarization_" +
            "_DoSkewCorrectionPage_" +
            "_Do90DegreeRotation_4" +
            "_DoBackgroundSmoothing_" +
            "_DoCropCorrection_" +
            "_DoScaleBWImageToDPI_300" +
            "_DoEdgeCleanup_" +
            "_DoContourCleaning_" +
            "_LoadSetting_<Property Name=\"CBinarize.Wei_Green_To_Gray.Int\" Value=\"0\" />" +
            "_LoadSetting_<Property Name=\"CBinarize.Contrast_Slider_Pos.Int\" Value=\"5\" />" +
            "_LoadSetting_<Property Name=\"CBinarize.Cleanup_Slider_Pos.Int\" Value=\"5\" />" +
            "_LoadSetting_<Property Name=\"CBinarize.Wei_Blue_To_Gray.Int\" Value=\"1\" />" +
            "_LoadSetting_<Property Name=\"CBinarize.Wei_Red_To_Gray.Int\" Value=\"0\" />"

    var usbCameraConfiguration: USBCameraConfiguration? = null
}