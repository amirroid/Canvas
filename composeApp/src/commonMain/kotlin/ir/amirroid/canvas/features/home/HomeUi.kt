package ir.amirroid.canvas.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import canvas.composeapp.generated.resources.Res
import canvas.composeapp.generated.resources.home
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@CircuitInject(HomeScreen::class, AppScope::class)
class HomeUi : Ui<HomeScreen.State> {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun Content(
        state: HomeScreen.State,
        modifier: Modifier
    ) {
        HomeContent(state, modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(state: HomeScreen.State, modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    state.eventSink.invoke(HomeScreen.Event.NavigateToAddNewPaint)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.home))
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(state.paints, key = { it.paint.id }) { paintWthDocument ->
                    Card(modifier = Modifier.fillMaxWidth(), onClick = {
                        state.eventSink.invoke(HomeScreen.Event.ClickPaint(paintWthDocument.paint.id))
                    }) {
                        Text(paintWthDocument.document.name, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}