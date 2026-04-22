package com.iamtheorm.sonder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class UserRole {
    SCHOOL,
    COLLEGE
}

class SonderViewModel(application: Application) : AndroidViewModel(application) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _userType = MutableStateFlow(UserRole.SCHOOL)
    val userType = _userType.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()

    private val _institutionId = MutableStateFlow("")
    val institutionId = _institutionId.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _otpRequestId = MutableStateFlow("")
    val otpRequestId: StateFlow<String> = _otpRequestId.asStateFlow()

    private val _isSessionReady = MutableStateFlow(false)
    val isSessionReady: StateFlow<Boolean> = _isSessionReady.asStateFlow()

    private val repository = AuthRepository(sessionManager = SessionManager(application.applicationContext))

    init {
        restoreSession()
    }

    fun setUserType(value: UserRole) {
        _userType.value = value
    }

    fun setInstitutionId(value: String) {
        _institutionId.value = value
    }

    fun setEmail(value: String) {
        _email.value = value
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        repository.clearSession()
        _isLoggedIn.value = false
        _isPremium.value = false
        _otpRequestId.value = ""
    }

    fun requestOtp(fullName: String, age: String, onSuccess: () -> Unit) {
        val ageValue = age.toIntOrNull()
        if (_email.value.isBlank()) {
            _errorMessage.value = "Email is required."
            return
        }
        if (_institutionId.value.isBlank()) {
            _errorMessage.value = "Institution ID is required."
            return
        }
        if (fullName.isBlank()) {
            _errorMessage.value = "Full name is required."
            return
        }
        if (ageValue == null || ageValue <= 0) {
            _errorMessage.value = "Enter a valid age."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.requestOtp(
                OtpRequestPayload(
                    fullName = fullName,
                    age = ageValue,
                    email = _email.value.trim(),
                    institutionId = _institutionId.value.trim(),
                    userType = _userType.value
                )
            )
            _isLoading.value = false
            result.onSuccess {
                _otpRequestId.value = it.otpRequestId
                onSuccess()
            }.onFailure {
                _errorMessage.value = it.message ?: "Could not request OTP."
            }
        }
    }

    fun verifyOtp(otp: String, onSuccess: () -> Unit) {
        if (otp.length != 4) {
            _errorMessage.value = "Enter the 4-digit OTP."
            return
        }
        if (_otpRequestId.value.isBlank()) {
            _errorMessage.value = "OTP session missing. Please request OTP again."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.verifyOtp(
                OtpVerifyPayload(otpRequestId = _otpRequestId.value, otp = otp)
            )
            _isLoading.value = false
            result.onSuccess {
                if (it.accessToken.isNotBlank()) {
                    repository.saveSession(
                        AuthSession(
                            accessToken = it.accessToken,
                            isPremium = it.isPremium,
                            email = _email.value.trim(),
                            userType = _userType.value,
                            institutionId = _institutionId.value.trim()
                        )
                    )
                }
                _isPremium.value = it.isPremium
                _isLoggedIn.value = true
                onSuccess()
            }.onFailure {
                _errorMessage.value = it.message ?: "Could not verify OTP."
            }
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val session = repository.loadSession()
            if (session != null && session.accessToken.isNotBlank()) {
                _isLoggedIn.value = true
                _isPremium.value = session.isPremium
                _email.value = session.email
                _userType.value = session.userType
                _institutionId.value = session.institutionId
            }
            _isSessionReady.value = true
        }
    }
}
