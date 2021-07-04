package com.example.freelancingapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import api.ServiceBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.UserRepository

class LoginForm : AppCompatActivity() {
    private lateinit var etusername : TextInputEditText
    private lateinit var etpassword : EditText
    private lateinit var loginn : Button
    private lateinit var signn : TextView
    private lateinit var linearlayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)
        etusername = findViewById(R.id.etUsername)
        etpassword = findViewById(R.id.etPassword)
        loginn = findViewById(R.id.btnLogin)
        signn = findViewById(R.id.txtSignup)
        linearlayout = findViewById(R.id.linear11)
        signn.setOnClickListener {
            val intent = Intent(this, SignupForm::class.java)
            startActivity(intent)
        }

        loginn.setOnClickListener {
            if (validate()) {
        login()
            }

            }
    }

    private fun login() {
        val username = etusername.text.toString()
        val password = etpassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.checkUser(username,password)
                if (response.success == true) {

                    ServiceBuilder.token = "Bearer " + response.token
                    ServiceBuilder.currentuser = response.data
                    val shared = getSharedPreferences("Credential_Info", MODE_PRIVATE)
                    val editior = shared.edit()
                    editior.putString("username", etusername.text.toString())
                    editior.putString("password", etpassword.text.toString())
                    editior.apply()
                    startActivity(
                        Intent(
                            this@LoginForm,
                            MainActivity::class.java
                        )
                    )
                    finish()
                    val notificationManager = NotificationManagerCompat.from(this@LoginForm)

                    val notificationChannels = NotificationChannels(this@LoginForm)
                    notificationChannels.createNotificationChannel()

                    val notification = NotificationCompat.Builder(this@LoginForm, notificationChannels.channel1)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                            .setContentTitle("Authorized")
                            .setContentText("You are logged in.")
                            .setColor(Color.BLACK)
                            .build()

                    notificationManager.notify(1, notification)
                } else {
                    withContext(Dispatchers.Main) {
                        val snack = Snackbar.make(
                                linearlayout, "Invalid credentials", Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK") {
                            snack.dismiss()
                        }
                        snack.show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginForm,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validate() : Boolean {
        return when {
            TextUtils.isEmpty(etusername.text) -> {
                etusername.error = "Username must not be empty"
                etusername.requestFocus()
                false
            }
            TextUtils.isEmpty(etpassword.text) -> {
                etpassword.error = "Password must not be empty"
                etpassword.requestFocus()
                false
            }

            else -> true
        }
    }
}
