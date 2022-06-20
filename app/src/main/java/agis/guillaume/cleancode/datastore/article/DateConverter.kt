package agis.guillaume.cleancode.datastore.article

import androidx.room.TypeConverter
import java.util.*

/**
 * Date converter to be able to store and retrieve a date inside the field in the local DB.
 */
class DateConverter {

    // use the timestamp stored to re-create the Date object
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    // convert the date into the timestamp
    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}