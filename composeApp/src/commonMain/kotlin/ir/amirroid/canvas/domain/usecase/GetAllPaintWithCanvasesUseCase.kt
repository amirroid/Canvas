package ir.amirroid.canvas.domain.usecase

import ir.amirroid.canvas.domain.models.PaintWithCanvasDocument
import ir.amirroid.canvas.domain.repository.CanvasRepository
import ir.amirroid.canvas.domain.repository.PaintRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class GetAllPaintWithCanvasesUseCase(
    private val paintRepository: PaintRepository,
    private val canvasRepository: CanvasRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke() = paintRepository.getAllPaints()
        .flowOn(dispatcher)
        .map { paints ->
            paints.mapNotNull { paintItem ->
                canvasRepository.getCanvasDocument(paintItem.fileUri)?.let { document ->
                    PaintWithCanvasDocument(
                        paint = paintItem,
                        document = document
                    )
                }
            }
        }
}