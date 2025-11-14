package ir.amirroid.canvas.data.file

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesBinding(AppScope::class)
@Inject
@SingleIn(AppScope::class)
class AndroidDocumentStorage(private val context: Context) : DocumentStorage {

    override fun read(uri: String): ByteArray {
        val parsed = uri.toUri()
        val inputStream = context.contentResolver.openInputStream(parsed)
            ?: throw IllegalArgumentException("Cannot open InputStream for URI: $uri")
        return inputStream.use { it.readBytes() }
    }

    override fun write(uri: String, content: ByteArray) {
        val parsed = uri.toUri()
        val outputStream = context.contentResolver.openOutputStream(parsed, "rwt")
            ?: throw IllegalArgumentException("Cannot open OutputStream for URI: $uri")
        outputStream.use { it.write(content) }
    }

    override fun takePermission(uri: String) {
        context.contentResolver.takePersistableUriPermission(
            uri.toUri(),
            Intent.FLAG_GRANT_READ_URI_PERMISSION.or(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        )
    }
}