package ir.amirroid.canvas.domain.repository

import ir.amirroid.canvas.domain.models.CanvasDocument


interface CanvasRepository {
    suspend fun getCanvasDocument(fileUri: String): CanvasDocument?
    suspend fun saveCanvasDocument(fileUri: String, document: CanvasDocument)
}