package dev.johnoreilly.confetti.wear.conferences

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.google.android.horologist.composables.PlaceholderChip
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults.ItemType
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.Chip
import dev.johnoreilly.confetti.BuildConfig
import dev.johnoreilly.confetti.GetConferencesQuery
import dev.johnoreilly.confetti.decompose.ConferencesComponent
import dev.johnoreilly.confetti.wear.components.ScreenHeader
import dev.johnoreilly.confetti.wear.preview.TestFixtures
import dev.johnoreilly.confetti.wear.ui.ConfettiTheme
import dev.johnoreilly.confetti.wear.ui.toColor

@Composable
fun ConferencesRoute(
    component: ConferencesComponent,
) {
    val uiState by component.uiState.subscribeAsState()

    if (!BuildConfig.DEBUG) {
        ReportDrawnWhen {
            uiState !is ConferencesComponent.Loading
        }
    }

    ConferencesView(
        uiState = uiState,
        navigateToConference = { conference ->
            component.onConferenceClicked(conference)
        }
    )
}

@Composable
fun ConferencesView(
    uiState: ConferencesComponent.UiState,
    navigateToConference: (GetConferencesQuery.Conference) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnState: ScalingLazyColumnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ItemType.Text,
            last = ItemType.Chip
        )
    )

    ScreenScaffold(scrollState = columnState) {
        ScalingLazyColumn(
            modifier = modifier.fillMaxSize(), columnState = columnState
        ) {
            item {
                ScreenHeader("Conferences")
            }

            when (uiState) {
                is ConferencesComponent.Loading -> {
                    items(5) {
                        PlaceholderChip(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ChipDefaults.secondaryChipColors()
                        )
                    }
                }

                is ConferencesComponent.Success -> {
                    // TODO show current year
                    items(uiState.relevantConferences) { conference ->
                        ConferencesChip(conference, navigateToConference)
                    }
                }

                else -> {
                    // TODO
                }
            }
        }
    }
}

@Composable
private fun ConferencesChip(
    conference: GetConferencesQuery.Conference,
    navigateToConference: (GetConferencesQuery.Conference) -> Unit
) {
    println(conference.themeColor)

    val seedColor = conference.themeColor?.toColor()

    ConfettiTheme(seedColor = seedColor) {
        Chip(
            modifier = Modifier.fillMaxWidth(),
            label = conference.name,
            onClick = {
                navigateToConference(conference)
            },
            colors = ChipDefaults.secondaryChipColors()
        )
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun ConferencesViewPreview() {
    ConferencesView(
        uiState = ConferencesComponent.Success(
            TestFixtures.conferences.groupBy { it.days[0].year }
        ),
        navigateToConference = {},
    )
}
