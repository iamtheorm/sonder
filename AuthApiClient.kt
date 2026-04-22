import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class AuthApiClient(
    private val baseUrl: String = AuthConfig.BASE_URL
) {
    fun requestOtp(payload: OtpRequestPayload): OtpRequestResult {
        val response = postJson(
            endpoint = "/api/v1/auth/request-otp",
            body = JSONObject()
                .put("fullName", payload.fullName)
                .put("age", payload.age)
                .put("email", payload.email)
                .put("institutionId", payload.institutionId)
                .put("userType", payload.userType.name),
            bearerToken = null
        )

        val body = JSONObject(response)
        return OtpRequestResult(
            otpRequestId = body.optString("otpRequestId", body.optString("requestId")),
            message = body.optString("message", "OTP sent successfully.")
        )
    }

    fun verifyOtp(payload: OtpVerifyPayload): OtpVerifyResult {
        val response = postJson(
            endpoint = "/api/v1/auth/verify-otp",
            body = JSONObject()
                .put("otpRequestId", payload.otpRequestId)
                .put("otp", payload.otp),
            bearerToken = null
        )

        val body = JSONObject(response)
        return OtpVerifyResult(
            accessToken = body.optString("accessToken", body.optString("token")),
            isPremium = body.optBoolean("isPremium", false)
        )
    }

    private fun postJson(endpoint: String, body: JSONObject, bearerToken: String?): String {
        val url = URL(baseUrl.trimEnd('/') + endpoint)
        val connection = createConnection(url = url, method = "POST", bearerToken = bearerToken).apply {
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
        }

        return try {
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(body.toString())
                writer.flush()
            }

            val stream = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream ?: connection.inputStream
            }

            val payloadText = BufferedReader(stream.reader()).use { it.readText() }
            if (connection.responseCode !in 200..299) {
                val errorMessage = runCatching {
                    JSONObject(payloadText).optString("message")
                }.getOrDefault("")
                val fallback = if (errorMessage.isBlank()) payloadText else errorMessage
                throw IllegalStateException(
                    if (fallback.isBlank()) "Request failed (${connection.responseCode})."
                    else fallback
                )
            }
            payloadText
        } finally {
            connection.disconnect()
        }
    }

    private fun createConnection(
        url: URL,
        method: String,
        bearerToken: String?
    ): HttpURLConnection {
        return (url.openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15_000
            readTimeout = 15_000
            doInput = true
            setRequestProperty("Accept", "application/json")
            if (!bearerToken.isNullOrBlank()) {
                setRequestProperty("Authorization", "Bearer $bearerToken")
            }
        }
    }
}
