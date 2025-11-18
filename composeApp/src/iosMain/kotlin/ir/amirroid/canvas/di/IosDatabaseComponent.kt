package ir.amirroid.canvas.di

import androidx.room.Room
import androidx.room.RoomDatabase
import ir.amirroid.canvas.data.database.AppDatabase
import ir.amirroid.canvas.utils.Constants
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface IosDatabaseComponent {
    @Provides
    fun provideDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        return Room.databaseBuilder(
            name = Constants.DB_NAME
        )
    }
}