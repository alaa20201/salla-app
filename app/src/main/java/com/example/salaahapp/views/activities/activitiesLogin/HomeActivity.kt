package com.example.salaahapp.views.activities.activitiesLogin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CalendarView
import com.example.salaahapp.R

import kotlinx.android.synthetic.main.activity_home.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.example.salaahapp.database.MyAppDatabase
import com.example.salaahapp.network.SallaApi
import com.example.salaahapp.views.fragments.CalendarFragment
import com.example.salaahapp.views.fragments.QuranFragment
import com.example.salaahapp.views.fragments.VerseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val REQUEST_MULTIPLE = 101
    lateinit var toolbar: Toolbar
    lateinit var navViewBttom: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var auth : FirebaseAuth
    lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.sallaApi = SallaApi.createService()
        homeViewModel.getSallahData()

        setupToolbar()
        setBottomNavigation()
        onItemSelectedColorChanged()
        text_view_title.text = "Calendar"
        auth = FirebaseAuth.getInstance()

        navViewBttom.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.container_fragment_home, CalendarFragment()).commit()
        setBottomNavigation()
        setUpMultiplePermission()
    }
    private fun setUpMultiplePermission(){
        val appPermission = arrayOf(
            Manifest.permission.SET_ALARM,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        var listPermission :ArrayList<String> = ArrayList()
        for(permission in appPermission){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                listPermission.add(permission)
            }
        }
        requestMultiplePermission()
    }
    private fun requestMultiplePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.SET_ALARM,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),REQUEST_MULTIPLE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_MULTIPLE ->{
//                Toast.makeText(applicationContext, "MULTIPLE ACCESS GRANTED", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupToolbar() {
        toolbar = findViewById(R.id.tool_bar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }
    private fun onItemSelectedColorChanged(){
        var colorStateList : ColorStateList = resources.getColorStateList(R.color.navigation_item_color)
        //navigationView.setItemTextColor(csl)
        nav_bottom_view.itemIconTintList = colorStateList
        nav_bottom_view.itemTextColor=colorStateList
    }

    private fun setBottomNavigation() {
        navViewBttom = findViewById(R.id.nav_bottom_view)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                //startActivity(Intent(this, MyAccountActivity::class.java))
            }
            R.id.nav_ramadan ->{

            }
            R.id.nav_news -> {
                //startActivity(Intent(this, RewardActivity::class.java))
            }
            R.id.nav_recite -> {
                //startActivity(Intent(this, OrderActivity::class.java))
            }
            R.id.nav_setting -> {
                //startActivity(Intent(this, UpdateProfileActivity::class.java))
            }
            R.id.nav_logout -> {
                auth.signOut()
                //startActivity(Intent(this, MainActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val myOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_calendar -> {
                    text_view_title.text = "Calendar"
                    supportFragmentManager.beginTransaction().replace(R.id.container_fragment_home, CalendarFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_quran -> {
                    text_view_title.text = "Quran Book"
                    supportFragmentManager.beginTransaction().replace(R.id.container_fragment_home, QuranFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_verse -> {
                    text_view_title.text = "Verse"
                    supportFragmentManager.beginTransaction().replace(R.id.container_fragment_home, VerseFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }










//    private fun onCreateCurrentDate() {
//        val calendar = Calendar.getInstance()
//        var mYear = calendar.get((Calendar.YEAR))
//        var mMonth = calendar.get((Calendar.MONTH))
//        var mDay = calendar.get((Calendar.DAY_OF_MONTH))
//        date_view.text = (mMonth +1).toString() + "/" +  mDay.toString()+ "/" + mYear
//    }
//
//    private fun onClickCalendar(){
//        calendar_view.setOnDateChangeListener(object:CalendarView.OnDateChangeListener{
//            override fun onSelectedDayChange(
//                view: CalendarView,
//                year: Int,
//                month: Int,
//                dayOfMonth: Int
//            ) {
//                val Date = ( (month + 1).toString() + "/" +  dayOfMonth.toString()+ "/" + year)
//                // set this date in TextView for Display
//                date_view.text = Date
//
//            }
//        })
//    }
//
//    private fun onClickDate(){
//        image_view_home.setOnClickListener {
//            var intent = Intent(this,DateActivity::class.java)
//            var date = date_view.text.toString()
//            intent.putExtra("PickedDate",date)
//            startActivity(intent)
//
//        }
//    }

}
