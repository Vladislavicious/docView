import android.content.Context
import java.io.File

class FileWorker(private val context: Context) {


    fun deleteFile(filePath: String): Boolean {
        if( filePath in context.fileList() )
            return context.deleteFile(filePath)
        return true
    }

    fun readFileContents( fileName: String ): String? {
        val file = File(context.filesDir, fileName)
        if( file.length() == 0L )
            return null
        return file.readText()

    }

    fun truncateFile(filePath: String ): Boolean {
        if( !deleteFile(filePath) )
            return false

        return writeInFile(filePath, "")
    }

    fun writeInFile(filePath: String, str: String): Boolean {
        context.openFileOutput(filePath, Context.MODE_PRIVATE).use {
            it.write(str.toByteArray())
            return true
        }
        return false
    }
}






