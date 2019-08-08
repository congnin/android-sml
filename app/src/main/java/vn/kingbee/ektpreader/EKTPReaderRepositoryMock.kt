package vn.kingbee.ektpreader

import io.reactivex.Observable
import vn.kingbee.application.MyApp
import vn.kingbee.domain.ektp.entity.EKTPDataAndPhotoResponse
import vn.kingbee.domain.ektp.entity.InitFPScanResponse
import vn.kingbee.domain.ektp.entity.InitHomeScreenResponse
import vn.kingbee.domain.ektp.entity.ReadCardResponse
import vn.kingbee.domain.ektp.repository.EKTPReaderRepository
import vn.kingbee.utils.FileUtils
import java.util.concurrent.TimeUnit

class EKTPReaderRepositoryMock : EKTPReaderRepository {

    inner class MockEktp {
        var readCardResponse: ReadCardResponse? = null
        var fingerPrintResponse: InitFPScanResponse? = null
        var bioDataResponse: EKTPDataAndPhotoResponse? = null
        var initHomeScreenResponse: InitHomeScreenResponse? = null
    }

    private fun getMockEKTP(): Observable<MockEktp>
            = FileUtils.getMockEKTPFromResource(MyApp.getInstance())

    override fun recheckConnection(): Int = 0

    override fun openConnection(): Boolean = true

    override fun readCard(): Observable<ReadCardResponse> {
        return Observable.timer(1, TimeUnit.SECONDS)
            .flatMap { getMockEKTP() }
            .map { response -> response.readCardResponse }
    }

    override fun initFPScan(readCard: Boolean, fpIndex: Int, retryCounter: Int): Observable<InitFPScanResponse> {
        return Observable.timer(1, TimeUnit.SECONDS)
            .flatMap { getMockEKTP() }
            .map { response -> response.fingerPrintResponse }
    }

    override fun getKTPDataAndPhoto(): Observable<EKTPDataAndPhotoResponse> {
        return Observable.timer(5, TimeUnit.SECONDS)
            .flatMap { getMockEKTP() }
            .map { response -> response.bioDataResponse }
    }

    override fun initHomeScreen(): Observable<InitHomeScreenResponse> {
        return Observable.timer(5, TimeUnit.SECONDS)
            .flatMap { getMockEKTP() }
            .map { response -> response.initHomeScreenResponse }
    }

    override fun closeConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}