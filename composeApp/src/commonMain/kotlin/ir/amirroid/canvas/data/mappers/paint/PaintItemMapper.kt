package ir.amirroid.canvas.data.mappers.paint

import ir.amirroid.canvas.data.database.entity.PaintEntity
import ir.amirroid.canvas.domain.models.PaintItem

fun PaintItem.toEntity() = PaintEntity(
    id = id,
    fileUri = fileUri
)