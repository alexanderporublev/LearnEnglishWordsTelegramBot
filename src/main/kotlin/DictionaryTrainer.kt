package org.example

import java.io.File

data class Statistics(
    val learned: Int,
    val total: Int,
    val percent: Double,
) {
    fun print() =  println("Выучено $learned из ${total} слов | ${"%.2f".format(percent)}%")
}

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class DictionaryTrainer {
    val dictionary = mutableListOf<Word>()

    fun loadDictionary() {
        val file = File(FILE_NAME)
        file.forEachLine {
            Word.fromString(it)?.let { dictionary.add(it) }
        }
    }

    fun storeDictionary() {
        val file = File(FILE_NAME)
        file. writeText("")
        dictionary.forEach {
            file.appendText("${it.toString(SEPARATOR)}\n")
        }
    }

    fun getStatistics(): Statistics {
        val learnedWordsCount = dictionary.count { it.answersCount >= REPEATS_COUNT_FOR_LEARN }
        val percent = learnedWordsCount.toDouble() / dictionary.size * 100
        return Statistics(learnedWordsCount, dictionary.size, percent)
    }

    fun getNextQuestion(): Question? {
        val allUnlearnedWords = dictionary.filter { it.answersCount < REPEATS_COUNT_FOR_LEARN }
        if (allUnlearnedWords.isEmpty()) {
            println("Вы выучили все слова")
            return null
        }
        val variants = dictionary.getUnlearnedWords()
        val wordToLearn = variants.filter { it.answersCount < REPEATS_COUNT_FOR_LEARN }.random()
        return Question(variants, wordToLearn)
    }

    fun checkAnswerForQuestion(question: Question, answer: Int): Boolean{
        if (question.variants[answer] != question.correctAnswer)
            return false

        question.correctAnswer.answersCount++
        storeDictionary()
        return true
    }
}