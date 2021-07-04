package api

import Model.Job
import response.JobResponse
import response.JobUpdateResponse
import retrofit2.Response
import retrofit2.http.*

interface JobApi {

    //Post a Job
    @POST("/jobs/insert")
    suspend fun postJob(@Body job : Job,
                        @Header("Authorization") token: String
    ) : retrofit2.Response<JobUpdateResponse>

    @GET("/jobs/all")
    suspend fun getAllJobs(
            @Header("Authorization") token: String
    ): retrofit2.Response<JobResponse>

    @GET("/job/one/{id}")
    suspend fun getoneJob(
            @Path("id") id: String,
            @Header("Authorization") token: String
    ): retrofit2.Response<JobResponse>

    @DELETE("/job/delete/{id}")
    suspend fun deleteone(
            @Path("id") id: String,
            @Header("Authorization") token: String
    ): retrofit2.Response<JobResponse>

    @PUT("/job/update/{id}")
    suspend fun updateone(
            @Body job: Job,
            @Path("id") id: String,
            @Header("Authorization") token: String
    ): retrofit2.Response<JobUpdateResponse>
}