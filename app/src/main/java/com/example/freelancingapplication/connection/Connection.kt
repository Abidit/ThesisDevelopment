package connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

@Suppress("Deprecation")
class Connection(val context: Context) {
    fun getConnection():Boolean
    {
        var connection = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) as NetworkInfo
        val mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) as NetworkInfo
        println(connectivityManager.activeNetworkInfo)
        if((wifiConnection.isConnected) || (mobileConnection.isConnected))
        {
            connection = true
        }
        return connection
    }

    fun hasInternetConnected():Boolean
    {
        //"https://www.google.com"
        var connections:Boolean = false
        if(getConnection())
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val connection = java.net.URL("http://192.168.1.68:90/images/1618152892322lms profile.jpg").openConnection() as HttpURLConnection
                    connection.setRequestProperty("User-Agent","ConnectionTest")
                    connection.setRequestProperty("Connection","close")
                    connection.connectTimeout = 1000
                    connection.connect()
                    connections = connection.responseCode == 200
                }
                catch(ex:Exception)
                {
                    Log.e("classTag","${ex.toString()}")
                    println(ex.printStackTrace())
                }
            }

        }
        else
        {
            Log.w("classTag","No Network Available")
        }
        Thread.sleep(2000)
        return connections
    }


}