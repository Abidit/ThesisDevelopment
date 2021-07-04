package adapter

import Model.Application
import Model.Job
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import api.ServiceBuilder
import com.example.freelancingapplication.MainActivity
import com.example.freelancingapplication.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import repository.AppRepository
import repository.JobRepository


class JobAdapter(
        var jobList: MutableList<Job>,
        var context: Context?
) : RecyclerView.Adapter<JobAdapter.AdapterViewHolder>()
{
    class AdapterViewHolder(view: View) :RecyclerView.ViewHolder(view){

        var jobTitle : TextView = view.findViewById(R.id.tvJobtitle)
        var description : TextView = view.findViewById(R.id.tvDescription)
        var skills : TextView = view.findViewById(R.id.tvSkills)
        var budgetTime : TextView = view.findViewById(R.id.tvBudgetTime)
        var posted : TextView = view.findViewById(R.id.tvPosted)
        var applybtn : Button = view.findViewById(R.id.applyBTN)
        var updatebtn : Button = view.findViewById(R.id.updateBTN)
        var deletebtn : Button = view.findViewById(R.id.deleteBTN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homelayout, parent, false)
        return AdapterViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val job = jobList[position]
        val idd = job._id
        val typee = ServiceBuilder.currentuser?.Usertype
        holder.jobTitle.text = "Title: ${job.JobTitle}"
        holder.description.text = "Description: ${job.Description}"
        holder.skills.text = "Skills: ${job.SkillsNeeded}"
        holder.budgetTime.text = "Budget: ${job.BudgetTime}"



        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.activity_update_job)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(true)

        val Title :EditText = dialog.findViewById(R.id.titleU)
        val Desc : EditText = dialog.findViewById(R.id.descU)
        val Skill : EditText = dialog.findViewById(R.id.skillsU)
        val Budget : EditText = dialog.findViewById(R.id.budgetU)
        val UPDATE : Button = dialog.findViewById(R.id.updatebtnU)
        Title.setText(job.JobTitle)
        Desc.setText(job.Description)
        Skill.setText(job.SkillsNeeded)
        Budget.setText(job.BudgetTime)

        UPDATE.setOnClickListener {
            val Ujob = Job(JobTitle = Title.text.toString(), Description = Desc.text.toString(), SkillsNeeded = Skill.text.toString(), BudgetTime = Budget.text.toString())

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = JobRepository()
                    val response = repository.updateone(Ujob, idd!!)
                    if(response.success ==true){

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                    context,
                                    "You have Updated the job.", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                catch (ex: Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                                context,
                                ex.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            dialog.dismiss()
            notifyDataSetChanged()
            val i = Intent(context, MainActivity()::class.java)
            context!!.startActivity(i)
        }

        if(typee=="Job Owner") {
            if(job.JobOwner==ServiceBuilder.currentuser?.Fullname) {


                holder.updatebtn.visibility = View.VISIBLE
                holder.deletebtn.visibility = View.VISIBLE

                holder.updatebtn.setOnClickListener {
                    dialog.show()
                }


                holder.deletebtn.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    //set title for alert dialog
                    builder.setTitle("Confirm Delete")
                    builder.setMessage("Are you Sure you want to Delete Job.")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Yes") { dialogInterface, which ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val repository = JobRepository()
                                val response = idd?.let { it1 -> repository.deleteone(it1) }
                                if (response?.success == true) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                                context,
                                                "You have deleted the job.", Toast.LENGTH_SHORT
                                        ).show()
                                        jobList.remove(job)
                                        notifyDataSetChanged()
                                    }
                                }

                            } catch (ex: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                            context,
                                            ex.toString(), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                    //performing cancel action
                    builder.setNeutralButton("Cancel") { dialogInterface, which ->
                        Toast.makeText(context, "clicked cancel", Toast.LENGTH_LONG).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()
                }
            }else{
                holder.posted.text = "Posted By: ${job.JobOwner}"
                holder.updatebtn.visibility = View.INVISIBLE
                holder.deletebtn.visibility = View.INVISIBLE

            }

        }
        if(typee=="Applicant"){
            holder.posted.text = "Posted By: ${job.JobOwner}"
            holder.applybtn.visibility = View.VISIBLE

            holder.applybtn.setOnClickListener {

                val application = Application(JobName = job.JobTitle,
                        JobOwner = job.JobOwner, UserName = ServiceBuilder.currentuser!!.Fullname,
                        Email = ServiceBuilder.currentuser!!.Email)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val appRepo = AppRepository()
                        val response = appRepo.getApplicationAT(ServiceBuilder.currentuser?.Fullname!!, job.JobTitle!!)
                        if (response.data == null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val appRepo = AppRepository()
                                    val response = appRepo.createApplication(application)
                                    if(response.success == true)
                                    {
                                        withContext(Dispatchers.Main)
                                        {
                                            println(response)
                                            Toast.makeText(context, "Applied Successfully", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                }
                                catch (ex:Exception)
                                {
                                    withContext(Dispatchers.Main)
                                    {
                                        Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                                        println(ex.printStackTrace())
                                    }
                                }
                            }


                        }
                        if(response.data!=null){
                            withContext(Dispatchers.Main)
                            {
                                Toast.makeText(context, "You have already applied to this Job", Toast.LENGTH_SHORT).show()

                            }

                        }
                    }
                catch (ex:Exception){
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                        println(ex.printStackTrace())
                    }
                }}


            }

            }

        }

    override fun getItemCount(): Int {
        return  jobList.size
    }

}