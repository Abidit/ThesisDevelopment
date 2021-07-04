package com.example.freelancingapplication

import Model.Job
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import api.ServiceBuilder
import kotlinx.coroutines.*
import repository.JobRepository
import java.lang.Exception

class PostJob : AppCompatActivity() {
    private lateinit var JobTitle : EditText
    private lateinit var Description : EditText
    private lateinit var SkillsNeeded : EditText
    private lateinit var BudgetTime : EditText
    private lateinit var POST : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)

        JobTitle = findViewById(R.id.titleJ)
        Description = findViewById(R.id.descJ)
        SkillsNeeded = findViewById(R.id.skillsJ)
        BudgetTime = findViewById(R.id.budgetJ)
        POST = findViewById(R.id.postbtnJ)
        val idd = ServiceBuilder.currentuser?._id
        val namee = ServiceBuilder.currentuser?.Fullname

    POST.setOnClickListener {
        if (validate()){
        val add = Job(JobTitle = JobTitle.text.toString(),Description = Description.text.toString()
                ,BudgetTime = BudgetTime.text.toString(),SkillsNeeded = SkillsNeeded.text.toString(), _id = idd,JobOwner = namee)

        CoroutineScope(Dispatchers.IO).launch {
            try{

                val job = JobRepository()
                val response = job.postJob(add)
                if(response.success==true){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@PostJob, "New job Added", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@PostJob, MainActivity()::class.java))
                        finish()
                    }

                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@PostJob, "Error Posting", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@PostJob, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

    }

    private fun validate(): Boolean {
        when {
            TextUtils.isEmpty(JobTitle.text) -> {
                JobTitle.error = "Jobtitle must not be empty"
                JobTitle.requestFocus()
                return false
            }
            TextUtils.isEmpty(Description.text) -> {
                Description.error = "Description must not be empty"
                Description.requestFocus()
                return false
            }
            TextUtils.isEmpty(SkillsNeeded.text) -> {
                SkillsNeeded.error = "SkillsNeeded must not be empty"
                SkillsNeeded.requestFocus()
                return false
            }

            TextUtils.isEmpty(BudgetTime.text) -> {
                BudgetTime.error = "BudgetTime must not be empty"
                BudgetTime.requestFocus()
                return false
            }
            else -> return true
        }
    }
}