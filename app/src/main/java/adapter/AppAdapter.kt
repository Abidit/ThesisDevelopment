package adapter

import Fragments.Work
import Model.Application
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import api.ServiceBuilder
import com.example.freelancingapplication.MainActivity
import com.example.freelancingapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.AppRepository

class AppAdapter(
        var appList: MutableList<Application>,
        var context: Context?
) : RecyclerView.Adapter<AppAdapter.AdapterViewHolder>() {
    class AdapterViewHolder(view: View) :RecyclerView.ViewHolder(view){

        var jobname : TextView = view.findViewById(R.id.appJobName)
        var jobowner : TextView = view.findViewById(R.id.appJobOwner)
        var username : TextView = view.findViewById(R.id.appUserName)
        var email : TextView = view.findViewById(R.id.appEmail)
        var accepted : TextView = view.findViewById(R.id.acceptedtxt)
        var acceptbtn : Button = view.findViewById(R.id.acceptbtn)
        var cancelbtn : Button = view.findViewById(R.id.cancelbtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.applayout, parent, false)
        return AdapterViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        var app = appList[position]
        val idd = app._id
        val typee = ServiceBuilder.currentuser?.Usertype

        if(typee=="Admin"){
            holder.jobowner.visibility=View.VISIBLE
            holder.username.visibility=View.VISIBLE
            holder.email.visibility=View.VISIBLE
            holder.jobname.text = "Title: ${app.JobName}"
            holder.jobowner.text = "Posted By: ${app.JobOwner}"
            holder.username.text = "Application Name: ${app.UserName}"
            holder.email.text = "Applicant Email: ${app.Email}"
            holder.accepted.text = "Accepted: ${app.Accepted}"
        }
        if(typee=="Applicant"){
            holder.jobowner.visibility=View.VISIBLE
            holder.jobname.text = "Title: ${app.JobName}"
            holder.jobowner.text = "Posted By: ${app.JobOwner}"
            if(app.Accepted=="true"){
                holder.accepted.text = "Your Application is Accepted. Check Email for further details."
            }
            else{
                holder.accepted.text = "Pending : Application on request."

            }
            holder.cancelbtn.visibility = View.VISIBLE
            holder.cancelbtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                //set title for alert dialog
                builder.setTitle("Confirm Delete")
                builder.setMessage("Are you Sure you want to Cancel Application.")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            val appp = AppRepository()
                            val response = appp.deleteApp(idd)
                            if(response.success == true){
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                            context,
                                            "You have deleted Application.", Toast.LENGTH_SHORT
                                    ).show()
                                    appList.remove(app)
                                    notifyDataSetChanged()
                                }
                            }

                        }
                        catch (ex : Exception){
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                        context,
                                        ex.toString(), Toast.LENGTH_SHORT
                                ).show()
                                ex.printStackTrace()
                            }
                        }
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
        if(typee=="Job Owner") {
            holder.username.visibility=View.VISIBLE
            holder.email.visibility=View.VISIBLE

            holder.jobname.text = "Title: ${app.JobName}"
            holder.username.text = "Application Name: ${app.UserName}"
            holder.email.text = "Applicant Email: ${app.Email}"
            if(app.Accepted=="false"){
                holder.accepted.text = "Received Application"
                holder.acceptbtn.visibility = View.VISIBLE
                holder.acceptbtn.setOnClickListener {

                    val builder = AlertDialog.Builder(context)
                    //set title for alert dialog
                    builder.setTitle("Confirm Accept")
                    builder.setMessage("Are you Sure you want to Accept Application.")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Yes"){dialogInterface, which ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try{
                                val appp = AppRepository()
                                val response = appp.updateApp(idd, "true")
                                if (response.success==true){

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Updated!!", Toast.LENGTH_SHORT
                                        ).show()

                                        notifyDataSetChanged()

                                        var intent = Intent(context,MainActivity::class.java)
                                        intent.putExtra("fragNumber",2)
                                        context!!.startActivity(intent)

                                        
                                    }
                                }
                            }
                            catch (ex : Exception){
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
                    builder.setNeutralButton("Cancel"){dialogInterface , which ->
                        Toast.makeText(context,"clicked cancel",Toast.LENGTH_LONG).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()
                }
            }
            else{
                holder.accepted.text = "Application Already Approved."

            }

            }

    }

    override fun getItemCount(): Int {
        return appList.size
    }
}