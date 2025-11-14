package ir.amirroid.canvas.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


abstract class AppComponent {
    @SingleIn(AppScope::class)
    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @SingleIn(AppScope::class)
    @Provides
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>
    ): Circuit {
        return Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .build()
    }

    abstract val circuit: Circuit
}