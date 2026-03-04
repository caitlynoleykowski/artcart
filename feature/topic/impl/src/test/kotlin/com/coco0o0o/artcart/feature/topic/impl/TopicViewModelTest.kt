/*
 * Copyright 2022 The Android Open Source Project
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

package com.coco0o0o.artcart.feature.topic.impl

import com.coco0o0o.artcart.core.data.repository.CompositeUserNewsResourceRepository
import com.coco0o0o.artcart.core.model.data.FollowableArtLocation
import com.coco0o0o.artcart.core.model.data.NewsResource
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.testing.repository.TestNewsRepository
import com.coco0o0o.artcart.core.testing.repository.TestLocationsRepository
import com.coco0o0o.artcart.core.testing.repository.TestUserDataRepository
import com.coco0o0o.artcart.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class TopicViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val topicsRepository = TestLocationsRepository()
    private val newsRepository = TestNewsRepository()
    private val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository,
    )
    private lateinit var viewModel: TopicViewModel

    @Before
    fun setup() {
        viewModel = TopicViewModel(
            userDataRepository = userDataRepository,
            locationsRepository = topicsRepository,
            userNewsResourceRepository = userNewsResourceRepository,
            topicId = testInputTopics[0].artLocation.id,
        )
    }

    @Test
    fun topicId_matchesTopicIdFromSavedStateHandle() =
        assertEquals(testInputTopics[0].artLocation.id, viewModel.topicId)

    @Test
    fun uiStateTopic_whenSuccess_matchesTopicFromRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        topicsRepository.sendTopics(testInputTopics.map(FollowableArtLocation::artLocation))
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].artLocation.id))
        val item = viewModel.topicUiState.value
        assertIs<TopicUiState.Success>(item)

        val topicFromRepository = topicsRepository.getLocation(
            testInputTopics[0].artLocation.id,
        ).first()

        assertEquals(topicFromRepository, item.followableArtLocation.artLocation)
    }

    @Test
    fun uiStateNews_whenInitialized_thenShowLoading() = runTest {
        assertEquals(NewsUiState.Loading, viewModel.newsUiState.value)
    }

    @Test
    fun uiStateTopic_whenInitialized_thenShowLoading() = runTest {
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicLoading_thenShowLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].artLocation.id))
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccess_thenTopicSuccessAndNewsLoading() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

            topicsRepository.sendTopics(testInputTopics.map { it.artLocation })
            userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].artLocation.id))
            val topicUiState = viewModel.topicUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertIs<TopicUiState.Success>(topicUiState)
            assertIs<NewsUiState.Loading>(newsUiState)
        }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccessAndNewsIsSuccess_thenAllSuccess() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                combine(
                    viewModel.topicUiState,
                    viewModel.newsUiState,
                    ::Pair,
                ).collect()
            }
            topicsRepository.sendTopics(testInputTopics.map { it.artLocation })
            userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].artLocation.id))
            newsRepository.sendNewsResources(sampleNewsResources)
            val topicUiState = viewModel.topicUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertIs<TopicUiState.Success>(topicUiState)
            assertIs<NewsUiState.Success>(newsUiState)
        }

    @Test
    fun uiStateTopic_whenFollowingTopic_thenShowUpdatedTopic() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        topicsRepository.sendTopics(testInputTopics.map { it.artLocation })
        // Set which topic IDs are followed, not including 0.
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].artLocation.id))

        viewModel.followTopicToggle(true)

        assertEquals(
            TopicUiState.Success(followableArtLocation = testOutputTopics[0]),
            viewModel.topicUiState.value,
        )
    }
}

private const val TOPIC_1_NAME = "Android Studio"
private const val TOPIC_2_NAME = "Build"
private const val TOPIC_3_NAME = "Compose"
private const val TOPIC_SHORT_DESC = "At vero eos et accusamus."
private const val TOPIC_LONG_DESC = "At vero eos et accusamus et iusto odio dignissimos ducimus."
private const val TOPIC_URL = "URL"
private const val TOPIC_IMAGE_URL = "Image URL"

private val testInputTopics = listOf(
    FollowableArtLocation(
        ArtLocation(
            id = "0",
            name = TOPIC_1_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = true,
    ),
    FollowableArtLocation(
        ArtLocation(
            id = "1",
            name = TOPIC_2_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = false,
    ),
    FollowableArtLocation(
        ArtLocation(
            id = "2",
            name = TOPIC_3_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = false,
    ),
)

private val testOutputTopics = listOf(
    FollowableArtLocation(
        ArtLocation(
            id = "0",
            name = TOPIC_1_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = true,
    ),
    FollowableArtLocation(
        ArtLocation(
            id = "1",
            name = TOPIC_2_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = true,
    ),
    FollowableArtLocation(
        ArtLocation(
            id = "2",
            name = TOPIC_3_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFavorited = false,
    ),
)

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = "Video 📺",
        artLocations = listOf(
            ArtLocation(
                id = "0",
                name = "Headlines",
                shortDescription = "",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            ),
        ),
    ),
)
