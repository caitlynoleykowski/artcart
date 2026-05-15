package com.coco0o0o.artcart.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.component.ArtCartIconToggleButton
import com.coco0o0o.artcart.core.designsystem.component.DynamicAsyncImage
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.ui.R.string

@Composable
fun LocationsItem(
    name: String,
    following: Boolean,
    locationImageUrl: String,
    onClick: () -> Unit,
    onFollowButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    description: String = "",
    secondaryDescription: String = "",
    isSelected: Boolean = false,
) {
    ListItem(
        leadingContent = {
            DynamicAsyncImage(
                imageUrl = locationImageUrl,
                contentDescription = null,
                modifier = iconModifier.size(72.dp),
            )
        },
        headlineContent = {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Column {
                Text(
                    text = description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(text = secondaryDescription)
            }
        },
        trailingContent = {
            ArtCartIconToggleButton(
                checked = following,
                onCheckedChange = onFollowButtonClick,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(
                            id = string.core_ui_locations_card_follow_button_content_desc,
                        ),
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = stringResource(
                            id = string.core_ui_locations_card_unfollow_button_content_desc,
                        ),
                    )
                },
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                Color.Transparent
            },
        ),
        modifier = modifier
            .semantics(mergeDescendants = true) {
                selected = isSelected
            }
            .clickable(enabled = true, onClick = onClick),
    )
}

@Preview
@Composable
private fun LocationsCardPreview() {
    ArtCartTheme {
        Surface {
            LocationsItem(
                name = "Compose",
                description = "Description",
                secondaryDescription = "Secondary description",
                following = false,
                locationImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun LocationsCardLongNamePreview() {
    ArtCartTheme {
        Surface {
            LocationsItem(
                name = "This is a very very very very long name",
                description = "Description",
                secondaryDescription = "Secondary description",
                following = true,
                locationImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun LocationsCardLongDescriptionPreview() {
    ArtCartTheme {
        Surface {
            LocationsItem(
                name = "Compose",
                description = "This is a very very very very very very very " +
                    "very very very long description",
                secondaryDescription = "Secondary description",
                following = false,
                locationImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun LocationsCardWithEmptyDescriptionPreview() {
    ArtCartTheme {
        Surface {
            LocationsItem(
                name = "Compose",
                description = "",
                secondaryDescription = "",
                following = true,
                locationImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun LocationsCardSelectedPreview() {
    ArtCartTheme {
        Surface {
            LocationsItem(
                name = "Compose",
                description = "",
                secondaryDescription = "",
                following = true,
                locationImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
                isSelected = true,
            )
        }
    }
}
