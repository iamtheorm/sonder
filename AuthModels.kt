data class OtpRequestPayload(
    val fullName: String,
    val age: Int,
    val email: String,
    val institutionId: String,
    val userType: UserRole
)

data class OtpRequestResult(
    val otpRequestId: String,
    val message: String
)

data class OtpVerifyPayload(
    val otpRequestId: String,
    val otp: String
)

data class OtpVerifyResult(
    val accessToken: String,
    val isPremium: Boolean
)
