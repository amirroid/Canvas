package ir.amirroid.canvas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import ir.amirroid.canvas.data.database.entity.PaintEntity
import ir.amirroid.canvas.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface PaintDao {
    @Query("SELECT * FROM ${Constants.PAINT_ENTITY}")
    fun getAllPaints(): Flow<List<PaintEntity>>

    @Query("SELECT * FROM ${Constants.PAINT_ENTITY} WHERE id = :id")
    fun getPaint(id: Long): PaintEntity

    @Insert(onConflict = IGNORE)
    suspend fun addNewPaint(paintEntity: PaintEntity)
}