package response

import Model.Job

data class JobUpdateResponse (
        val success: Boolean? = null,
        val data: Job? =null
        )


