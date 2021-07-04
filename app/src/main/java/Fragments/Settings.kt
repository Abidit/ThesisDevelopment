package Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import api.ServiceBuilder
import com.example.freelancingapplication.FreelanceMap
import com.example.freelancingapplication.LoginForm
import com.example.freelancingapplication.NotificationChannels
import com.example.freelancingapplication.R


class Settings : Fragment() {

    private lateinit var btnMap : Button
    private lateinit var btnLogout : Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        btnMap = view.findViewById(R.id.map_btn)
        btnLogout = view.findViewById(R.id.logoutbtn)
        btnMap.setOnClickListener {
            startActivity(Intent(context, FreelanceMap::class.java))
        }
        btnLogout.setOnClickListener {
            logoutUser()
        }
        return view;
    }

    private fun logoutUser() {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you Sure you want to logout.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            ServiceBuilder.token = null
            ServiceBuilder.currentuser = null

            val prefs = activity?.getSharedPreferences("Credential_Info", Context.MODE_PRIVATE);
            val editor = prefs?.edit()
            if (editor != null) {
                editor.clear()
                editor.apply()
            }
            val intent = Intent(context, LoginForm::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            activity?.startActivity(intent);
            activity?.finish();
            Toast.makeText(context,"You have been logged out.",Toast.LENGTH_LONG).show()

            val notificationChannels = context?.let { NotificationChannels(it) }
            notificationChannels?.createNotificationChannel()

            val notification = context?.let {
                notificationChannels?.let { it1 ->
                    NotificationCompat.Builder(it, it1.channel1)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                            .setContentTitle("Logout")
                            .setContentText("You are logged out. Please Login")
                            .setColor(Color.BLACK)
                            .build()
                }
            }

            if (notification != null) {
                context?.let { NotificationManagerCompat.from(it) }?.notify(1, notification)
            }

        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(context,"clicked cancel",Toast.LENGTH_LONG).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()

    }

}