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

package com.coco0o0o.artcart.core.domain

import com.coco0o0o.artcart.core.domain.TopicSortField.NAME
import com.coco0o0o.artcart.core.model.data.FollowableArtLocation
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.testing.repository.TestLocationsRepository
import com.coco0o0o.artcart.core.testing.repository.TestUserDataRepository
import com.coco0o0o.artcart.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetFollowableTopicsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val topicsRepository = TestLocationsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetFollowableLocationsUseCase(
        topicsRepository,
        userDataRepository,
    )

    @Test
    fun whenNoParams_followableTopicsAreReturnedWithNoSorting() = runTest {
        // Obtain a stream of followable topics.
        val followableTopics = useCase()

        // Send some test topics and their followed state.
        topicsRepository.sendTopics(testArtLocations)
        userDataRepository.setFollowedTopicIds(setOf(testArtLocations[0].id, testArtLocations[2].id))

        // Check that the order hasn't changed and that the correct topics are marked as followed.
        assertEquals(
            listOf(
                FollowableArtLocation(testArtLocations[0], true),
                FollowableArtLocation(testArtLocations[1], false),
                FollowableArtLocation(testArtLocations[2], true),
            ),
            followableTopics.first(),
        )
    }

    @Test
    fun whenSortOrderIsByName_topicsSortedByNameAreReturned() = runTest {
        // Obtain a stream of followable topics, sorted by name.
        val followableTopics = useCase(
            sortBy = NAME,
        )

        // Send some test topics and their followed state.
        topicsRepository.sendTopics(testArtLocations)
        userDataRepository.setFollowedTopicIds(setOf())

        // Check that the followable topics are sorted by the topic name.
        assertEquals(
            followableTopics.first(),
            testArtLocations
                .sortedBy { it.name }
                .map {
                    FollowableArtLocation(it, false)
                },
        )
    }
}

private val testArtLocations = listOf(
    ArtLocation("1", "Headlines", "", "", "", ""),
    ArtLocation("2", "Android Studio", "", "", "", ""),
    ArtLocation("3", "Compose", "", "", "", ""),
)
