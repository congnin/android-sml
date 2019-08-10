package vn.kingbee.domain.entity.configuration

class USBCameraConfiguration {
    var rotation: String? = null
    var scaleImage: String? = null
    var previewWidth: String? = null
    var previewHeight: String? = null
    var ktpPreviewWidth: String? = null
    var ktpPreviewHeight: String? = null
    var payslipPreviewWidth: String? = null
    var payslipPreviewHeight: String? = null
    var brightness: String? = null
    var contrast: String? = null
    var gamma: String? = null
    var hue: String? = null
    var cropStartX: String? = null
    var cropStartY: String? = null
    var cropWidth: String? = null
    var cropHeight: String? = null

    constructor(){
        this.rotation = "180"
        this.scaleImage = "3"
        this.ktpPreviewWidth = "3264"
        this.ktpPreviewHeight = "2448"
        this.payslipPreviewWidth = "1600"
        this.payslipPreviewHeight = "1200"
        this.brightness = "28"
        this.contrast = "30"
        this.gamma = "36"
        this.hue = "50"
    }

    companion object {
        fun getKTPConfiguration(other: USBCameraConfiguration): USBCameraConfiguration {
            val ktpConfiguration = USBCameraConfiguration()
            ktpConfiguration.rotation = other.rotation
            ktpConfiguration.scaleImage = other.scaleImage
            ktpConfiguration.previewWidth = other.ktpPreviewWidth
            ktpConfiguration.previewHeight = other.ktpPreviewHeight
            ktpConfiguration.brightness = other.brightness
            ktpConfiguration.contrast = other.contrast
            ktpConfiguration.gamma = other.gamma
            ktpConfiguration.hue = other.hue
            ktpConfiguration.cropStartX = other.cropStartX
            ktpConfiguration.cropStartY = other.cropStartY
            ktpConfiguration.cropWidth = other.cropWidth
            ktpConfiguration.cropHeight = other.cropHeight
            return ktpConfiguration
        }

        fun getPaySlipConfiguration(other: USBCameraConfiguration): USBCameraConfiguration {
            val payslip = USBCameraConfiguration()
            payslip.rotation = other.rotation
            payslip.scaleImage = other.scaleImage
            payslip.previewWidth = other.payslipPreviewWidth
            payslip.previewHeight = other.payslipPreviewHeight
            payslip.brightness = other.brightness
            payslip.contrast = other.contrast
            payslip.gamma = other.gamma
            payslip.hue = other.hue
            payslip.cropStartX = other.cropStartX
            payslip.cropStartY = other.cropStartY
            payslip.cropWidth = other.cropWidth
            payslip.cropHeight = other.cropHeight
            return payslip
        }
    }
}