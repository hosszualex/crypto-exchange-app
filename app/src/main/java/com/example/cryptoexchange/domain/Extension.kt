package com.example.cryptoexchange.domain

fun String.removeStrings(stringToRemove: Set<String>): String {
    var newString = this
    for (char in stringToRemove) {
        newString = newString.replace(char, "")
    }
    return newString
}

fun Set<String>.toStringForSymbols(): String {
    return this.toString().substring(1, this.toString().length - 1).removeWhitespaceRegex()
}

fun String.removeWhitespaceRegex(): String {
    val regex = Regex("\\s")
    return regex.replace(this, "")
}

fun Double.formatDailyRelativeChange(): String {
    return if (this > 0) {
        "+" + String.format("%.2f", this) + "%"
    } else {
        String.format("%.2f", this) + "%"
    }
}
