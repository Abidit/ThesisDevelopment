package response

import Model.Application


data class AppCreateResponse(
        val success : Boolean? = null,
        val data : Application? = null
)