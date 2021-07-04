package repository

import Model.Job
import api.JobApi
import api.MyApiRequest
import api.ServiceBuilder
import response.JobResponse
import response.JobUpdateResponse

class JobRepository : MyApiRequest() {
    val jbApi = ServiceBuilder.buildService(JobApi::class.java)

    suspend fun postJob(job: Job) : JobUpdateResponse {
        return apiRequest {
            jbApi.postJob(job,ServiceBuilder.token!!)
        }

    }

    suspend fun getAllJobs(): JobResponse {
        return apiRequest {
            jbApi.getAllJobs(ServiceBuilder.token!!)
        }
    }

suspend fun getoneJob(id: String): JobResponse {
        return apiRequest {
            jbApi.getoneJob(id,ServiceBuilder.token!!)
        }
    }

    suspend fun deleteone(id: String): JobResponse {
        return apiRequest {
            jbApi.deleteone(id,ServiceBuilder.token!!)
        }
    }

    suspend fun updateone(job: Job,id:String): JobUpdateResponse {
        return apiRequest {
            jbApi.updateone(job,id,ServiceBuilder.token!!)
        }
    }

}


