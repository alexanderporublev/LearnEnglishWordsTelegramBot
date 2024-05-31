package org.example

import java.io.File

data class Statistics(
    val learned: Int,
    val total: Int,
    val percent: Double,
) {
    fun print() = println("Выучено $learned из ${total} слов | ${"%.2f".format(percent)}%")
}

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class DictionaryTrainer(
    val repeatsCountForLearn: Int = 3,
    val variantsCount: Int = 4,
) {
    val dictionary = mutableListOf<Word>()

    fun loadDictionary() {
        val file = File(FILE_NAME)
        file.forEachLine {
            Word.fromString(it)?.let { dictionary.add(it) }
        }
    }

    fun storeDictionary() {
        val file = File(FILE_NAME)
        file.writeText("")
        dictionary.forEach {
            file.appendText("${it.toString(SEPARATOR)}\n")
        }
    }

    fun getStatistics(): Statistics {
        val learnedWordsCount = dictionary.count { it.answersCount >= repeatsCountForLearn }
        val percent = learnedWordsCount.toDouble() / dictionary.size * 100
        return Statistics(learnedWordsCount, dictionary.size, percent)
    }

    fun getNextQuestion(): Question? {
        val allUnlearnedWords = dictionary.filter { it.answersCount < repeatsCountForLearn }
        if (allUnlearnedWords.isEmpty()) {
            println("Вы выучили все слова")
            return null
        }
        val variants = getUnlearnedWords(variantsCount)
        val wordToLearn = variants.filter { it.answersCount < repeatsCountForLearn }.random()
        return Question(variants, wordToLearn)
    }

    fun checkAnswerForQuestion(question: Question, answer: Int): Boolean {
        if (question.variants[answer] != question.correctAnswer)
            return false

        question.correctAnswer.answersCount++
        storeDictionary()
        return true
    }

    private fun getUnlearnedWords(count: Int = 4): List<Word> {
        val allUnlearnedWords = dictionary.filter { it.answersCount < repeatsCountForLearn }
        val result = allUnlearnedWords.shuffled().take(count)
        if (result.size == count)
            return result

        return (result + dictionary.filter { it.answersCount >= repeatsCountForLearn }
            .shuffled()
            .take(count - result.size)
                ).shuffled()
    }
}