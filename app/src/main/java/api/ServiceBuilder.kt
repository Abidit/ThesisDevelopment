package api

import Model.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    const val BASE_URL = "http://10.0.2.2:3000/"
    // const val BASE_URL = "http://localhost:3000/"
    // const val BASE_URL = "http://192.168.1.16:3000/"
    private val okHttp= OkHttpClient.Builder()
    var token:String?=null
    var online : Boolean? = null
    var currentuser : User? = null
    private val retrofitBuilder=
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())

    private val retrofit= retrofitBuilder.build()
    //Generic Class
    fun <T> buildService(serviceType:Class<T>):T{
        return retrofit.create(serviceType)
    }
}