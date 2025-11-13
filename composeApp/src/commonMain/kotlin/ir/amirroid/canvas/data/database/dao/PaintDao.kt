package ir.amirroid.canvas.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.amirroid.canvas.data.database.entity.PaintEntity
import ir.amirroid.canvas.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface PaintDao {
    @Query("SELECT * FROM ${Constants.PAINT_ENTITY}")
    fun getAllPaints(): Flow<List<PaintEntity>>

    @Upsert
    suspend fun upsertPaint(paintEntity: PaintEntity)
}