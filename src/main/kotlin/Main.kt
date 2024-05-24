package org.example

import java.io.File

const val MENU_LEARN_WORDS = "1"
const val MENU_STATISTICS = "2"
const val MENU_EXIT = "0"
const val REPEATS_COUNT_FOR_LEARN = 3
const val VARIANTS_COUNT = 4
const val FILE_NAME="words.txt"
const val SEPARATOR="|"

fun showMenu() = println("Меню: $MENU_LEARN_WORDS – Учить слова, $MENU_STATISTICS – Статистика, $MENU_EXIT – Выход")

fun showStatistics(wordList: List<Word>) {
    val learnedWordsCount = wordList.count { it.answersCount >= REPEATS_COUNT_FOR_LEARN }
    val percent = learnedWordsCount.toDouble() / wordList.size * 100
    println("Выучено $learnedWordsCount из ${wordList.size} слов | ${"%.2f".format(percent)}%")
}

fun List<Word>.haveAllWordsBeenLearned(): Boolean = count { it.answersCount < REPEATS_COUNT_FOR_LEARN } == 0

fun List<Word>.getUnlearnedWords(count: Int = VARIANTS_COUNT): List<Word> {
    val allUnlearnedWords = filter { it.answersCount < REPEATS_COUNT_FOR_LEARN }
    val result = allUnlearnedWords.shuffled().take(count)
    if (result.size == count)
        return result

    return (result + filter { it.answersCount >= REPEATS_COUNT_FOR_LEARN }
        .shuffled()
        .take(count - result.size)
            ).shuffled()
}

fun learnOneWord(words: MutableList<Word>): Boolean {
    val allUnlearnedWords = words.filter { it.answersCount < REPEATS_COUNT_FOR_LEARN }
    if (allUnlearnedWords.isEmpty()) {
        println("Вы выучили все слова")
        return false
    }
    val variants = words.getUnlearnedWords()
    val wordToLearn = variants.filter { it.answersCount < REPEATS_COUNT_FOR_LEARN }.random()
    println(wordToLearn.original)
    println(variants.mapIndexed { index, word -> "${index + 1}.${word.translate}" }
        .joinToString(separator = " ", postfix = " 0.В главное меню"))

    val rightAnswer = (variants.indexOf(wordToLearn) + 1).toString()

    print("Ваш ответ: ")
        when(readln()) {
            "0" -> return false
            rightAnswer -> {
                println("Вы дали верный ответ")
                words.forEach { if (it.original == wordToLearn.original) it.answersCount++ }
                storeAnswer(words)
                return true
            }
            else -> {
                println("К сожалению, это неправильный ответ.")
                return false
            }
        }
}

fun storeAnswer(words: List<Word>) {
    val file = File(FILE_NAME)
    file.delete()
    file.createNewFile()
    words.forEach {
        file.appendText("${it.toString(SEPARATOR)}\n")
    }
}

fun main() {
    val file = File(FILE_NAME)
    val wordList = mutableListOf<Word>()
    file.forEachLine {
        Word.fromString(it)?.let { wordList.add(it) }
    }

    while (true) {
        showMenu()
        when (readln()) {
            MENU_LEARN_WORDS -> learnOneWord(wordList)
            MENU_STATISTICS -> showStatistics(wordList)
            MENU_EXIT -> return
            else -> println("Неверный ввод")
        }
    }
}