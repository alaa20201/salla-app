package com.example.salaahapp.views.fragments


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.room.Room

import com.example.salaahapp.R
import com.example.salaahapp.database.MyAppDatabase
import com.example.salaahapp.helpers.decorator.SameDayDecorator
import com.example.salaahapp.views.activities.fragmentsCalendar.DateActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {
    //lateinit var calender: CalendarView
    lateinit var calendarView:MaterialCalendarView
    lateinit var date_view: TextView
    lateinit var sameDayDecorator: SameDayDecorator
    lateinit var sameDayDecorator2: SameDayDecorator
    lateinit var sameDayDecorator3: SameDayDecorator
    lateinit var sameDayDecorator4: SameDayDecorator
    lateinit var sameDayDecorator5: SameDayDecorator
    lateinit var sameDayDecorator6: SameDayDecorator
    lateinit var sameDayDecorator7: SameDayDecorator
    lateinit var sameDayDecorator8: SameDayDecorator

    val calendar = Calendar.getInstance()
    var mYear = calendar.get((Calendar.YEAR))
    var mMonth = calendar.get((Calendar.MONTH))
    var mDay = calendar.get((Calendar.DAY_OF_MONTH))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        date_view = view.findViewById(R.id.text_view_picked_date)
        /**
         * Testing
          */
        //calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE)
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_NONE
        sameDayDecorator = SameDayDecorator(CalendarDay.from(2020, 2, 24),false)
        sameDayDecorator2 = SameDayDecorator(CalendarDay.from(2020, 2, 23),true)
        sameDayDecorator3 = SameDayDecorator(CalendarDay.from(2020, 2, 25),true)
        sameDayDecorator4 = SameDayDecorator(CalendarDay.from(2020, 2, 26),true)
        sameDayDecorator5 = SameDayDecorator(CalendarDay.from(2020, 2, 27),true)
        sameDayDecorator6 = SameDayDecorator(CalendarDay.from(2020, 2, 28),false)
        sameDayDecorator7 = SameDayDecorator(CalendarDay.from(2020, 2, 29),true)
        calendarView.addDecorators(sameDayDecorator)
        calendarView.addDecorators(sameDayDecorator2)
        calendarView.addDecorators(sameDayDecorator3)
        calendarView.addDecorators(sameDayDecorator4)
        calendarView.addDecorators(sameDayDecorator5)
        calendarView.addDecorators(sameDayDecorator6)
        calendarView.addDecorators(sameDayDecorator7)

        //var yesterday = calendarView.setDateSelected(CalendarDay.from(2020, 3, 24), true)
        /**
         * Setting calendar
         */
        ///calendarView.selectionColor = Color.RED
        //calendarView.setDateSelected(CalendarDay.today(), true);
        //calendarView.setDateSelected(CalendarDay.from(2020, 2, 24), true)
        /**
         * Logic for checking current day prayers
         */


        //calendarView.selectionColor = Color.CYAN
        //var current_date = (mMonth +1).toString() + "/" +  mDay.toString()+ "/" + mYear
       // calendarView.currentDate = CalendarDay.today()
        //var date = calendarView.currentDate
        //var viewDate = view.findViewById(R.id.)

        onCreateCurrentDate(view)
        onClickCalendar(view)
        onClickDate(view)
        checkPrayerStatus(view)
        return view
    }

    private fun checkPrayerStatus(view:View){
        var myDB = Room.databaseBuilder(activity!!, MyAppDatabase::class.java,"prayerdb")
            .allowMainThreadQueries()
            .build()
        var count = myDB.myDao().isDatabaseNull()

        /**
         * Setting calendar
         */

        if (count > 0){
            var prayerList = myDB.myDao().getAllPrayer()
            if (prayerList.size == 5){
                view.layout_picked_date.setBackgroundResource(R.drawable.layout_date_complete)
                view.image_view_picked_date.setImageResource(R.drawable.ic_check_complete_24dp)
                calendarView.
                calendarView.selectionColor = Color.CYAN
                calendarView.setDateSelected(CalendarDay.today(), true)
            }
            else{
                calendarView.selectionColor = Color.RED
                calendarView.setDateSelected(CalendarDay.today(), true)
                view.layout_picked_date.setBackgroundResource(R.drawable.layout_date_incomplete)
            }
        }else{
            view.layout_picked_date.setBackgroundResource(R.drawable.layout_date_incomplete)
        }

    }
    private fun onCreateCurrentDate(view: View) {
        date_view.text = (mMonth +1).toString() + "/" +  mDay.toString()+ "/" + mYear
    }
    private fun onClickCalendar(view:View){
//        view.calendar_view.setOnDateChangeListener(object: CalendarView.OnDateChangeListener{
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
    }

    private fun onClickDate(view:View){
        view.image_view_home.setOnClickListener {
            var intent = Intent(activity, DateActivity::class.java)
            var date = date_view.text.toString()
            intent.putExtra("PickedDate",date)
            startActivity(intent)
        }
    }


}
