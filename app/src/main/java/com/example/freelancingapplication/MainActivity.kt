package com.example.freelancingapplication

import Fragments.*
import adapter.MenuBottomAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

var fragments:MutableList<Fragment> = mutableListOf(Home(),Profile(),Work(),Settings())
var fragId:MutableList<Int> = mutableListOf(R.id.ic_Home,R.id.ic_Profile,R.id.ic_Jobs,R.id.ic_Settings)


private lateinit var bottom : BottomNavigationView
private lateinit var pager : ViewPager

class MainActivity : AppCompatActivity() {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bottom = findViewById(R.id.btmNAV)
        pager = findViewById(R.id.view_pager)
        val home = Home()
        val profile = Profile()
        val settings = Settings()
        val work = Work()
        val menuadapter = MenuBottomAdapter(fragments,supportFragmentManager)
        pager.adapter = menuadapter
        var intents = intent.getIntExtra("fragNumber",-1)
        if(intents < 0)
        {

            bottom.menu.findItem(R.id.ic_Home).isChecked = true

        }
        else
        {
            makeCurrentFrag(fragments[intents])
            bottom.menu.findItem(fragId[intents]).isChecked = true
        }


        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_Home ->{

                    pager.currentItem=0
                    bottom.menu.findItem(R.id.ic_Home).isChecked = true
                }
                R.id.ic_Settings -> {
                    pager.currentItem=3
                    bottom.menu.findItem(R.id.ic_Settings).isChecked = true

                }
                R.id.ic_Profile -> {
                    pager.currentItem=1
                    bottom.menu.findItem(R.id.ic_Profile).isChecked = true

                }
                R.id.ic_Jobs -> {
                    pager.currentItem=2
                    bottom.menu.findItem(R.id.ic_Jobs).isChecked = true
                }
            }
            true
        }

        pager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {


                bottom.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }


    private fun makeCurrentFrag(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frames, fragment)
            commit()
        }


}