package ir.amirroid.canvas.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.amirroid.canvas.data.database.AppDatabase
import ir.amirroid.canvas.utils.Constants
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface AndroidDatabaseComponent {
    @Provides
    fun provideDatabaseBuilder(
        context: Context
    ): RoomDatabase.Builder<AppDatabase> {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = Constants.DB_NAME
        )
    }
}