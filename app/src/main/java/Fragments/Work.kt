package Fragments

import Model.Application
import adapter.AppAdapter
import adapter.JobAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.ServiceBuilder
import com.example.freelancingapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.AppRepository
import java.lang.Exception

class Work : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var AppHeading: TextView
    private lateinit var listtapp: MutableList<Application>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_work, container, false)
        recyclerView = view.findViewById(R.id.apprecycler)
        AppHeading= view.findViewById(R.id.AppHeading)
        load(container)

        return view
    }

    private fun load(container: ViewGroup?) {

        val type = ServiceBuilder.currentuser!!.Usertype
        val name = ServiceBuilder.currentuser!!.Fullname
        when (type) {
            "Applicant" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val apps = AppRepository()
                        val response = apps.getApplicationA(name!!)
                        if(response.success==true){
                            listtapp = response.data!!

                        }
                        if(response.data==null){
                            AppHeading.text = "No Applications"
                        }

                        withContext(Dispatchers.Main) {
                            activity?.runOnUiThread {
                                //Code for the UiThread
                                recyclerView.adapter = AppAdapter(listtapp, requireContext())
                                recyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        }
                    } catch (ex:Exception){
                        withContext(Dispatchers.Main) {
                            println(ex.printStackTrace())
                            Toast.makeText(context,
                                    "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            "Job Owner" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val apps = AppRepository()
                        val response = apps.getApplicationO(name!!)
                        if(response.success==true){
                            listtapp = response.data!!

                        }
                        if(response.data==null){
                            AppHeading.text = "No Applications"
                        }

                        withContext(Dispatchers.Main) {
                            activity?.runOnUiThread {
                                //Code for the UiThread
                                recyclerView.adapter = AppAdapter(listtapp, requireContext())
                                recyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        }
                    } catch (ex:Exception){
                        withContext(Dispatchers.Main) {
                            println(ex.printStackTrace())
                            Toast.makeText(context,
                                    ex.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val apps = AppRepository()
                        val response = apps.getApplication()
                        if (response.success == true) {
                            listtapp = response.data!!

                        }
                        if (response.data == null) {
                            AppHeading.text = "No Applications"
                        }

                        withContext(Dispatchers.Main) {
                            activity?.runOnUiThread {
                                //Code for the UiThread
                                recyclerView.adapter = AppAdapter(listtapp, requireContext())
                                recyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            println(ex.printStackTrace())
                            Toast.makeText(context,
                                    "Error : ${ex.toString()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}