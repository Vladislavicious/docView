package com.example.pr2v6.back

import Consultation
import com.example.DoctorReview
import getRandomString
import com.example.DATA_FORMAT
import com.example.JsonObjectType
import com.example.MyJsonObject
import com.example.getCustomDataFromString
import com.example.getDateFromString
import com.example.getFloatFromPair
import com.example.getFloatFromString
import com.example.getListFromJsonList
import com.example.parseCustomDataFromInput
import strIsLitsOnly
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class Doctor(
    var specialization: String,
    var fullName: String, // Фамилия, имя, отчество
    var workSchedule: List<LocalDateTime>,
    var consultationPrice: Float,
    val patientReviews: List<DoctorReview>
) {
    init {
        require(fullName.split(" ").size == 3) { "Введен неверный формат ФИО" }
        require(consultationPrice >= 0) { "Цена приема не может быть отрицательной" }
    }

    // Функция для вывода ФИО в формате "Фамилия И. О."
    fun getFullNameFormatted(): String {
        val parts = fullName.split(" ")
        return "${parts[0]} ${parts[1][0]}. ${parts[2][0]}."
    }

    companion object {
        private var lastContactId = 0
        fun createDoctorsList(numContacts: Int) : ArrayList<Doctor> {
            val contacts = ArrayList<Doctor>()
            for (i in 1..numContacts) {
                contacts.add(
                    getRandomDoctor()
                )
            }
            return contacts
        }
    }

    fun toJSON(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append("{\n")
        builder.append("\"specialization\": \"$specialization\",\n")
        builder.append("\"fullName\": \"$fullName\",\n")
        builder.append("\"workSchedule\": [")
        if( workSchedule.isNotEmpty() ) {
            for( (i, schedule) in workSchedule.withIndex() ) {
                builder.append("\n\"${schedule.format(DateTimeFormatter.ofPattern(DATA_FORMAT))}\"")
                if( i != workSchedule.size - 1 )
                    builder.append(',')
            }
            builder.append("\n")
        }
        builder.append("],\n")
        builder.append("\"consultationPrice\": $consultationPrice,\n")
        builder.append("\"patientReviews\": [")
        if( patientReviews.isNotEmpty() ) {
            for( (i, review) in patientReviews.withIndex() ) {
                builder.append('\n')
                builder.append(review.toJSON())
                if( i != patientReviews.size - 1 )
                    builder.append(',')
            }
            builder.append("\n")
        }
        builder.append("]\n}")
        return builder.toString()
    }

    fun getFormattedOutput(): String {
        val returnStr: String = "Врач: ${getFullNameFormatted()}\n" +
                "Специализация: $specialization\n" +
                "Стоимость консультации: $consultationPrice\n" +
                "График работы:\n${getSheduleFormatted()}\n" +
                "${getReviewsFormatted()}"
        return returnStr
    }

    fun changeDoctor(): Boolean {
        println("Для изменения введите новое значение и нажмите Enter. Для перехода к следующему пункту просто нажмите Enter")

        println("ФИО: ${this.fullName}")
        val changedName = readlnOrNull() ?: return false
        if( !changedName.isBlank() ) {
            if( !strIsLitsOnly(changedName) )
            {
                println("Плохое имя")
                return false
            }
            if( changedName.split(" ").size != 3) {
                println("Плохой формат ФИО")
                return false
            }
            this.fullName = changedName
        }

        println("Специализация: $specialization\n")
        val spec = readlnOrNull() ?: return false
        if( !spec.isBlank() ) {
            if( strIsLitsOnly(spec) )
                this.specialization = spec
            else {
                println("Плохая специализация")
                return false
            }
        }

        println("Стоимость консультации: $consultationPrice\n")
        val consultation = readlnOrNull() ?: return false

        if( !consultation.isBlank() ) {
            val consPrice: Float = getFloatFromString(consultation) ?: return false
            if( consPrice < 0.0f ) {
                println("Введена отрицательная стоимость")
                return false
            }
            else {
                this.consultationPrice = consPrice
            }
        }

        println("График работы: ${getSheduleFormatted()}\n")
        val mutabl: MutableList<LocalDateTime> = this.workSchedule.toMutableList()

        print("\nВводите дату в формате $DATA_FORMAT.\n")
        for( (i, schedule) in this.workSchedule.withIndex() )
        {
            println(schedule.format(DateTimeFormatter.ofPattern(DATA_FORMAT)))
            val newScheduleStr = readlnOrNull()
            if (newScheduleStr == null ) {
                return false
            }

            if( newScheduleStr.isBlank() ) {
                continue
            }
            else {
                val parsedSchedule = getDateFromString(newScheduleStr)
                if( parsedSchedule == null ) {
                    println("Неправильный формат даты")
                    return false
                }
                mutabl[i] = parsedSchedule
            }
        }

        this.workSchedule = mutabl.toList()

        return true
    }

    fun getConsultation( position: Int ): Consultation {
        val time = this.workSchedule[position]

        return Consultation(time, this.fullName, this.specialization, this.consultationPrice)
    }

    private fun getReviewsFormatted(): String {
        val reviewsFormatted: StringBuilder = StringBuilder()
        if( patientReviews.isNotEmpty() )
            reviewsFormatted.append("\n\t\t\tОтзывы:\n")
        for ( review in patientReviews ) {
            reviewsFormatted.append(review.getFormattedOutput())
            reviewsFormatted.append("\n")
        }

        return reviewsFormatted.toString()
    }

    private fun getSheduleFormatted(): String {
        val scheduleFormatted: StringBuilder = StringBuilder()


        for ( schedule in workSchedule ) {
            val scheduleStr = schedule.format(DateTimeFormatter.ofPattern(DATA_FORMAT))
            scheduleFormatted.append(scheduleStr)
            scheduleFormatted.append("\n")
        }

        return scheduleFormatted.toString()
    }

    fun getDoctorsWorkSchedule(): List<LocalDateTime> {
        return workSchedule
    }

    fun getDoctorsConsultations(): List<Consultation> {
        val returnList: MutableList<Consultation> = mutableListOf()
        for( index in 0 until this.workSchedule.size )
        {
            returnList.add( getConsultation(index) )
        }

        return returnList
    }
    fun getDoctorsReviews(): List<DoctorReview> {
        return patientReviews
    }

    fun getDoctorsRating(): Float {

        if( patientReviews.isEmpty() )
            return 0f

        var sum = 0f
        for( item in patientReviews )
            sum += item.rating

        return sum / patientReviews.size

    }

    fun getRatingString(): String {
        var rating: Float = getDoctorsRating()
        var ratingText = rating.toString()
        if( rating == 0.0f ) {
            ratingText = "N/A"
        }
        return ratingText
    }
}

