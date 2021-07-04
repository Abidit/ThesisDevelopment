package response

import Model.User


data class ImageResponse(
        val success : Boolean? = null,
        val data : User? = null
)
