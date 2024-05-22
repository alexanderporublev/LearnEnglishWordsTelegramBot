package org.example

import java.io.File

const val MENU_LEARN_WORDS = "1"
const val MENU_STATISTICS = "2"
const val MENU_EXIT = "0"
const val REPEATS_COUNT_FOR_LEARN = 3

fun showMenu() = println("Меню: $MENU_LEARN_WORDS – Учить слова, $MENU_STATISTICS – Статистика, $MENU_EXIT – Выход")

fun showStatistics(wordList: List<Word>) {
    val learnedWordsCount = wordList.count { it.answersCount >= REPEATS_COUNT_FOR_LEARN }
    val percent = learnedWordsCount.toDouble() / wordList.size * 100
    println("Выучено $learnedWordsCount из ${wordList.size} слов | ${"%.2f".format(percent)}%")
}

fun main() {
    val file = File("words.txt")
    val wordList = mutableListOf<Word>()
    file.readLines().forEach {
        Word.fromString(it)?.let { wordList.add(it) }
    }

    wordList.forEach { println(it) }

    while (true) {
        showMenu()
        when (readln()) {
            MENU_LEARN_WORDS -> println("Учить слова")
            MENU_STATISTICS -> showStatistics(wordList)
            MENU_EXIT -> return
            else -> println("Неверный ввод")
        }
    }
}