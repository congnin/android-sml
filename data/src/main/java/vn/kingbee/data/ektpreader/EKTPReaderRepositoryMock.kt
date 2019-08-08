package vn.kingbee.data.ektpreader

import vn.kingbee.domain.ektp.entity.EKTPDataAndPhotoResponse
import vn.kingbee.domain.ektp.entity.InitFPScanResponse
import vn.kingbee.domain.ektp.entity.InitHomeScreenResponse
import vn.kingbee.domain.ektp.entity.ReadCardResponse
import vn.kingbee.domain.ektp.repository.EKTPReaderRepository

class EKTPReaderRepositoryMock : EKTPReaderRepository {

    inner class MockEktp {
        var readCardResponse: ReadCardResponse
        var fingerPrintResponse: InitFPScanResponse
        var bioDataResponse: EKTPDataAndPhotoResponse
        var initHomeScreenResponse: InitHomeScreenResponse
    }

    override fun recheckConnection(): Int {
        return 0
    }

    override fun openConnection(): Boolean {
        return true
    }


}