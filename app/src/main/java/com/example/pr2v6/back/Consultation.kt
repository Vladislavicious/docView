import com.example.DATA_FORMAT
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Consultation ( val time: LocalDateTime,
                     val doctorName: String,
                     val doctorSpecialization: String,
                     val price: Float) {

    fun getTimeFormatted(): String {
        return time.format(DateTimeFormatter.ofPattern(DATA_FORMAT))
    }

    override fun equals(other: Any?): Boolean {
        if( ! (other is Consultation) )
            return false

        if( time == other.time &&
            doctorName == other.doctorName &&
            doctorSpecialization == other.doctorSpecialization &&
            price == other.price )
            return true
        return false
    }
}

open class ListBaseImpl internal constructor( protected val _b: MutableList<Consultation> ) : MutableList<Consultation> by _b

object ConsultationList: ListBaseImpl( mutableListOf() ) {
    fun payForAll() {
        if( this.isNotEmpty()) {
            println("С вас: ${calculateTotalCost()}")
            this.clear()
            val unnecessary = readlnOrNull()
        }
        else {
            println("Нет консультаций")
        }
        return
    }

    fun cancelByIndex( index: Int ): Boolean {
        val realIndex = index - 1
        if( !CheckIfInBounds(realIndex) )
            return false

        removeAt(realIndex)
        return true
    }

    fun cancelAll() {
        this.clear()
        println("Все записи отменены")
        return
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append( String.format("%-3S|%-20S|%-20S|%-20S|%-10S\n", "№", "ФИО доктора", "Время", "Специализация", "Стоимость") )
        for ((i, consultation) in this.withIndex()) {
            builder.append( String.format("%-3d|%-20S|%-20S|%-20S|%-10.1f\n", i + 1, consultation.doctorName,
                consultation.getTimeFormatted(), consultation.doctorSpecialization, consultation.price) )
        }
        builder.append("-".repeat(77) + "\n")
        builder.append( String.format("%-24S|%-20S|%-20S|%-10S\n", "Разных докторов", "Разных дней", "Разных специализаций", "Стоимость") )
        builder.append( String.format("%-24d|%-20d|%-20d|%-10.1f\n", this.calculateTotalNumberOfDifferentDoctors(), this.calculateTotalNumberOfDifferentDays(),
                                                                    this.calculateTotalNumberOfDifferentSpecializations(), this.calculateTotalCost()) )

        return builder.toString()
    }

    private fun calculateTotalNumberOfDifferentDoctors(): Int {
        var differentDoctors: MutableSet<String> = this.mapTo(mutableSetOf()) { it.doctorName }
        return differentDoctors.count()
    }

    private fun calculateTotalNumberOfDifferentSpecializations(): Int {
        var differentSpecs: MutableSet<String> = this.mapTo(mutableSetOf()) { it.doctorSpecialization }
        return differentSpecs.count()
    }

    private fun calculateTotalNumberOfDifferentDays(): Int {
        var differentDates: MutableSet<LocalDate> = this.mapTo(mutableSetOf()) { it.time.toLocalDate() }
        return differentDates.count()
    }

    fun calculateTotalCost(): Float {
        var cost: Float = this.map { it.price }.sum()
        return cost
    }

    private fun CheckIfInBounds(index: Int): Boolean {
        return index >= 0 && index < this.size
    }
}