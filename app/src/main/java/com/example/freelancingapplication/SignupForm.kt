package com.example.freelancingapplication

import Database.UsersDB
import Model.User
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import repository.UserRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SignupForm : AppCompatActivity() {

    private lateinit var Fullname : EditText
    private lateinit var Username : EditText
    private lateinit var PasswordS : EditText
    private lateinit var CPassword : EditText
    private lateinit var btnRegister : Button
    private lateinit var btnlogg : TextView
    private lateinit var Imagee : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_form)

        Fullname = findViewById(R.id.etFullname)
        Username = findViewById(R.id.etUsernameS)
        PasswordS = findViewById(R.id.etPasswordS)
        CPassword = findViewById(R.id.etCpassword)
        btnRegister = findViewById(R.id.btnSignup)
        btnlogg = findViewById(R.id.txtlogg)
        Imagee = findViewById(R.id.imggg)

        btnlogg.setOnClickListener {
            startActivity(Intent(this, LoginForm::class.java))
        }


        btnRegister.setOnClickListener {

            if(validate()) {
                val fname = Fullname.text.toString()
                val usrnam = Username.text.toString()
                val passw = PasswordS.text.toString()
                val cpass = CPassword.text.toString()

                if (passw != cpass) {
                    CPassword.error = "Password does not match."
                    CPassword.requestFocus()
                    return@setOnClickListener
                } else {


                    val user = User(Fullname = fname, Email = "dummymail@mail.com", Username = usrnam, Password = passw, Usertype = "Applicant")
                    CoroutineScope(Dispatchers.IO).launch {
                        try {

                            val userRepository = UserRepository()
                            val response = userRepository.registerUser(user)
                            if(response.success == true){

                                if (imageUrl != null){
                                    uploadImage(response.data!!._id)
                                }
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@SignupForm,
                                        "Register Success", Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this@SignupForm,LoginForm::class.java))
                                }
                                val notificationManager = NotificationManagerCompat.from(this@SignupForm)

                                val notificationChannels = NotificationChannels(this@SignupForm)
                                notificationChannels.createNotificationChannel()

                                val notification = NotificationCompat.Builder(this@SignupForm, notificationChannels.channel1)
                                        .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                                        .setContentTitle("Authorized")
                                        .setContentText("You are Registered.")
                                        .setColor(Color.BLACK)
                                        .build()

                                notificationManager.notify(1, notification)
                            }
                        } catch (ex: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@SignupForm,
                                    ex.toString(), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        Imagee.setOnClickListener {
            loadPopup()
        }
    }

    private fun uploadImage(_id: String) {
        if (imageUrl != null) {
            //convert url to file or path
            val file = File(imageUrl!!)

            val extensions = MimeTypeMap.getFileExtensionFromUrl(imageUrl)
            val mimetype : String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensions)

            //create a req body and attach to file key
            val reqFile =
                    RequestBody.create(MediaType.parse(mimetype), file)

            val body =
                    MultipartBody.Part.createFormData("file", file.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userr = UserRepository()
                    val response = userr.uploadImage(_id, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignupForm, "Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                            startActivity(Intent(this@SignupForm,LoginForm::class.java))
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mero Error ",ex.toString())
                        Toast.makeText(
                                this@SignupForm,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                //overall location of selected image
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val contentResolver = contentResolver
                //locator and identifier
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])

                imageUrl = cursor.getString(columnIndex)

                //image preview
                Imagee.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                Imagee.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }

    private fun bitmapToFile(imageBitmap: Bitmap, s: String): File? {
        var file: File? = null
        return try {
            file = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .toString() + File.separator + s
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    private fun loadPopup() {
        val popupMenu = PopupMenu(this@SignupForm, Imagee)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }
    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }


    private fun validate(): Boolean {
        when {
            TextUtils.isEmpty(Fullname.text) -> {
                Fullname.error = "Fullname must not be empty"
                Fullname.requestFocus()
                return false
            }

            TextUtils.isEmpty(PasswordS.text) -> {
                PasswordS.error = "Password must not be empty"
                PasswordS.requestFocus()
                return false
            }
            TextUtils.isEmpty(CPassword.text) -> {
                CPassword.error = "Password must not be empty"
                CPassword.requestFocus()
                return false
            }
            TextUtils.isEmpty(Username.text) -> {
            Username.error = "Username must not be empty"
            Username.requestFocus()
            return false
        }
            (PasswordS.text.toString()) != (CPassword.text.toString())-> {
            CPassword.error = "Re-type password"
            CPassword.requestFocus()
            return false
        }

            else -> return true
        }
    }

}