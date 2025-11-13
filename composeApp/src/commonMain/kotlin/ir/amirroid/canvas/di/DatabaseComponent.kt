package ir.amirroid.canvas.di

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.amirroid.canvas.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseComponent {
    @Provides
    fun provideSqliteDriver(): SQLiteDriver = BundledSQLiteDriver()

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppDatabase(
        builder: RoomDatabase.Builder<AppDatabase>,
        driver: SQLiteDriver
    ) = builder.setDriver(driver).setQueryCoroutineContext(Dispatchers.IO).build()
}