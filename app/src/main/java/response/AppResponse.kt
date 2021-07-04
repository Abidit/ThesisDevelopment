package response

import Model.Application


data class AppResponse(
        val success : Boolean? = null,
        val data : MutableList<Application>? = null
)