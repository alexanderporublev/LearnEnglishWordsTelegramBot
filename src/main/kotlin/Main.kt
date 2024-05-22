package org.example

import java.io.File

fun main() {
    val file = File("words.txt")
    val wordList = emptyList<Word>().toMutableList()
    file.readLines().forEach {
        val word = Word.fromString(it)
        if (word != null)
            wordList.add(word)
    }

    wordList.forEach { println(it) }
}