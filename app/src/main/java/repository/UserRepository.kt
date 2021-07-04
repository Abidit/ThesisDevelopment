package repository

import Model.User
import api.MyApi
import api.MyApiRequest
import api.ServiceBuilder
import okhttp3.MultipartBody
import response.ImageResponse
import response.LoginResponse


class UserRepository: MyApiRequest() {
    val myApi= ServiceBuilder.buildService(MyApi::class.java)
    suspend fun registerUser(user: User):LoginResponse{
        return apiRequest {
            myApi.registerUser(user)
        }
    }
    suspend fun checkUser(username:String,password:String):LoginResponse{
        return apiRequest {
            myApi.checkUser(username, password)
        }
    }

    suspend fun uploadImage(id: String, body: MultipartBody.Part)
            : ImageResponse {
        return apiRequest {
            myApi.uploadImage( id, body)
        }
    }
}

