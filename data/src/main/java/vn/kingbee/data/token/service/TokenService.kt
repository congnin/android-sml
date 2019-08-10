package vn.kingbee.data.token.service

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import vn.kingbee.domain.entity.token.AccessTokenResponse

interface TokenService {
    @POST("/token")
    @FormUrlEncoded
    fun getToken(
        @Header("Authorization") authorization: String?,
        @Field("grant_type") grantType: String?,
        @Field("scope") scope: String?): Observable<AccessTokenResponse>

    @POST("/revoke")
    @FormUrlEncoded
    fun revokeToken(@Header("Authorization") authorization: String?,
                    @Field("token") accessToken: String?): Observable<AccessTokenResponse>
}