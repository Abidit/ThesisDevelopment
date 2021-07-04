package connection

import android.app.Activity
import android.content.Context
import api.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.UserRepository

class AutoLogin(val activity : Activity) {
    fun userLogin()
    {
        var pref = activity.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        var username = pref.getString("username","")
        var password = pref.getString("password","")

        if(username != "" && password != "")
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.checkUser(username!!,password!!)
                    if(response.success == true)
                    {
                        ServiceBuilder.token = "Bearer "+response.token
                        ServiceBuilder.currentuser = response.data

                    }

                }

                catch (ex:Exception)
                {
                    println(ex.toString())
                }
            }
        }
    }

}