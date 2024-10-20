package com.example

class MyJsonObject(val targetString: String,
                   var parseFromIndex: Int,
                   val maxParseLength: Int ) {

    var propertiesDict: MutableMap<String, Pair<JsonObjectType, String>> =
        mutableMapOf()

    public fun getValueByKey(key: String ) : Pair<JsonObjectType, String>? {
        return this.propertiesDict[key]
    }

    public fun parse(): Boolean {

        val strLength: Int = targetString.length
        if (strLength < 4)
            return false

        do {
            val key: String = parseKey() ?: return false

            if ( !findStartOfValue() )
                return false

            val value: Pair<JsonObjectType, String> = this.parseValue() ?: return false

            propertiesDict[key] = value

            findStartOfKey()
        } while( parseFromIndex < strLength )

        return true
    }

    private fun parseKey(): String? {
        var startIndex: Int = findSymbol('"', this.parseFromIndex, this.targetString) ?: return null

        startIndex++

        val endIndex: Int = findSymbol('"', startIndex + 1, this.targetString) ?: return null

        val key: String = this.targetString.substring(startIndex = startIndex, endIndex = endIndex).trim()

        return key
    }


    private fun parseValue(): Pair<JsonObjectType, String>? {

        val type: JsonObjectType = getJsonObjectType(this.parseFromIndex, this.targetString)
        if( type == JsonObjectType.NonJson)
            return null // Под вопросом

        val endIndex: Int?
        when( type ) {
            JsonObjectType.Str -> endIndex = findSymbolOrEnd(',', this.parseFromIndex, this.targetString)
            JsonObjectType.Number -> endIndex = findSymbolOrEnd(',', this.parseFromIndex, this.targetString)
            JsonObjectType.Null -> endIndex = findSymbolOrEnd(',', this.parseFromIndex, this.targetString)
            JsonObjectType.Bool -> endIndex = findSymbolOrEnd(',', this.parseFromIndex, this.targetString)

            JsonObjectType.Array -> {
                val indexes: Pair<Int, Int>? = findContentBetweenBraces('[', this.parseFromIndex, this.targetString)
                if (indexes != null) {
                    this.parseFromIndex = indexes.first + 1
                    endIndex = indexes.second
                }
                else
                    endIndex = null
            }
            JsonObjectType.Dict -> {
                val indexes: Pair<Int, Int>? = findContentBetweenBraces('{', this.parseFromIndex, this.targetString)
                if ( indexes != null ) {
                    this.parseFromIndex = indexes.first + 1
                    endIndex = indexes.second
                }
                else
                    endIndex = null
            }

            else -> endIndex = null

        }

        if( endIndex == null )
            return null

        var subStr: String = this.targetString.substring(this.parseFromIndex, endIndex)
        if( type == JsonObjectType.Str )
            subStr = subStr.trim('"')

        subStr = subStr.trim()

        this.parseFromIndex = endIndex

        return Pair(type, subStr)
    }

    private fun findStartOfValue(): Boolean {
        var index: Int? = findSymbol(':', this.parseFromIndex, this.targetString)
        if( index == null )
            return false

        index = findNonSpaceSymbol(index + 1, this.targetString)

        if( index == null )
            return false

        this.parseFromIndex = index
        return true
    }

    private fun findStartOfKey() : Unit {
        val startOfNextKeyQuote: Int? = findSymbol('"', this.parseFromIndex, this.targetString)
        if( startOfNextKeyQuote == null ) {
            this.parseFromIndex = this.targetString.length
        } else {
            this.parseFromIndex = startOfNextKeyQuote
        }

    }
}
