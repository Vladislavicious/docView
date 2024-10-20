import java.io.File
import java.io.IOException
import java.io.PrintWriter

fun deleteFile(filePath: String): Boolean {
    val file = File(filePath)
    if( !file.exists() )
        return true

    try {
        file.delete()
        return true
    } catch (e: IOException) {
        return false
    }
}

fun openOrCreateFile(filePath: String): File? {
    val file = File(filePath)
    if (!file.exists()) {
        try {
            file.createNewFile()
            return file
        } catch (e: IOException) {
            return null
        }
    }
    return file
}

fun readFileContents( fileName: String ): String? {
    val file = openOrCreateFile(fileName)?: return null

    return if (file.length() == 0L) {
        null
    } else {
        try {
            file.readText()
        } catch (e: IOException) {
            null
        }
    }
}

fun truncateFile( fileName: String ): Boolean {
    try {
        PrintWriter(fileName).use { }
    } catch (e: IOException) {
        return false
    }
    return true
}

fun writeInFile( fileName: String, str: String): Boolean {
    val file = openOrCreateFile(fileName)?: return false

    file.writeText(str)
    return true
}
