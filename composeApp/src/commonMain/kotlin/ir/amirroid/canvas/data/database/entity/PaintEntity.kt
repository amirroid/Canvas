package ir.amirroid.canvas.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.amirroid.canvas.utils.Constants

@Entity(tableName = Constants.PAINT_ENTITY)
data class PaintEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileUri: String
)