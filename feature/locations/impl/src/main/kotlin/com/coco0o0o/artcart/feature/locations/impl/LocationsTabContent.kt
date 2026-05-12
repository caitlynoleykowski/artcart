package com.coco0o0o.artcart.feature.locations.impl

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.DraggableScrollbar
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.scrollbarState
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.LocationsItem

@Composable
fun LocationsTabContent(
    locations: List<ArtLocation>,
    onLocationClick: (String) -> Unit,
    onSaveClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    withBottomSpacer: Boolean = true,
    selectedLocationId: String? = null,
    shouldHighlightSelectedLocation: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val scrollableState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .testTag(LIST_PANE_TEST_TAG),
            contentPadding = PaddingValues(vertical = 16.dp),
            state = scrollableState,
        ) {
            locations.forEach { artLocation ->
                val artLocationId = artLocation.id
                item(key = artLocationId) {
                    val isSelected = shouldHighlightSelectedLocation && artLocationId == selectedLocationId
                    LocationsItem(
                        name = artLocation.name,
                        following = artLocation.isFavorite,
                        description = artLocation.address,
                        secondaryDescription = artLocation.primaryTypeName,
                        locationImageUrl = artLocation.photoUrl,
                        onClick = {
                            onLocationClick(artLocationId)
                                  },
                        onFollowButtonClick = {
                            onSaveClick(artLocationId, it)
                                              },
                        isSelected = isSelected,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            if (withBottomSpacer) {
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
        val scrollbarState = scrollableState.scrollbarState(
            itemsAvailable = locations.size,
        )
        scrollableState.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = scrollableState.rememberDraggableScroller(
                itemsAvailable = locations.size,
            ),
        )
    }
}

val LIST_PANE_TEST_TAG = "locations:locations"