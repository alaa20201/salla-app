package com.example.salaahapp.helpers.decorator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SameDayDecorator(var markDay: CalendarDay,var isComplete:Boolean) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {

        //Log.d("day,today",day.toString()+" *********"+CalendarDay.today().toString())
        if (day == markDay){
            //Log.d("yes!!!day,today",day.toString()+" "+CalendarDay.today().toString())
            //decorate(view)
            return true
        }
        return false
    }
    override fun decorate(view: DayViewFacade) {
        view.addSpan(CircleBackGroundSpan())

    }

    inner class CircleBackGroundSpan : LineBackgroundSpan {
        override fun drawBackground(
            canvas: Canvas,
            paint: Paint,
            left: Int,
            right: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            lineNumber: Int
        ) {
            var paint = Paint()
            if (!isComplete){
                //Color.parseColor("#228BC34A")
                paint.color = Color.RED
            }
            else{
                paint.color = Color.GREEN
            }

            canvas.drawCircle((right - left) / 2.toFloat(), (bottom - top) / 2 +40.toFloat(),14.toFloat(),paint)
        }

    }
}