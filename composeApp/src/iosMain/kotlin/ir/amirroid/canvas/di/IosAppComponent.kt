package ir.amirroid.canvas.di

import me.tatarka.inject.annotations.Component
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

//@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class IosAppComponent : AppComponent()