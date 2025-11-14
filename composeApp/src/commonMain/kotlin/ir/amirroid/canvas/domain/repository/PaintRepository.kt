package ir.amirroid.canvas.domain.repository

import ir.amirroid.canvas.domain.models.CanvasDocument
import ir.amirroid.canvas.domain.models.PaintItem
import kotlinx.coroutines.flow.Flow

interface PaintRepository {
    fun getAllPaints(): Flow<List<PaintItem>>
    fun getCanvasDocument(fileUri: String): CanvasDocument?
    suspend fun addNewPaint(paint: PaintItem)
}