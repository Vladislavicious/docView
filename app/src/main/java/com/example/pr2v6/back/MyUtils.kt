
fun getRandomString(length: Int): String {
    val charset = ('a'..'z') + ('A'..'Z')
    return List(length) { charset.random() }.joinToString("")
}

fun strIsLitsOnly(str: String): Boolean {
    val charset = ('a'..'z') + ('A'..'Z') + ('а'..'я') + ('А'..'Я') + (' ') + ('\n')
    for (char in str.trim(' ', '\n')) {
        if (!charset.contains(char)) {
            return false
        }
    }
    return true
}