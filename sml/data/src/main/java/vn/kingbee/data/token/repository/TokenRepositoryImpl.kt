package vn.kingbee.data.token.repository

import io.reactivex.Observable
import vn.kingbee.data.base.BaseRepository
import vn.kingbee.data.token.service.TokenService
import vn.kingbee.domain.entity.token.AccessTokenRequest
import vn.kingbee.domain.entity.token.AccessTokenResponse
import vn.kingbee.domain.token.repository.TokenRepository

class TokenRepositoryImpl : BaseRepository, TokenRepository {

    private var service: TokenService

    constructor(service: TokenService) {
        this.service = service
    }

    override fun getToken(request: AccessTokenRequest): Observable<AccessTokenResponse> {
        return processRequest(service.getToken(request.authorization, request.grantType, request.scope))
    }

    override fun revokeToken(request: AccessTokenRequest): Observable<AccessTokenResponse> {
        return service.revokeToken(request.authorization, request.token)
    }
}