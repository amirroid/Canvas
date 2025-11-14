package ir.amirroid.canvas.data.repository

import ir.amirroid.canvas.data.database.dao.PaintDao
import ir.amirroid.canvas.data.database.entity.PaintEntity
import ir.amirroid.canvas.data.file.DocumentStorage
import ir.amirroid.canvas.data.mappers.paint.toDomain
import ir.amirroid.canvas.data.mappers.paint.toEntity
import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.models.PaintItem
import ir.amirroid.canvas.domain.repository.PaintRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class PaintRepositoryImpl(
    private val dao: PaintDao,
    private val json: Json,
    private val documentStorage: DocumentStorage
) : PaintRepository {
    override fun getAllPaints(): Flow<List<PaintItem>> {
        return dao.getAllPaints().map { entities -> entities.map { entity -> entity.toDomain() } }
    }

    override fun getCanvasDocument(fileUri: String): CanvasDocument? {
        return runCatching {
            val bytes = documentStorage.read(fileUri)
            json.decodeFromString<CanvasDocument>(bytes.decodeToString())
        }.getOrNull()
    }

    override suspend fun saveCanvasDocument(
        fileUri: String,
        document: CanvasDocument
    ) {
        val bytes = json.encodeToString(document).encodeToByteArray()
        documentStorage.write(fileUri, bytes)
    }

    override fun takeFilePermission(fileUri: String) {
        documentStorage.takePermission(fileUri)
    }

    override suspend fun addNewPaint(fileUri: String) {
        dao.addNewPaint(
            paintEntity = PaintEntity(
                fileUri = fileUri
            )
        )
    }
}