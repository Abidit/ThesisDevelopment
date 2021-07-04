package response

import Model.User

data class LoginResponse(
    val success: Boolean? = null,
    val token: String? = null,
    val data : User? = null
)
