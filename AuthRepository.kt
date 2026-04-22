import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiClient: AuthApiClient = AuthApiClient(),
    private val sessionManager: SessionManager? = null
) {
    suspend fun requestOtp(payload: OtpRequestPayload): Result<OtpRequestResult> {
        return withContext(Dispatchers.IO) {
            runCatching {
                apiClient.requestOtp(payload)
            }
        }
    }

    suspend fun verifyOtp(payload: OtpVerifyPayload): Result<OtpVerifyResult> {
        return withContext(Dispatchers.IO) {
            runCatching {
                apiClient.verifyOtp(payload)
            }
        }
    }

    fun saveSession(session: AuthSession) {
        sessionManager?.saveSession(session)
    }

    fun loadSession(): AuthSession? {
        return sessionManager?.loadSession()
    }

    fun clearSession() {
        sessionManager?.clearSession()
    }

    fun authorizationHeader(): String? {
        val token = sessionManager?.getAccessToken()
        return if (token.isNullOrBlank()) null else "Bearer $token"
    }
}
