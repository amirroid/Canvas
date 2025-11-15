package ir.amirroid.canvas.domain.usecase

import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.repository.CanvasRepository
import ir.amirroid.canvas.domain.repository.FileRepository
import ir.amirroid.canvas.domain.repository.PaintRepository
import me.tatarka.inject.annotations.Inject

@Inject
class CreateNewPaintUseCase(
    private val paintRepository: PaintRepository,
    private val canvasRepository: CanvasRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(name: String, fileUri: String) {
        fileRepository.takeFilePermission(fileUri)

        val document = CanvasDocument(name = name, elements = emptyList())
        canvasRepository.saveCanvasDocument(fileUri, document)

        paintRepository.addNewPaint(fileUri)
    }
}