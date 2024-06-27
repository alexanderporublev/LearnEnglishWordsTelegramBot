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
}