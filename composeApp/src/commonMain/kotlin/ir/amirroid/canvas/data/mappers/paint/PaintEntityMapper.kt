package ir.amirroid.canvas.data.mappers.paint

import ir.amirroid.canvas.data.database.entity.PaintEntity
import ir.amirroid.canvas.domain.models.PaintItem

fun PaintEntity.toDomain() = PaintItem(
    id = id,
    fileUri = fileUri
)