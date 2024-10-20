import com.example.*
import com.example.pr2v6.back.Doctor
import com.example.pr2v6.back.getDoctorFromString

open class HiddenBaseImpl internal constructor( protected val _b: MutableList<Doctor> ) : MutableList<Doctor> by _b

//TODO: Убрать работу с файлом в отдельный класс

object DoctorList: HiddenBaseImpl( mutableListOf() ) {
    private var fileName: String = ""

    fun initialize(filename: String): Boolean {
        this.fileName = filename

        val fileContent = readFileContents(this.fileName) ?: return false

        val Indexes = findContentBetweenBraces('[', 0, fileContent) ?: return false

        val content = fileContent.substring(startIndex = Indexes.first + 1, endIndex = Indexes.second)
        val arr = getListFromJsonList(content, { stroka -> getDoctorFromString(stroka) } )
        if(arr != null) {
            this.addAll(arr)
        }

        return true
    }


    fun toJSON(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append("[")
        if( this.size != 0 ) {
            for( (i, doctor) in this.withIndex() ) {
                builder.append("\n")
                builder.append(doctor.toJSON())
                if( i != this.size - 1 )
                    builder.append(",")
            }
        }

        builder.append("\n")
        builder.append("]")
        return builder.toString()
    }

    fun delete(): Boolean {
        return deleteFile(this.fileName)
    }

    override fun add(element: Doctor): Boolean {
        super.add(element)

        return rewriteBackToFile()
    }

    override fun removeAt(index: Int): Doctor {
        val dctr = super.removeAt(index)

        this.rewriteBackToFile()

        return dctr
    }

    override fun clear() {
        super.clear()
        this.truncateFile()
    }

    fun changeDoctorAt(doctorIndex: Int): Boolean {
        if( doctorIndex < 0 || doctorIndex >= this.size )
            return false

        val changeResults = this[doctorIndex].changeDoctor()
        if( changeResults )
           return this.rewriteBackToFile()

        return changeResults
    }

    private fun rewriteBackToFile(): Boolean {
        if( !truncateFile()  ) return false

        return writeInFile(
            this.fileName,
            this.toJSON()
        )
    }

    private fun truncateFile(): Boolean {
        if( truncateFile(this.fileName) != true ) {
            println("couldn't truncate file")
            return false
        }
        return true
    }

    fun findDoctorAccordingToString(specialization: String, price: Float): Doctor? {
        return this.find { (it.specialization.lowercase() == specialization.lowercase()) && (it.consultationPrice == price) }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append( String.format("%-3S|%-20S|%-20S|%-10S\n", "№", "ФИО доктора", "Специализация", "Стоимость") )
        for ((i, doctor) in this.withIndex()) {
            builder.append( String.format("%-3d|%-20S|%-20S|%-10.1f\n", i, doctor.getFullNameFormatted(),
                                                     doctor.specialization, doctor.consultationPrice) )
        }
        return builder.toString()
    }
}