package ir.amirroid.canvas.domain.repository

interface FileRepository {
    fun takeFilePermission(fileUri: String)
    fun writeFileContent(fileUri: String, content: ByteArray)
    fun readFileContent(fileUri: String): ByteArray
}