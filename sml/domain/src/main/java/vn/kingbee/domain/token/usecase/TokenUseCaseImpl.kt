package vn.kingbee.domain.token.usecase

import io.reactivex.Observable
import vn.kingbee.domain.entity.token.AccessTokenRequest
import vn.kingbee.domain.entity.token.AccessTokenResponse
import vn.kingbee.domain.token.repository.TokenRepository

class TokenUseCaseImpl : TokenUseCase {
    private var repository: TokenRepository

    constructor(repository: TokenRepository) {
        this.repository = repository
    }

    override fun getToken(request: AccessTokenRequest): Observable<AccessTokenResponse> {
        return repository.getToken(request)
    }

    override fun revokeToken(request: AccessTokenRequest): Observable<AccessTokenResponse> {
        return repository.revokeToken(request)
    }
}