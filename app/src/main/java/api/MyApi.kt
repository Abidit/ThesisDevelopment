
package api

import Model.User
import okhttp3.MultipartBody
import response.ImageResponse
import response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface MyApi {
    // Register User
    @POST("/user/insert")
    suspend fun registerUser(@Body user: User):retrofit2.Response<LoginResponse>


    @FormUrlEncoded
    @POST("user/login")
    suspend fun checkUser(
        @Field("Username")username:String,
        @Field("Password")password:String
    ): Response<LoginResponse>


    @Multipart
    @PUT("/photo/{id}")
    suspend fun uploadImage(
            @Path("id") id: String,
            @Part file: MultipartBody.Part
    ): Response<ImageResponse>
}