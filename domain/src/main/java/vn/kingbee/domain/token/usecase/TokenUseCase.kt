package vn.kingbee.domain.token.usecase

import io.reactivex.Observable
import vn.kingbee.domain.entity.token.AccessTokenRequest
import vn.kingbee.domain.entity.token.AccessTokenResponse

interface TokenUseCase {
    fun getToken(request: AccessTokenRequest): Observable<AccessTokenResponse>

    fun revokeToken(request: AccessTokenRequest): Observable<AccessTokenResponse>
}