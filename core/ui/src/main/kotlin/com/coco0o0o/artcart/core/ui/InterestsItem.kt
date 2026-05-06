/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coco0o0o.artcart.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.component.DynamicAsyncImage
import com.coco0o0o.artcart.core.designsystem.component.ArtCartIconToggleButton
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
            LocationsIcon(locationImageUrl, iconModifier.size(48.dp))
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Column {
                Text(text = description)
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

@Composable
private fun LocationsIcon(topicImageUrl: String, modifier: Modifier = Modifier) {
    if (topicImageUrl.isEmpty()) {
        Icon(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            imageVector = Icons.Rounded.Person,
            // decorative image
            contentDescription = null,
        )
    } else {
        DynamicAsyncImage(
            imageUrl = topicImageUrl,
            contentDescription = null,
            modifier = modifier,
        )
    }
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
