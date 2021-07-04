package response

import Model.Job

data class JobResponse (
    val success: Boolean? = null,
    val data: MutableList<Job>? =null
)