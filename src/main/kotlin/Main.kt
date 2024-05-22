package org.example

import java.io.File

fun main() {
    val file = File("words.txt")
    val wordList = mutableListOf<Word>()
    file.readLines().forEach {
        Word.fromString(it)?.let { wordList.add(it) }
    }

    wordList.forEach { println(it) }
}