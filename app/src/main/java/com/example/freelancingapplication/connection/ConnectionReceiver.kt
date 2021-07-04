package connection

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import api.ServiceBuilder

class ConnectionReceiver(val activity: Activity) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {
        val conn = Connection(context)
        val userData = AutoLogin(activity)
        if(conn.getConnection())
        {
            ServiceBuilder.online = true
            userData.userLogin()
            Toast.makeText(context, "You are online!!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            ServiceBuilder.online = false
            Toast.makeText(context, "You are offline!!", Toast.LENGTH_SHORT).show()

        }
    }
}