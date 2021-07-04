package Fragments

import Database.UsersDB
import Model.Job
import adapter.JobAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.ServiceBuilder
import com.example.freelancingapplication.PostJob
import com.example.freelancingapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.JobRepository


class Home : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addjob: Button
    private lateinit var listt: MutableList<Job>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        addjob = view.findViewById(R.id.addBtn)
        if(ServiceBuilder.online==false){
            addjob.visibility = View.VISIBLE
            addjob.setOnClickListener {
                Toast.makeText(context, "You're Offline.Connect to internet", Toast.LENGTH_SHORT).show()
            }
        }

        loadjobs()

        val usertype = ServiceBuilder.currentuser?.Usertype
        if (usertype=="Job Owner"){
            addjob.visibility = View.VISIBLE
            addjob.setOnClickListener {
                if (container != null) {
                    startActivity(Intent(container.context, PostJob::class.java))
                }
            }
        }


        return view

    }

    private fun loadjobs() {
        if (ServiceBuilder.online == true){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val jobbb = JobRepository()
                    val response = jobbb.getAllJobs()
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            // Put all the student details in lstStudents
                            val lstStudents = response.data

                            context?.let { UsersDB.getInstance(it).getJobDAO().deleteJobs() }
                            context?.let { UsersDB.getInstance(it).getJobDAO().insertJob(lstStudents!!) }
                            listt = UsersDB.getInstance(requireContext()).getJobDAO().getJobs()
                            activity?.runOnUiThread {
                                //Code for the UiThread
                                recyclerView.adapter = JobAdapter(listt, requireContext())
                                recyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        println(ex.printStackTrace())
                        println("four")
                        Toast.makeText(context,
                                "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                val instance = UsersDB.getInstance(requireContext())
                listt = instance.getJobDAO().getJobs()
                withContext(Dispatchers.Main){
                    recyclerView.adapter=JobAdapter(listt,requireContext())
                    recyclerView.layoutManager=LinearLayoutManager(requireContext())
                }
            }
        }


    }
}