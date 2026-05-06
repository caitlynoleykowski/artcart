package com.coco0o0o.artcart.core.domain

import com.coco0o0o.artcart.core.domain.TopicSortField.NAME
import com.coco0o0o.artcart.core.model.data.FavoriteableArtLocation
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.testing.repository.TestLocationsRepository
import com.coco0o0o.artcart.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetFollowableLocationsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val locationsRepository = TestLocationsRepository()

    val useCase = GetFavoriteableLocationsUseCase(
        locationsRepository,
    )

    @Test
    fun whenNoParams_followableTopicsAreReturnedWithNoSorting() = runTest {
        val favoriteableLocations = useCase()

        // Send some test topics and their favorited state.
        locationsRepository.updateLocations(testArtLocations)

        // Check that the order hasn't changed and that the correct locations are marked as favorited.
        assertEquals(
            listOf(
                FavoriteableArtLocation(testArtLocations[0], true),
                FavoriteableArtLocation(testArtLocations[1], false),
                FavoriteableArtLocation(testArtLocations[2], true),
            ),
            favoriteableLocations.first(),
        )
    }

    @Test
    fun whenSortOrderIsByName_topicsSortedByNameAreReturned() = runTest {
        val favoriteableLocations = useCase()

        locationsRepository.updateLocations(testArtLocations)

        assertEquals(
            favoriteableLocations.first(),
            testArtLocations
                .sortedBy { it.name }
                .map {
                    FavoriteableArtLocation(it, false)
                },
        )
    }
}

private val testArtLocations = listOf(
    ArtLocation("1", "Headlines", "", "", "", ""),
    ArtLocation("2", "Android Studio", "", "", "", ""),
    ArtLocation("3", "Compose", "", "", "", ""),
)
