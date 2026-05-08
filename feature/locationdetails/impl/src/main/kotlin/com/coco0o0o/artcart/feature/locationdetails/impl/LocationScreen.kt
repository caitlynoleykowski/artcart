package com.coco0o0o.artcart.feature.locationdetails.impl

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.DynamicAsyncImage
import com.coco0o0o.artcart.core.designsystem.component.ArtCartFilterChip
import com.coco0o0o.artcart.core.designsystem.component.ArtCartLoadingWheel
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.R as UiR
import com.coco0o0o.artcart.feature.locationdetails.api.R as LocationDetailsR
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import android.content.Intent
import androidx.compose.ui.draw.clip
import androidx.core.net.toUri
import com.coco0o0o.artcart.core.designsystem.component.ArtCartButton
import com.coco0o0o.artcart.core.designsystem.component.ArtCartIcon
import com.coco0o0o.artcart.core.designsystem.component.ArtCartIconButton
import com.coco0o0o.artcart.core.designsystem.component.InfoCard

@Composable
fun LocationScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = hiltViewModel(),
) {
    val locationUiState: LocationUiState by viewModel.locationUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(modifier) {
        // TODO: workaround full screen
        Spacer(modifier = Modifier.height(TopAppBarDefaults.TopAppBarExpandedHeight))

        LocationScreen(
            locationUiState = locationUiState,
            modifier = modifier.testTag("location:${viewModel.locationId}"),
            showBackButton = showBackButton,
            onBackClick = onBackClick,
            onFollowClick = viewModel::followLocationToggle,
            onLocationClick = {
                openGoogleMaps(context, it.latitude, it.longitude)
            },
        )
    }
}

fun openGoogleMaps(
    context: Context,
    lat: Double,
    lng: Double,
) {
    val uri = "geo:$lat,$lng?q=$lat,$lng".toUri()

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
private fun LocationScreen(
    locationUiState: LocationUiState,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    onLocationClick: (ArtLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (locationUiState) {
            LocationUiState.Loading -> {
                ArtCartLoadingWheel(
                    modifier = Modifier.align(Alignment.Center),
                    contentDesc = "",
                )
            }

            LocationUiState.Error -> {
                Text(
                    text = "Couldn’t load this art spot.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            is LocationUiState.Success -> {
                LocationDetailsContent(
                    location = locationUiState.location,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick,
                    onFollowClick = onFollowClick,
                    onLocationClick = onLocationClick,
                )
            }
        }
    }
}

@Composable
private fun LocationDetailsContent(
    location: ArtLocation,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    onLocationClick: (ArtLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            val imageShape = RoundedCornerShape(
                topStart = 28.dp,
                topEnd = 10.dp,
                bottomEnd = 28.dp,
                bottomStart = 10.dp,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(imageShape)
            ) {
                DynamicAsyncImage(
                    imageUrl = location.photoUrl,
                    contentDescription = location.name,
                    modifier = Modifier.matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black,
                                ),
                            ),
                        ),
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black,
                                ),
                            ),
                        )
                        .clip(imageShape)
                    ,
                )

                if (showBackButton) {
                    ArtCartIconButton(
                        onClick = onBackClick,
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    )
                }

                ArtCartIconButton(
                    onClick = { onFollowClick(!location.isFavorite) },
                    icon = if (location.isFavorite) Icons.Filled.Check else Icons.Outlined.Add,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp),
                ) {
                    if (location.primaryTypeName.isNotBlank()) {
                        Text(
                            text = location.primaryTypeName.uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                        )
                    }

                    Text(
                        text = location.name.ifBlank { "Unnamed art location" },
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InfoCard {
                    DetailRow(
                        icon = Icons.Outlined.Place,
                        label = "Address",
                        value = location.address.ifBlank { "No address available" },
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    DetailRow(
                        icon = Icons.Outlined.Star,
                        label = "Rating",
                        value = if (location.rating > 0f) "${location.rating} / 5" else "No rating yet",
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    DetailRow(
                        icon = Icons.Outlined.MyLocation,
                        label = "Coordinates",
                        value = "%.5f, %.5f".format(location.latitude, location.longitude),
                    )
                }

                ArtCartButton(
                    onClick = { onLocationClick(location) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "View on Map",
                    )
                }

                if (location.url.isNotBlank()) {
                    OutlinedButton(
                        onClick = { /* wire browser intent here */ },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary,),
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 28.dp,
                            bottomEnd = 10.dp,
                            bottomStart = 28.dp,
                        ),
                    ) {
                        Text(
                            text = "Website",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        ArtCartIcon(
            imageVector = icon,
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}