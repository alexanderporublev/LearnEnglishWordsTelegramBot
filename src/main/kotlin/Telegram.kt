package org.example

fun getChatsWhichSentCommand(updates: String, command: String): String? {
    val result = emptyList<String>().toMutableList()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val matchTextResult: MatchResult? = messageTextRegex.find(updates)
    if (matchTextResult?.groups?.get(1)?.value == command) {
        val chatIdRegex: Regex = "\"chat\":\\{\"id\":(\\d+),".toRegex()
        val matchChatIdResult: MatchResult? = chatIdRegex.find(updates)
        return matchChatIdResult?.groups?.get(1)?.value
    }
    return null
}

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val botService = TelegramBotService(botToken)
    while (true) {
        try {
            Thread.sleep(2000)
            val updates: String = botService.getUpdates(updateId)
            println(updates)
            updateId = getLastUpdateId(updates)
            val chat = getChatsWhichSentCommand(updates, "hello")
            if (chat != null)
                botService.sendMessage(chat, "Hello")
        } catch (e: Exception) {
            continue
        }
    }
}

fun getLastUpdateId(updates: String): Int {
    val startUpdateId = updates.lastIndexOf("\"update_id\":") + "\"update_id\":".length
    val endUpdateId = updates.indexOf(',', startUpdateId)
    val updateIdString = updates.substring(startUpdateId, endUpdateId)
    println(updateIdString)
    return updateIdString.toInt() + 1
}