fun getDoctorFromJsonObject(jsonObject: MyJsonObject): Doctor? {
    val spec = jsonObject.getValueByKey("specialization")
    val name = jsonObject.getValueByKey("fullName")
    val schedule = jsonObject.getValueByKey("workSchedule")
    val price = jsonObject.getValueByKey("consultationPrice")
    val reviews = jsonObject.getValueByKey("patientReviews")

    if( (spec == null ) || (name == null ) || (schedule == null ) || (price == null ) || (reviews == null ) )
        return null

    if( JsonObjectType.Str != spec.first || JsonObjectType.Str != name.first )
        return null

    if( name.second.split(" ").size != 3 )
        return null

    if( schedule.first != JsonObjectType.Array )
        return null

    val scheduleList: List<LocalDateTime>? = getListFromJsonList(schedule.second, { data -> getDateFromString( data.trim(chars = " \"".toCharArray() )) })
    if(scheduleList == null)
        return null

    val floatPrice: Float = getFloatFromPair(price) ?: return null

    if( reviews.first != JsonObjectType.Array )
        return null

    val reviewsArray = getListFromJsonList(reviews.second, { dict -> getCustomDataFromString( dict ) })
    if( reviewsArray == null )
        return null

    return Doctor(
        specialization = spec.second,
        fullName = name.second,
        workSchedule = scheduleList,
        consultationPrice = floatPrice,
        patientReviews = reviewsArray
    )
}

