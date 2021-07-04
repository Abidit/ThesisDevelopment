package repository

import Model.Application
import Model.Job
import api.AppApi
import api.MyApiRequest
import api.ServiceBuilder
import response.AppCreateResponse
import response.AppDelResponse
import response.AppResponse
import response.JobResponse

class AppRepository : MyApiRequest() {
    val AApi = ServiceBuilder.buildService(AppApi::class.java)

    suspend fun createApplication(app: Application) : AppCreateResponse {
        return apiRequest {
            AApi.createApplication(app, ServiceBuilder.token!!)
        }
    }

    suspend fun getApplication(): AppResponse {
        return apiRequest {
            AApi.getApplication(ServiceBuilder.token!!)
        }
    }

    suspend fun getApplicationO(name:String): AppResponse {
        return apiRequest {
            AApi.getApplicationO(name,ServiceBuilder.token!!)
        }
    }

    suspend fun getApplicationA(name:String): AppResponse {
        return apiRequest {
            AApi.getApplicationA(name,ServiceBuilder.token!!)
        }
    }

    suspend fun getApplicationAT(name:String, title : String): AppCreateResponse {
        return apiRequest {
            AApi.getApplicationApplied(name, title,ServiceBuilder.token!!)
        }
    }
    suspend fun deleteApp(id:String): AppDelResponse {
        return apiRequest {
            AApi.deleteApp(id,ServiceBuilder.token!!)
        }
    }
    suspend fun updateApp(id:String,Accepted:String): AppCreateResponse {
        return apiRequest {
            AApi.updateApp(id,Accepted,ServiceBuilder.token!!)
        }
    }
}
