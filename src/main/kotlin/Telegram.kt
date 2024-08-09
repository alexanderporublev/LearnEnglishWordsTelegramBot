package org.example

enum class ChatState {INIT, MENU, QUESTION}
fun getChatsWhichSentCommand(updates: String, command: String): String? {
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val matchTextResult: MatchResult? = messageTextRegex.find(updates)
    if (matchTextResult?.groups?.get(1)?.value == command) {
        val chatIdRegex: Regex = "\"chat\":\\{\"id\":(\\d+),".toRegex()
        val matchChatIdResult: MatchResult? = chatIdRegex.find(updates)
        return matchChatIdResult?.groups?.get(1)?.value
    }
    return null
}

fun getCallbackData(updates: String): String? {
    val callbackTextRegex: Regex = "\"callback_data\":\"(.+?)\"".toRegex()
    val matchTextResult: MatchResult? = callbackTextRegex.find(updates)
    return matchTextResult?.groups?.get(1)?.value
}

fun getLastUpdateId(updates: String): Int {
    val updateTextRegex: Regex = "\"update_id\":\"(.+?)\"".toRegex()
    val matchTextResult: MatchResult? = updateTextRegex.find(updates)
    val updateStr = matchTextResult?.groups?.get(1)?.value
    println(updateStr)
    if (updateStr != null)
        return  updateStr.toInt() + 1
    else
        return 0
}


fun checkNextQuestionAndSend(trainer: DictionaryTrainer, botService: TelegramBotService, chatId: String) {
    val question = trainer.getNextQuestion()
    if (question != null) {
        botService.sendQuestion(chatId, question)
    }
    else {
        botService.sendMessage(chatId, "Вы выучили все слова")
    }
}

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val botService = TelegramBotService(botToken)
    var state = ChatState.INIT
    val dictionaryTrainer = DictionaryTrainer()
    var chatId: String? = null
    while (true) {
        try {
            Thread.sleep(2000)
            val updates: String = botService.getUpdates(updateId)
            println(updates)
            updateId = getLastUpdateId(updates)
            when (state) {
                ChatState.INIT -> {
                    chatId = getChatsWhichSentCommand(updates, "menu")
                    if (chatId != null) {
                        botService.sendMenu(chatId)
                    }
                    state = ChatState.MENU
                }
                ChatState.MENU -> {
                    val callbackResult = getCallbackData(updates)
                    when (callbackResult) {
                        "LEARN_WORD_BUTTON_CLICKED" -> {
                            if (chatId != null){
                                checkNextQuestionAndSend(dictionaryTrainer, botService, chatId)
                            }
                        }
                        "STATISTICS_BUTTON_CLICKED" -> {
                            if (chatId != null) {
                                botService.sendMessage(chatId, dictionaryTrainer.getStatistics().asString())
                                botService.sendMenu(chatId)
                            }
                        }
                    }
                }
                ChatState.QUESTION -> {

                }
            }
        } catch (e: Exception) {
            continue
        }
    }
}

