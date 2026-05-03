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

import android.util.Log
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.model.data.FollowableArtLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case which obtains a list of topics with their followed state.
 */
class GetFollowableLocationsUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository,
) {
    /**
     * Returns a list of topics with their associated followed state.
     *
     * @param sortBy - the field used to sort the topics. Default NONE = no sorting.
     */
    operator fun invoke(): Flow<List<ArtLocation>> {
        print("playing around early")
        Log.i("GetFollowableLocationsUseCase", "playing around early")
        return locationsRepository.getLocations()
    }
}
