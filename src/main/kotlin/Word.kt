package org.example

data class Word(
    val original: String,
    val translate: String,
    var answersCount: Int = 0,
) {
    fun toString(separator: String): String = "$original$separator$translate$separator$answersCount"

    companion object{
        fun fromString(str: String, separator: String = SEPARATOR): Word? {
            try {
                val (original, translate, answersCount) = str.split(separator)
                return Word(original, translate, answersCount.toInt())
            } catch (e: Exception) {
                return null
            }
        }
    }
}
