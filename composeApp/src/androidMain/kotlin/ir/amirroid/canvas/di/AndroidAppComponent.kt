package ir.amirroid.canvas.di

import android.content.Context
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class AndroidAppComponent(
    @get:Provides val context: Context
): AppComponent(), AndroidAppComponentMerged