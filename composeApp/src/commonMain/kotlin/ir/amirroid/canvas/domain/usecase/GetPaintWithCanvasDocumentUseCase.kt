package ir.amirroid.canvas.domain.usecase

import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.domain.repository.CanvasRepository
import ir.amirroid.canvas.domain.repository.PaintRepository
import me.tatarka.inject.annotations.Inject

@Inject
class GetPaintWithCanvasDocumentUseCase(
    private val paintRepository: PaintRepository,
    private val canvasRepository: CanvasRepository
) {
    suspend operator fun invoke(
        paintId: Long
    ): PaintWithCanvasDocument? {
        val paint = paintRepository.getPaint(paintId)
        val document = canvasRepository.getCanvasDocument(paint.fileUri)
        return document?.let {
            PaintWithCanvasDocument(
                paint = paint,
                document = document
            )
        }
    }
}