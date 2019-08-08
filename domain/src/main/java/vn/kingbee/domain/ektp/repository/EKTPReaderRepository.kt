package vn.kingbee.domain.ektp.repository

import io.reactivex.Observable
import vn.kingbee.domain.ektp.entity.EKTPDataAndPhotoResponse
import vn.kingbee.domain.ektp.entity.InitFPScanResponse
import vn.kingbee.domain.ektp.entity.InitHomeScreenResponse
import vn.kingbee.domain.ektp.entity.ReadCardResponse

interface EKTPReaderRepository {
    fun recheckConnection(): Int

    fun openConnection(): Boolean

    fun readCard(): Observable<ReadCardResponse>

    fun initFPScan(readCard: Boolean, fpIndex: Int, retryCounter: Int): Observable<InitFPScanResponse>

    fun getKTPDataAndPhoto(): Observable<EKTPDataAndPhotoResponse>

    fun initHomeScreen(): Observable<InitHomeScreenResponse>

    fun closeConnection()
}