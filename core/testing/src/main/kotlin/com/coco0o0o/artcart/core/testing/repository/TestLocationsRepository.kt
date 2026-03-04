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

package com.coco0o0o.artcart.core.testing.repository

import com.coco0o0o.artcart.core.data.Synchronizer
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestLocationsRepository : LocationsRepository {
    /**
     * The backing hot flow for the list of topics ids for testing.
     */
    private val topicsFlow: MutableSharedFlow<List<ArtLocation>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getLocations(): Flow<List<ArtLocation>> = topicsFlow

    override fun getLocation(id: String): Flow<ArtLocation> =
        topicsFlow.map { topics -> topics.find { it.id == id }!! }

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    fun sendTopics(artLocations: List<ArtLocation>) {
        topicsFlow.tryEmit(artLocations)
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
