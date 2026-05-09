package ua.androstav.kaisafe.utils

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileLogger {
    private const val FILE_NAME = "nausafe_analytics.log"

    fun log(context: Context, tag: String, message: String) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logEntry = "$timestamp [$tag]: $message\n"

            // Запис у файл у внутрішній пам'яті (доступний тільки додатку)
            val file = File(context.filesDir, FILE_NAME)
            val writer = FileWriter(file, true) // true = додавати в кінець файлу (append)
            writer.append(logEntry)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}