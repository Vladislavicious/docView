package com.example

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

const val DATA_FORMAT = "dd/MM/yyyy HH:mm"

enum class JsonObjectType {
    Dict,
    Array,
    Str,
    Number,
    Null,
    Bool,
    NonJson
}

fun getStringBetweenQuotes(quoteSymbol: Char, str: String): String? {
    var startSymbol : Int = str.indexOfFirst { c -> c == quoteSymbol }

    if( startSymbol == -1 )
        return null

    val endSymbol : Int = str.indexOfLast { c -> c == quoteSymbol }

    if(startSymbol == endSymbol)
        return null

    if ( endSymbol == -1)
        return null

    startSymbol++

    return str.substring(startSymbol, endSymbol)
}

fun findSymbol(goalSymbol: Char, startFrom: Int, str: String): Int? {
    val symbolIndex : Int = str.indexOf(goalSymbol, startFrom)
    return if (symbolIndex == -1) {
        null
    } else
        symbolIndex
}

fun findSymbolOrEnd(goalSymbol: Char, startFrom: Int, str: String): Int {
    var i: Int = startFrom
    while ( i < str.length ) {
        if (str[i] == goalSymbol)
            break
        i++
    }
    return i
}

fun findNonSpaceSymbol(startFrom: Int, str: String): Int? {
    for (i in startFrom until str.length) {
        if ( str[i].isWhitespace() )
            continue

        return i
    }
    return null
}

// Возвращает начало-конец скобок
fun findContentBetweenBraces(braceSymbol: Char, startFrom: Int, str: String): Pair<Int, Int>? {
    assert( braceSymbol in "([{" )

    var firstBracket: Boolean = true

    val closeBraceSymbol: Char
    closeBraceSymbol = when (braceSymbol) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        else -> return null
    }

    var openBracketIndex: Int = -1
    var closeBracketIndex: Int = -1

    var braceCounter: Int = 0 // Переменная, считающая сколько скобок осталось до конца

    for ( i in startFrom until  str.length ) {
        val curSymbol: Char = str[i]
        if((curSymbol != braceSymbol) &&
            (curSymbol != closeBraceSymbol)
        ) {
            continue
        }

        when (curSymbol) {
            braceSymbol -> {
                if( firstBracket ) {
                    openBracketIndex = i
                    firstBracket = false
                }
                braceCounter++
            }
            closeBraceSymbol -> braceCounter--
        }

        if( braceCounter <= 0 ) { // если меньше нуля, значит нет открывающей скобки и выходим из цикла
            closeBracketIndex = i
            break
        }
    }

    if( openBracketIndex == -1  || closeBracketIndex == -1 )
        return null

    return Pair(openBracketIndex, closeBracketIndex)
}

fun getJsonObjectType( firstSymbolIndex: Int, str: String ): JsonObjectType {
    if(str.isEmpty())
        return JsonObjectType.NonJson

    val firstSymbol: Char = str[firstSymbolIndex]

    when( firstSymbol ) {
        '{' -> return JsonObjectType.Dict
        '[' -> return JsonObjectType.Array
        '"' -> return JsonObjectType.Str
    }

    val endIndex: Int = findSymbolOrEnd(',', firstSymbolIndex, str)

    val subStr: String = str.substring(firstSymbolIndex, endIndex)

    when( subStr ) {
        "true" -> return JsonObjectType.Bool
        "false" -> return JsonObjectType.Bool
        "null" -> return JsonObjectType.Null
    }

    if( subStr.toDoubleOrNull() != null ||
        subStr.toIntOrNull() != null )
        return JsonObjectType.Number

    return JsonObjectType.NonJson
}

// Возвращает null, только если в процессе парсинга была ошибка, в противном случае список
fun <T: Any> getListFromJsonList(targetStr: String,
                                 conversionFunction: (input: String) -> T? ) : List<T>? {
    val cleanTargetStr = targetStr.trim(' ', '\n', '\r', '\t', ',')
    if( cleanTargetStr.isEmpty() )
        return listOf()

    val firstNonNullSymbolIndex = findNonSpaceSymbol(0, cleanTargetStr) ?: return null

    val jsonObjType = getJsonObjectType(firstNonNullSymbolIndex, cleanTargetStr)
    var strList: MutableList<String>
    if ( jsonObjType == JsonObjectType.Dict || jsonObjType == JsonObjectType.Array ) {

        val braceSymbol: Char = if( jsonObjType == JsonObjectType.Array )
            '['
        else
            '{'

        var startSymbol = 0

        val strEnd = cleanTargetStr.length
        strList = mutableListOf()
        while ( startSymbol < strEnd ) {
            val startAndEnd = findContentBetweenBraces(braceSymbol = braceSymbol,
                                                        startFrom = startSymbol,
                                                        str = cleanTargetStr)
            if( startAndEnd == null )
                return null

            val subs = cleanTargetStr.substring(startAndEnd.first + 1, startAndEnd.second)
            strList.add( subs )
            startSymbol = startAndEnd.second + 1

        }
    }
    else {
        strList = cleanTargetStr.split(',').toMutableList()
        strList.removeIf { it.isBlank() }
    }


    if( strList.size == 0 )
        return listOf()

    var returnList: MutableList<T> = mutableListOf()

    for( i in 0 until strList.size ) {
        strList[i] = strList[i].trim()
        if(strList[i].isEmpty())
            continue

        val newElement: T = conversionFunction(strList[i]) ?: return null
        returnList.add(newElement)
    }

    if( returnList.size > 0 )
        return returnList.toList()

    return null
}

fun getDateFromString(strng: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(strng, DateTimeFormatter.ofPattern(DATA_FORMAT))
    } catch (e: DateTimeParseException) {
        null
    }
}

fun getLocalTimeFromPair(pair: Pair<JsonObjectType, String> ): LocalDateTime? {
    if (pair.first != JsonObjectType.Str)
        return null

    return getDateFromString(pair.second)
}

fun getFloatFromString(strng: String): Float? {
    return try {
        strng.toFloat()
    } catch (e: NumberFormatException) {
        null
    }
}

fun getFloatFromPair(pair: Pair<JsonObjectType, String> ): Float? {
    if (pair.first != JsonObjectType.Number)
        return null

    return getFloatFromString(pair.second)
}