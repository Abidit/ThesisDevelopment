package com.example.freelancingapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import api.ServiceBuilder
import connection.CheckConnection
import kotlinx.coroutines.*
import repository.UserRepository

class SplashScreen : AppCompatActivity() {
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        CheckConnection(this,this).checkRegisteredNetwork()


        val preffer = getSharedPreferences("Credential_Info", Context.MODE_PRIVATE)
        val username = preffer.getString("username","")
        val password = preffer.getString("password","")

        if (!hasPermission()) {
            requestPermission()
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)

            if(username !="" && password != "")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.checkUser(username!!,password!!)
                        if(response.success == true) {
                            ServiceBuilder.token = "Bearer " + response.token
                            ServiceBuilder.currentuser = response.data
                            ServiceBuilder.online = true
                            val intent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        {

                            withContext(Dispatchers.Main){
                                val intent = Intent(this@SplashScreen, LoginForm::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    catch (err:Exception)
                    {
                        val intent = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(intent)
                        ServiceBuilder.online = false
                        println("two")
                    }
                    finish()
                }
            }
            else
            {
                val intent = Intent(this@SplashScreen, LoginForm::class.java)
                startActivity(intent)
                finish()

            }
        }






    }

    override fun onDestroy() {
        super.onDestroy()
        CheckConnection(this,this).unregisteredNetwork()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissions, 1
        )
    }
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }
}


