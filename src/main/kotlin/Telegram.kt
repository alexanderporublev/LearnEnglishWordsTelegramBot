package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client = HttpClient.newBuilder().build()

    val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

    return response.body()
}

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0

    while (true){
        try {
            Thread.sleep(2000)
            val updates: String = getUpdates(botToken, updateId)
            println(updates)
            updateId = getLastUpdateId(updates)
        } catch (e: Exception){
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
