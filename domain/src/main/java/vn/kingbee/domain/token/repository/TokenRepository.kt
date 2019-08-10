package vn.kingbee.domain.token.repository

import io.reactivex.Observable
import vn.kingbee.domain.entity.token.AccessTokenRequest
import vn.kingbee.domain.entity.token.AccessTokenResponse

interface TokenRepository {
    fun getToken(request: AccessTokenRequest): Observable<AccessTokenResponse>

    fun revokeToken(request: AccessTokenRequest): Observable<AccessTokenResponse>
}