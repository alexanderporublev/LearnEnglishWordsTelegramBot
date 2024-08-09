package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TelegramBotService(
    val botToken: String,
) {
    private val client = HttpClient.newBuilder().build()

    private fun request(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun getUpdates(updateId: Int): String = request("https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId")


    fun sendMessage(chatId: String, message: String): String =
        request("https://api.telegram.org/bot$botToken/sendMessage?chat_id=$chatId&text=$message")

    fun sendMenu(chatId: String): String {
        val url = "https://api.telegram.org/bot$botToken/sendMessage"
        val sendMenuBody = """
            {
                "chat_id": $chatId,
                "text": "Основное меню",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            {
                                "text": "Изучить слова",
                                "callback_data": "LEARN_WORD_BUTTON_CLICKED"
                            },
                            {
                                "text": "Статистика",
                                "callback_data": "STATISTICS_BUTTON_CLICKED"
                            },
                            {
                                "text": "Выход",
                                "callback_data": "EXIT_BUTTON_CLICKED"
                            }
                        ]
                    ]
                }
            }
        """.trimIndent()
        val request = HttpRequest.newBuilder().uri(URI.create(url))
            .headers("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()

        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun sendQuestion(chatId: String, question: Question): String {
        val url = "https://api.telegram.org/bot$botToken/sendMessage"
        val wordListJsonObjectList = question.variants.shuffled().map {
            """
                {
                    "text": ${it.translate},
                    "callback_data": "ANSWER_${it.translate}"
                }
            """.trimIndent()
        }
        val sendQuestionBody = """
            {
                "chat_id": $chatId,
                "text": ${question.correctAnswer.original},
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            ${wordListJsonObjectList[0]},
                            ${wordListJsonObjectList[1]},
                        ],
                        [
                            ${wordListJsonObjectList[2]},
                            ${wordListJsonObjectList[3]},
                        ]
                    ]
                }
            }
        """.trimIndent()
        val request = HttpRequest.newBuilder().uri(URI.create(url))
            .headers("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendQuestionBody))
            .build()

        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

}