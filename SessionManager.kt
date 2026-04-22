import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class AuthSession(
    val accessToken: String,
    val isPremium: Boolean,
    val email: String,
    val userType: UserRole,
    val institutionId: String
)

class SessionManager(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "sonder_secure_session",
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(session: AuthSession) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, session.accessToken)
            .putBoolean(KEY_IS_PREMIUM, session.isPremium)
            .putString(KEY_EMAIL, session.email)
            .putString(KEY_USER_TYPE, session.userType.name)
            .putString(KEY_INSTITUTION_ID, session.institutionId)
            .apply()
    }

    fun loadSession(): AuthSession? {
        val token = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""
        if (token.isBlank()) return null

        val isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false)
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val userType = runCatching {
            UserRole.valueOf(prefs.getString(KEY_USER_TYPE, UserRole.SCHOOL.name) ?: UserRole.SCHOOL.name)
        }.getOrDefault(UserRole.SCHOOL)
        val institutionId = prefs.getString(KEY_INSTITUTION_ID, "") ?: ""

        return AuthSession(
            accessToken = token,
            isPremium = isPremium,
            email = email,
            userType = userType,
            institutionId = institutionId
        )
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun getAccessToken(): String? {
        val token = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""
        return token.ifBlank { null }
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val KEY_IS_PREMIUM = "key_is_premium"
        const val KEY_EMAIL = "key_email"
        const val KEY_USER_TYPE = "key_user_type"
        const val KEY_INSTITUTION_ID = "key_institution_id"
    }
}
