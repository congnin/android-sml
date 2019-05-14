package vn.kingbee.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateUtil {
    const val DD_MM_YYYY = "dd-MM-yyyy"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val MM_YYYY = "MM-yyyy"
    const val DD = "dd"
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val HH_MM_SS = "HH:mm:ss"


    fun format(date: Date, dateFormat: String): String {
        return SimpleDateFormat(dateFormat)
            .format(date)
    }

    @Throws(ParseException::class)
    fun format(date: String, dateFormat: String): Long {
        return SimpleDateFormat(dateFormat).parse(date)
            .time
    }

    fun lessOrEqualDate(dateStart: Date, dateEnd: Date, yearPeriod: Int): Boolean {
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = dateEnd
        calendarEnd.add(Calendar.YEAR, -yearPeriod)

        return !getBeginningTimeOfTheDate(dateStart).after(getBeginningTimeOfTheDate(calendarEnd.time))
    }

    fun lessDate(dateStart: Date, dateEnd: Date): Boolean {
        return getBeginningTimeOfTheDate(dateStart).before(getBeginningTimeOfTheDate(dateEnd))
    }

    fun getDate(date: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        return calendar.time
    }

    @Throws(ParseException::class)
    fun getDate(date: String, dateFormat: String): Date {
        return SimpleDateFormat(dateFormat)
            .parse(date)
    }

    fun getBeginningTimeOfTheDate(date: Date): Date {
        val calendar = Calendar.getInstance()

        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    fun normalizeDate(source: String, dateFormat: String): String {
        try {
            val date = getDate(source, dateFormat)
            return SimpleDateFormat(dateFormat).format(date)
        } catch (e: ParseException) {
            return source
        }
    }
}