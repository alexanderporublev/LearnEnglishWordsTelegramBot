package org.example

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

fun List<Word>.haveAllWordsBeenLearned(): Boolean = count { it.answersCount < REPEATS_COUNT_FOR_LEARN } == 0
