package vn.kingbee.application

import android.os.Environment
import vn.kingbee.application.Named.APP_PARENT_NAME
import vn.kingbee.application.Named.LOG_FOLDER_API_NAME
import vn.kingbee.application.Named.LOG_FOLDER_APP_NAME
import vn.kingbee.application.Named.LOG_FOLDER_NAME
import vn.kingbee.application.Named.MOCK_DATA_FOLDER_NAME
import vn.kingbee.application.Named.PROPERTIES_FILE_NAME
import vn.kingbee.application.Named.PROPERTIES_FOLDER_NAME
import java.io.File

object AppConstant {

    const val LINE_SEPARATOR = "\n"
    const val APP_TIMEOUT = 60 * 1000L
}

object Named {
    const val APP_PARENT_NAME = "android-widget"
    const val PROPERTIES_FILE_NAME = "application.properties"
    const val PROPERTIES_FOLDER_NAME = "properties"
    const val MOCK_DATA_FOLDER_NAME = "mock_data"
    const val LOG_FOLDER_NAME = "log"
    const val LOG_FOLDER_API_NAME = "api"
    const val LOG_FOLDER_APP_NAME = "app"
    const val DATA_APP_NAME = "data"
    const val TNC_NAME = "terms_and_conditions"

    const val MOCK_CONTENT_REPOSITORY = "MOCK_CONTENT_REPOSITORY"
    const val CONTENT_REPOSITORY_MOCK_SD_CARD = "MOCK_SD_CARD_CONTENT_REPOSITORY"
    const val CONTENT_REPOSITORY_MOCK_ASSET = "MOCK_ASSET_CONTENT_REPOSITORY"
    const val OK_HTTP_CLIENT_SELECTED = "OK_HTTP_CLIENT_SELECTED"
    const val OK_HTTP_CLIENT_MOCK = "OK_HTTP_CLIENT_MOCK"
    const val OK_HTTP_CLIENT_REAL = "OK_HTTP_CLIENT_REAL"
    const val END_POINT = "END_POINT"
    const val TIMER_FINGER_THANK_YOU_GO_TO_NEXT = "TIMER_FINGER_THANK_YOU_GO_TO_NEXT"
    const val ORIGINATION_RETROFIT = "ORIGINATION_RETROFIT"
    const val CSM_RETROFIT = "CSM_RETROFIT"
    const val SERVICING_CARD_STOCK_REPO = "SERVICING_CARD_STOCK_REPO"
    const val ORIGINATION_CARD_STOCK_REPO = "ORIGINATION_CARD_STOCK_REPO"
    const val ORIGINATION_CARD_STOCK_USECASE = "ORIGINATION_CARD_STOCK_USECASE"
    const val SERVICING_CARD_STOCK_USECASE = "SERVICING_CARD_STOCK_USECASE"
    const val ID_DOCUMENT_WIDTH = "ID_DOCUMENT_WIDTH"
    const val ID_DOCUMENT_HEIGHT = "ID_DOCUMENT_HEIGHT"
}

object CachedPath {
    private val CHAR_SEP = File.separatorChar
    // Base folder path on sdcard
    private val APP_PARENT_PATH = Environment.getExternalStorageDirectory().path + CHAR_SEP + APP_PARENT_NAME
    // properties/application.properties
    val PROPERTIES_ASSET_PATH = PROPERTIES_FOLDER_NAME + CHAR_SEP + PROPERTIES_FILE_NAME
    // sdcard/Kiosk/properties
    val PROPERTIES_FOLDER_PATH = APP_PARENT_PATH + CHAR_SEP + PROPERTIES_FOLDER_NAME
    // sdcard/Kiosk/properties/application.properties
    val PROPERTIES_FILE_PATH = PROPERTIES_FOLDER_PATH + CHAR_SEP + PROPERTIES_FILE_NAME
    // sdcard/Kiosk/mock
    val MOCK_SD_CARD_FOLDER_PATH = APP_PARENT_PATH + CHAR_SEP + MOCK_DATA_FOLDER_NAME

    // sdcard/kiosk/properties/log
    val LOG_PATH_BASE = APP_PARENT_PATH + CHAR_SEP + LOG_FOLDER_NAME
    val LOG_PATH_API = LOG_PATH_BASE + CHAR_SEP + LOG_FOLDER_API_NAME
    val LOG_PATH_APP = LOG_PATH_BASE + CHAR_SEP + LOG_FOLDER_APP_NAME
}