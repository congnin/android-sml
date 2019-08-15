package vn.kingbee.domain.dataprocess


interface DataProcessingRepository {
    fun getAccessToken(): String?

    fun getMsisdn(): String?
}