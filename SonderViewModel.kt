import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserRole {
    SCHOOL,
    COLLEGE
}

class SonderViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _userType = MutableStateFlow(UserRole.SCHOOL)
    val userType = _userType.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()

    private val _institutionId = MutableStateFlow("")
    val institutionId = _institutionId.asStateFlow()

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

    fun setUserType(value: UserRole) {
        _userType.value = value
    }

    fun setPremium(value: Boolean) {
        _isPremium.value = value
    }

    fun setInstitutionId(value: String) {
        _institutionId.value = value
    }
}
