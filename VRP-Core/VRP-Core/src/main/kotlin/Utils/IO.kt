package VRPCore.Utils

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class IO {
    fun ReadAllLines(filePath: String?): String {
        val contentBuilder = StringBuilder()
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).use { stream ->
                stream.forEach { s: String? ->
                    contentBuilder.append(s).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return contentBuilder.toString()
    }
}