package com.coco0o0o.artcart.feature.saved.impl

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.component.ArtCartButton
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.DraggableScrollbar
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.coco0o0o.artcart.core.designsystem.component.scrollbar.scrollbarState
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.LocationsItem

@Composable
fun SavedListContent(
    locations: List<ArtLocation>,
    onLocationClick: (String) -> Unit,
    onCheckoutClicked: () -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
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
            modifier = Modifier,
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
                        onClick = { onLocationClick(artLocationId) },
                        onSaveButtonClick = { onFollowButtonClick(artLocationId, it) },
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
        if (locations.size > 1) {
            ArtCartButton(
                onClick = { onCheckoutClicked() },
                text = { Text("Check out your optimized route ->") },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
            )
        }
    }
}