fun getDoctorFromString(stroka: String): Doctor? {
    val obj = MyJsonObject(stroka, 0, 10)
    val parseResult = obj.parse()
    if(!parseResult)
        return null
    return getDoctorFromJsonObject(obj)
}

fun parseDoctorFromInput(): Doctor? {
    print("\nВведите специализацию: ")
    val spec: String? = readlnOrNull()
    if( spec.isNullOrBlank() ) {
        println("Пустая специализация")
        return null
    }
    if( !strIsLitsOnly(spec) )
    {
        println("Плохая специализация")
        return null
    }

    print("\nВведите полное имя доктора: ")
    val name: String? = readlnOrNull()
    if( name.isNullOrBlank() ) {
        println("Пустое ФИО")
        return null
    }
    if( name.trim().split(" ").size != 3 ) {
        println("Должно быть введено ФИО")
        return null
    }
    if( !strIsLitsOnly(name) )
    {
        println("Плохое имя")
        return null
    }

    val scheduleList: MutableList<LocalDateTime> = mutableListOf()
    var newScheduleStr: String?

    do {
        print("\nВведите дату в формате $DATA_FORMAT: ")

        newScheduleStr = readlnOrNull()
        if (newScheduleStr.isNullOrBlank() )
            continue

        if( newScheduleStr == "end")
            break

        var newSchedule: LocalDateTime? = getDateFromString(newScheduleStr)
        if( newSchedule == null ) {
            println("Дата в неправильном формате")
            continue
        }

        println("чтобы закончить ввод, напишите \"end\"")

        scheduleList.add(newSchedule)

    } while( newScheduleStr != "end" )

    print("\nВведите стоимость консультации: ")
    val consultationPriceStr: String? = readlnOrNull()
    if( consultationPriceStr.isNullOrBlank() ) {
        println("Должна быть введена стоимость")
        return null
    }

    val consultationPrice: Float? = getFloatFromString(consultationPriceStr)
    if( consultationPrice == null || consultationPrice < 0 ) {
        println("Неправильный формат стоимости консультации")
        return null
    }

    val reviewList: MutableList<DoctorReview> = mutableListOf()
    var endStr: String?

    do {
        print("\nНапишите отзыв:\n")

        val review = parseCustomDataFromInput()

        if( review != null ) {
            reviewList.add(review)
        }

        println("чтобы закончить ввод, напишите \"end\". Для продолжения введите любой символ")
        endStr = readlnOrNull()

    } while( endStr != "end" )

    return Doctor(
        specialization = spec,
        fullName = name,
        workSchedule = scheduleList.toList(),
        consultationPrice = consultationPrice,
        patientReviews = reviewList.toList()
    )
}

fun getRandomReview(): DoctorReview {
    return DoctorReview(
        reviewDate = LocalDateTime.now(),
        patientName = getRandomString(7),
        reviewText = getRandomString(13),
        rating = nextInt(1, 10).toFloat()
    )
}

fun getRandomDoctor(): Doctor {
    val scheduleList: MutableList<LocalDateTime> = mutableListOf()
    val numOfSchedules = Random.nextInt(1, 8)
    for(i in 0 until numOfSchedules) {
        val addition = Random.nextLong(0, 600)
        var newDateTime = LocalDateTime.now().plusMinutes(addition)
        if( scheduleList.contains(newDateTime))
        {
            continue
        }
        else
        {
            scheduleList.add( newDateTime )
        }
    }
    scheduleList.sort()

    val cusDataList: MutableList<DoctorReview> = mutableListOf()
    val numOfCustomData = Random.nextInt(0, 5)
    for(i in 0 until numOfCustomData) {
        cusDataList.add(getRandomReview())
    }

    return Doctor(
        specialization = getRandomString(8),
        fullName = "${getRandomString(5)} ${getRandomString(6)} ${getRandomString(7)}",
        workSchedule = scheduleList.toList(),
        consultationPrice = nextInt(100, 10000).toFloat(),
        patientReviews = cusDataList.toList()
    )
}