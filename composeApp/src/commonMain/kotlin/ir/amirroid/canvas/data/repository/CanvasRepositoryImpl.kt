package ir.amirroid.canvas.data.repository

import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.repository.CanvasRepository
import ir.amirroid.canvas.domain.repository.FileRepository
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class CanvasRepositoryImpl(
    private val fileRepository: FileRepository,
    private val json: Json
) : CanvasRepository {
    override suspend fun getCanvasDocument(fileUri: String): CanvasDocument? {
        return runCatching {
            val bytes = fileRepository.readFileContent(fileUri)
            json.decodeFromString<CanvasDocument>(bytes.decodeToString())
        }.getOrNull()
    }

    override suspend fun saveCanvasDocument(
        fileUri: String,
        document: CanvasDocument
    ) {
        val bytes = json.encodeToString(document).encodeToByteArray()
        fileRepository.writeFileContent(fileUri, bytes)
    }

}