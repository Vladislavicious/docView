import com.example.getFloatFromString
import com.example.pr2v6.back.Doctor
import com.example.pr2v6.back.parseDoctorFromInput

fun addNewDoctorFromMenu(): Boolean {
    val doctor = parseDoctorFromInput() ?: return false

    return DoctorList.add(doctor)
}

internal fun findDoctor(): Doctor? {
    print("\nВведите специализацию врача: ")
    val spec = readlnOrNull() ?: return null

    print("\nВведите искомую стоимость консультации: ")
    val priceStr = readlnOrNull() ?: return null
    val price = getFloatFromString(priceStr) ?: return null

    return DoctorList.findDoctorAccordingToString(spec, price)
}

fun findDoctorWithMessage(): Unit {
    val doctor = findDoctor()

    if( doctor == null ) {
        println("Поиск не удался")
        return
    }

    println("Результат поиска:\n${doctor.getFormattedOutput()}")
    return
}

internal fun parseDoctorsIndex(): Int? {
    println("Список докторов:")
    print(DoctorList.toString())

    print("Введите номер Доктора: ")
    val num = readlnOrNull()
    if( num == null ) {
        println("Номер не может быть пустым")
        return null
    }
    val parsedNum: Int?
    try {
        parsedNum = num.toIntOrNull()
    } catch (e: NumberFormatException) {
        println("Плохой формат номера")
        return null
    }

    if( parsedNum == null || parsedNum < 0 || parsedNum >= DoctorList.size ) {
        println("Неверно задан номер")
        return null
    }

    return parsedNum
}

fun deleteCertainDoctor(): Unit {
    val parsedNum = parseDoctorsIndex()
    if( parsedNum == null )
        return

    DoctorList.removeAt(parsedNum)
}

fun clearDoctorList() {
    DoctorList.clear()
}

fun deleteDoctorList() {

    val result = DoctorList.delete()
    if( result == true ) {
        DoctorList.clear()
        println("Файл успешно удалён")
        return
    }
    println("Ошибка удаления файла")
}

fun changeDoctorFromFile() {
    val parsedNum = parseDoctorsIndex()
    if( parsedNum == null )
        return

    val changeResult = DoctorList.changeDoctorAt(parsedNum)
    if( changeResult == false ) {
        println("Доктор не изменён")
        return
    }

    println("Изменения успешно внесены")
}

//////////////////////////////////////////////////////////////////////////////////////

// 3) Посмотреть предварительные записи - делает табличный вывод всех записей с подведенным внизу итогом
// 4) Отменить предварительную запись - по номеру записи в таблице, начиная с единицы
// 5) Отменить все предварительные записи
// 6) Выход

fun payForConsultations(): Boolean {
    ConsultationList.payForAll()
    return true
}

fun signUpForConsultation() {
    if( ConsultationList.isNotEmpty() ) {
        println("Предыдущие записи:\n${ConsultationList.toString()}")
    }

    val dctr = findDoctor()
    if( dctr == null ) {
        println("Доктор не найден")
        return
    }

    ConsultationList.add(dctr.getConsultation(0))
    println("Сделана предварительная запись к доктору ${dctr.getFullNameFormatted()}")
}

fun printConsultations() {
    println(ConsultationList.toString())
}

fun cancelConsultation() {
    print("Введите номер консультации, которую хотите убрать: ")
    val num = readlnOrNull()
    if( num == null ) {
        println("Номер не может быть пустым")
        return
    }
    val parsedNum: Int?
    try {
        parsedNum = num.toIntOrNull()
    } catch (e: NumberFormatException) {
        println("Плохой формат номера")
        return
    }

    if( parsedNum == null ) {
        println("Плохой формат номера")
        return
    }

    if( ConsultationList.cancelByIndex(parsedNum) ) {
        println("Консультация успешно отменена")
        return
    }

    println("Не получилось отменить консультацию")
}

fun cancelAllConsultations() {
    ConsultationList.cancelAll()
}