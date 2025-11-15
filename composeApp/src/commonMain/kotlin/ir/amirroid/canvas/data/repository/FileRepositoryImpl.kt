package ir.amirroid.canvas.data.repository

import ir.amirroid.canvas.data.file.DocumentStorage
import ir.amirroid.canvas.domain.repository.FileRepository
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class FileRepositoryImpl(
    private val storage: DocumentStorage
) : FileRepository {
    override fun takeFilePermission(fileUri: String) {
        storage.takePermission(fileUri)
    }

    override fun writeFileContent(fileUri: String, content: ByteArray) {
        storage.write(fileUri, content)
    }

    override fun readFileContent(fileUri: String): ByteArray = storage.read(fileUri)
}