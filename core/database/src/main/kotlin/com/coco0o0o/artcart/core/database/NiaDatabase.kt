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

package com.coco0o0o.artcart.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coco0o0o.artcart.core.database.dao.NewsResourceDao
import com.coco0o0o.artcart.core.database.dao.NewsResourceFtsDao
import com.coco0o0o.artcart.core.database.dao.RecentSearchQueryDao
import com.coco0o0o.artcart.core.database.dao.TopicDao
import com.coco0o0o.artcart.core.database.dao.TopicFtsDao
import com.coco0o0o.artcart.core.database.model.NewsResourceEntity
import com.coco0o0o.artcart.core.database.model.NewsResourceFtsEntity
import com.coco0o0o.artcart.core.database.model.NewsResourceTopicCrossRef
import com.coco0o0o.artcart.core.database.model.RecentSearchQueryEntity
import com.coco0o0o.artcart.core.database.model.TopicEntity
import com.coco0o0o.artcart.core.database.model.TopicFtsEntity
import com.coco0o0o.artcart.core.database.util.InstantConverter

@Database(
    entities = [
        NewsResourceEntity::class,
        NewsResourceTopicCrossRef::class,
        NewsResourceFtsEntity::class,
        TopicEntity::class,
        TopicFtsEntity::class,
        RecentSearchQueryEntity::class,
    ],
    version = 1,
    autoMigrations = [
    ],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class NiaDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun newsResourceDao(): NewsResourceDao
    abstract fun topicFtsDao(): TopicFtsDao
    abstract fun newsResourceFtsDao(): NewsResourceFtsDao
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}
