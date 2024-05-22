package org.example

data class Word(
    val original: String,
    val translate: String,
    val answersCount: Int = 0,
) {
    companion object{
        fun fromString(str: String, separator: String = "|"): Word? {
            try {
                val (original, translate, answersCount) = str.split(separator)
                return Word(original, translate, answersCount.toInt())
            } catch (e: Exception) {
                return null
            }
        }
    }
}
