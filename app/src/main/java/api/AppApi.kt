package api

import Model.Application
import response.AppCreateResponse
import response.AppDelResponse
import response.AppResponse
import retrofit2.Response
import retrofit2.http.*

interface AppApi {
    //Create Application

    @POST("/application/insert")
    suspend fun createApplication(@Body application: Application,
    @Header("Authorization") token: String)
    : retrofit2.Response<AppCreateResponse>

    //Get Application
    @GET("/application/all")
    suspend fun getApplication(
            @Header("Authorization") token: String
    ) : retrofit2.Response<AppResponse>

    //Get Application owner
    @GET("/application/owner/{name}")
    suspend fun getApplicationO(
            @Path("name") name: String,
            @Header("Authorization") token: String
    ) : retrofit2.Response<AppResponse>

    //Get Application Applicant
    @GET("/application/apply/{name}")
    suspend fun getApplicationA(
            @Path("name") name: String,
            @Header("Authorization") token: String
    ) : retrofit2.Response<AppResponse>

    //Get Applied Applicants Application
    @GET("/application/get/{name}/{title}")
    suspend fun getApplicationApplied(
        @Path("name") name: String,
        @Path("title") title: String,
        @Header("Authorization") token: String
    ) : retrofit2.Response<AppCreateResponse>

    @DELETE("/application/delete/{id}")
    suspend fun deleteApp(
            @Path("id") id: String,
            @Header("Authorization") token: String
    ) : retrofit2.Response<AppDelResponse>


    @FormUrlEncoded
    @PUT("/application/update/{id}")
    suspend fun updateApp(
            @Path("id") id: String,
            @Field("Accepted") Accepted : String,
            @Header("Authorization") token: String
    ) : retrofit2.Response<AppCreateResponse>
}