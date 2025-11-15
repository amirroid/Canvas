package ir.amirroid.canvas.domain.repository

import ir.amirroid.canvas.domain.models.PaintItem
import kotlinx.coroutines.flow.Flow

interface PaintRepository {
    fun getAllPaints(): Flow<List<PaintItem>>
    suspend fun getPaint(paintId: Long): PaintItem
    suspend fun addNewPaint(fileUri: String)
}