package Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import api.ServiceBuilder
import com.bumptech.glide.Glide
import com.example.freelancingapplication.R

class Profile : Fragment() {

    private lateinit var image : ImageView
    private lateinit var name : TextView
    private lateinit var email : TextView
    private lateinit var type : TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        image = view.findViewById(R.id.currentimage)
        name = view.findViewById(R.id.currentname)
        email = view.findViewById(R.id.currentemail)
        type = view.findViewById(R.id.textViewE)

        setProfile()
        return view
    }
    @SuppressLint("SetTextI18n")
    private fun setProfile() {
        name.text = "Name : ${ServiceBuilder.currentuser?.Fullname.toString()}"
        email.text = "Email: ${ServiceBuilder.currentuser?.Email.toString()}"

    }
}