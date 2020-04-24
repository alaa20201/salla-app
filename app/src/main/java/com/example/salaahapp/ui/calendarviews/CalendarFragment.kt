package com.example.salaahapp.ui.calendarviews


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders

import com.example.salaahapp.R
import com.example.salaahapp.utils.SameDayDecorator
import com.example.salaahapp.utils.retrievePrayers
import com.example.salaahapp.ui.homescreen.HomeViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {
    lateinit var calendarView: MaterialCalendarView
    lateinit var date_view: TextView
    lateinit var sameDayDecorator: SameDayDecorator
    val calendar = Calendar.getInstance()
    private lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = activity?.let {
            ViewModelProviders.of(it)[HomeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        date_view = view.findViewById(R.id.text_view_picked_date)

        //TODO
        /**
         * should recieve all the saved dates and add decorater (completed/incomplete)
         * should add incomplete for today date
         * should trigger service to watch each prayer and push notification to adjust
         * prayer with the app and at the end should save today with adujsted values
         * if it is not save already by DateActivity
         */
        var inc = false
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
        homeViewModel.prayerData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val currentDate = CalendarDay.today()
            for (key in it.keys) {
                inc = isPrayersCompletedForThisDate(CalendarDay(Date(key)))
                decorateView(key, inc)
                inc = false
            }
            homeViewModel.selected_date.observe(viewLifecycleOwner, androidx.lifecycle.Observer { cc->
                val inc: Boolean = isPrayersCompletedForThisDate(cc)
                setTitleDate(cc)
                setTitleDateOutline(cc, inc, view)
                if (homeViewModel.sourceview == "LGN") {
                    val date = ((cc.month + 1).toString() + "/" + cc.day.toString() + "/" + cc.year)
                    decorateView(date, inc)
                }
            })
        })

        onClickCalendar(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        retrievePrayers(::setPrayerData)
    }

    fun isPrayersCompletedForThisDate(d: CalendarDay): Boolean {
        val date = ((d.month + 1).toString() + "/" + d.day.toString() + "/" + d.year)
        if (homeViewModel.prayerData?.value?.containsKey(date) == false ) return false
        homeViewModel.prayerData?.value?.get(date)?.let {
            for (key in it.keys) {
                val x = it[key]
                if (it[key].isNullOrBlank()) {
                    return@isPrayersCompletedForThisDate false
                }
            }
        }
        return true
    }

    fun decorateView(target: String, inComplete: Boolean) {
        val currentDate = CalendarDay(Date(target))
        sameDayDecorator = SameDayDecorator(
            currentDate,
            inComplete
        )
        calendarView.addDecorators(sameDayDecorator)
    }

    fun setTitleDateOutline(
        date: CalendarDay,
        complete: Boolean,
        view: View
    ) {
        if (complete) {
            setTitleDate(date)
            view.layout_picked_date.setBackgroundResource(R.drawable.layout_date_complete)
            view.image_view_picked_date.setImageResource(R.drawable.ic_check_complete_24dp)

        } else {
            setTitleDate(date)
            view.layout_picked_date.setBackgroundResource(R.drawable.layout_date_incomplete)
            view.image_view_picked_date.setImageResource(R.drawable.ic_error_outline_black_24dp)
        }
    }

    fun setPrayerData(map: Map<String, Map<String, String>>) {
        homeViewModel.setData(map)
    }

    fun setTitleDate(d: CalendarDay) {
        val Date = ((d.month + 1).toString() + "/" + d.day.toString() + "/" + d.year)
        date_view.text = Date
    }

    private fun onClickCalendar(view: View) {
        view.calendarView.setOnDateChangedListener(object : CalendarView.OnDateChangeListener,
            OnDateSelectedListener {
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                val Date = ((month + 1).toString() + "/" + dayOfMonth.toString() + "/" + year)
                // set this date in TextView for Display
                date_view.text = Date

            }

            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                val mDate =
                    ((date.month + 1).toString() + "/" + date.day.toString() + "/" + date.year)
                // set this date in TextView for Display
                date_view.text = mDate
                if (!date.isAfter(CalendarDay.today())) {
                    var intent = Intent(activity, DateActivity::class.java)
                    val prayerView = homeViewModel.prayerViewData
                    intent.putParcelableArrayListExtra("prayers", prayerView.prayerView)
                    homeViewModel.setSelectedDay(date)
                    if (checkIfExist(mDate)) homeViewModel.updateDateSelected(mDate)
                    else homeViewModel.resetDateSelected()
                    intent.putExtra("PickedDate", mDate)
                    startActivity(intent)
                }

            }
        })
    }

    fun checkIfExist(date: String): Boolean{
        return homeViewModel.prayerData.value?.contains(date)?:false
    }
}
