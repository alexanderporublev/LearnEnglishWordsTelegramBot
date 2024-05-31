package org.example

const val MENU_LEARN_WORDS = "1"
const val MENU_STATISTICS = "2"
const val MENU_EXIT = "0"
const val FILE_NAME = "words.txt"
const val SEPARATOR = "|"

fun showMenu() = println("Меню: $MENU_LEARN_WORDS – Учить слова, $MENU_STATISTICS – Статистика, $MENU_EXIT – Выход")

fun learnOneWord(dictionaryTrainer: DictionaryTrainer) {
    val question = dictionaryTrainer.getNextQuestion()
    if (question == null) {
        println("Вы выучили все слова")
        return
    }
    println(question.correctAnswer.original)
    println(question.variants.mapIndexed { index, word -> "${index + 1}.${word.translate}" }
        .joinToString(separator = " ", postfix = " 0.В главное меню"))

    print("Ваш ответ: ")
    try {
        val answer = readln().toInt()
        if (answer == 0)
            return

        if (dictionaryTrainer.checkAnswerForQuestion(question, answer - 1))
            println("Вы дали верный ответ")
        else
            println("К сожалению, это неправильный ответ.")
    } catch (e: Exception) {
        println("Неверный ввод.")
    }
}


fun main() {
    val trainer = DictionaryTrainer()
    trainer.loadDictionary()
    while (true) {
        showMenu()
        when (readln()) {
            MENU_LEARN_WORDS -> learnOneWord(trainer)
            MENU_STATISTICS -> trainer.getStatistics().print()
            MENU_EXIT -> return
            else -> println("Неверный ввод")
        }
    }
}