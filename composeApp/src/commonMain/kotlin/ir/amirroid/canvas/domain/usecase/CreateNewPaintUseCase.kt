package ir.amirroid.canvas.domain.usecase

import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.repository.PaintRepository
import me.tatarka.inject.annotations.Inject

@Inject
class CreateNewPaintUseCase(
    private val paintRepository: PaintRepository
) {
    suspend operator fun invoke(name: String, fileUri: String) {
        paintRepository.takeFilePermission(fileUri)

        val document = CanvasDocument(name = name, elements = emptyList())
        paintRepository.saveCanvasDocument(fileUri, document)

        paintRepository.addNewPaint(fileUri)
    }
}