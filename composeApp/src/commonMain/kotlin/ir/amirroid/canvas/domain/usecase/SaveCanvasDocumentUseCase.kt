package ir.amirroid.canvas.domain.usecase

import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.repository.CanvasRepository
import me.tatarka.inject.annotations.Inject

@Inject
class SaveCanvasDocumentUseCase(
    private val canvasRepository: CanvasRepository
) {
    suspend operator fun invoke(fileUri: String, document: CanvasDocument) =
        canvasRepository.saveCanvasDocument(fileUri, document)
}