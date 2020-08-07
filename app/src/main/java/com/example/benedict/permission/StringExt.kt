package com.example.benedict.permission

fun String.remove_() : String {
    val re = Regex("[\\s]")
    return re.replace(this, "")
}

fun String.removeSpecialChar() : String {
    val re = Regex("[^A-Za-z0-9 ]")
    return re.replace(this, "")
}

fun String.removeSpecialChar_() : String {
    val re = Regex("[^A-Za-z0-9]")
    return re.replace(this, "")
}

fun String.getNumbersOnly() : String {
    val re = Regex("\\D+")
    return re.replace(this, "")
}

fun String.getLettersOnly() : String {
    val re = Regex("[^A-Za-z]")
    return re.replace(this, "")
}

fun String.getLettersPeriodOnly() : String {
    val re = Regex("[^A-Za-z. ]")
    return re.replace(this, "")
}

fun String.getLettersPeriodSwedishOnly() : String {
    val re = Regex("[^A-Za-z. ÅåÄäÖöØøÆæŒœÉé]")
    return re.replace(this, "")
}
//積分
fun String.intOrString() : Any {
    val v = toIntOrNull()
    return when(v) {
        null -> this
        else -> v
    }
}