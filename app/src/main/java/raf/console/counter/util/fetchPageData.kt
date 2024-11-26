package raf.console.counter.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup

fun fetchPageData(url: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()

    return try {
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string() // Получаем HTML-строку
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
