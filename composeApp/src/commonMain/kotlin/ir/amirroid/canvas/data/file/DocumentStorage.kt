package ir.amirroid.canvas.data.file

interface DocumentStorage {
    fun read(uri: String): ByteArray
    fun write(uri: String, content: ByteArray)
    fun takePermission(uri: String)
}