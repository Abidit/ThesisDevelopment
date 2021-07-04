package com.example.freelancingapplication

import Fragments.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

var fragments:MutableList<Fragment> = mutableListOf(Home(),Profile(),Work(),Settings())
var fragId:MutableList<Int> = mutableListOf(R.id.ic_Home,R.id.ic_Profile,R.id.ic_Jobs,R.id.ic_Settings)

private lateinit var sensorManager: SensorManager
private var proximitysensor : Sensor? = null
private var lightsensor : Sensor? = null
private var accelerometersensor : Sensor? = null
private lateinit var bottom : BottomNavigationView

class MainActivity : AppCompatActivity(), SensorEventListener {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if(!checkSensor())
            return
        else{
            lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            proximitysensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            accelerometersensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, proximitysensor,SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, lightsensor,SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, accelerometersensor,SensorManager.SENSOR_DELAY_NORMAL)
        }

        bottom = findViewById(R.id.btmNAV)
        val home = Home()
        val profile = Profile()
        val settings = Settings()
        val work = Work()

        var intents = intent.getIntExtra("fragNumber",-1)
        if(intents < 0)
        {
            makeCurrentFrag(home)
            bottom.menu.findItem(R.id.ic_Home).isChecked = true

        }
        else
        {
            makeCurrentFrag(fragments[intents])
            bottom.menu.findItem(fragId[intents]).isChecked = true
        }


        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_Home -> makeCurrentFrag(home)
                R.id.ic_Settings -> makeCurrentFrag(settings)
                R.id.ic_Profile -> makeCurrentFrag(profile)
                R.id.ic_Jobs -> makeCurrentFrag(work)
            }
            true
        }
    }

    private fun checkSensor(): Boolean {
        var flag =true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null){
            flag = false
        }
        return flag

    }

    private fun makeCurrentFrag(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frames, fragment)
            commit()
        }

    override fun onSensorChanged(event: SensorEvent?) {


        if(event!!.sensor.type == Sensor.TYPE_PROXIMITY)
        {
            val values = event.values[0]
            if(values < 4)
            {
                val notificationChannels = NotificationChannels(this)
                notificationChannels.createNotificationChannel()
                val notification = this.let {
                    notificationChannels.let { it1 ->
                        NotificationCompat.Builder(it, it1.channel1)
                                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                                .setContentText("Object Collision Threat Detected.")
                                .setColor(Color.BLACK)
                                .build()
                    }
                }

                if (notification != null) {
                    this.let { NotificationManagerCompat.from(it) }.notify(1, notification)
                }
            }
        }
        if(event.sensor.type == Sensor.TYPE_LIGHT)
        {
            val values = event.values[0]
            if(values > 20000)
            {
                val notificationChannels = NotificationChannels(this)
                notificationChannels.createNotificationChannel()
                val notification = this.let {
                    notificationChannels.let { it1 ->
                        NotificationCompat.Builder(it, it1.channel1)
                                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                                .setContentText("High Amount of light Detected.")
                                .setColor(Color.BLACK)
                                .build()
                    }
                }

                if (notification != null) {
                    this.let { NotificationManagerCompat.from(it) }.notify(1, notification)
                }
            }
        }
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val values = event.values
            val xAxis = values[0]

            if(xAxis<(-7)) {
                makeCurrentFrag(Settings())
                bottom.menu.findItem(fragId[3]).isChecked = true

            }
            if (xAxis>(7)){
                    makeCurrentFrag(Profile())
                bottom.menu.findItem(fragId[1]).isChecked = true

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}