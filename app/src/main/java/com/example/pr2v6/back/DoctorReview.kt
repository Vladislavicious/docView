package com.example

import strIsLitsOnly
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DoctorReview(
    val reviewDate: LocalDateTime,
    val patientName: String,
    val reviewText: String,
    val rating: Float

) {
    fun getFormattedOutput(): String {
        val returnStr: String = "$patientName\nОценка: $rating\nОтзыв от ${getDateFormatted()}: \"" +
                "$reviewText\""
        return returnStr
    }

    fun getViewString(): String {
        return "$patientName от ${getDateFormatted()}:"
    }

    fun toJSON(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append("{\n")
        builder.append("\"reviewDate\": \"${getDateFormatted()}\",\n")
        builder.append("\"patientName\": \"$patientName\",\n")
        builder.append("\"reviewText\": \"$reviewText\",\n")
        builder.append("\"rating\": $rating\n}")
        return builder.toString()
    }

    private fun getDateFormatted(): String {
        return reviewDate.format(DateTimeFormatter.ofPattern(DATA_FORMAT))
    }
}

fun getCustomDataFromString(str: String): DoctorReview? {
    val jsonObj = MyJsonObject(str, 0, 10)
    val parseResult = jsonObj.parse()
    if(!parseResult)
        return null

    return getCustomDataFromJsonObject( jsonObj )
}

fun getCustomDataFromJsonObject(jsonObject: MyJsonObject ): DoctorReview? {
    val date = jsonObject.getValueByKey("reviewDate")
    val name = jsonObject.getValueByKey("patientName")
    val review = jsonObject.getValueByKey("reviewText")
    val rating = jsonObject.getValueByKey("rating")

    if((date == null) || (name == null) ||
        (review == null) || (rating == null))
        return null

    if( JsonObjectType.Str != name.first || JsonObjectType.Str != review.first )
        return null

    val dateTime: LocalDateTime = getLocalTimeFromPair(date) ?: return null

    val floatRating: Float = getFloatFromPair(rating) ?: return null

    return DoctorReview(
        reviewDate = dateTime,
        patientName = name.second,
        reviewText = review.second,
        rating = floatRating
    )
}

fun parseCustomDataFromInput(): DoctorReview? {
    print("Введите имя: ")
    val name: String? = readlnOrNull()
    if( name.isNullOrBlank() ) {
        println("Пустое имя")
        return null
    }
    if( !strIsLitsOnly(name) )
    {
        println("Плохое имя")
        return null
    }
    print("\nВведите рейтинг: ")
    val ratingStr: String? = readlnOrNull()
    if( ratingStr.isNullOrBlank() ) {
        println("Пустой рейтинг")
        return null
    }
    val rating: Float? = getFloatFromString(ratingStr)
    if( rating == null || rating < 0) {
        println("Плохой рейтинг")
        return null
    }

    print("\nВведите дату в формате $DATA_FORMAT: ")

    val newDateStr = readlnOrNull()
    if (newDateStr == null ) {
        println("Пустая дата")
        return null
    }

    var newDate: LocalDateTime? = getDateFromString(newDateStr)
    if( newDate == null ) {
        println("Дата в неправильном формате")
        return null
    }

    print("\nВведите отзыв: ")
    val review: String? = readlnOrNull()
    if( review == null ){
        println("Пустой отзыв")
        return null
    }

    return DoctorReview(
        reviewDate = newDate,
        patientName = name,
        reviewText = review,
        rating = rating,
    )
}