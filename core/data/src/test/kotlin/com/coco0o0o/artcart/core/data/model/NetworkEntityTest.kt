package com.coco0o0o.artcart.core.data.model

import com.coco0o0o.artcart.core.network.model.DisplayName
import com.coco0o0o.artcart.core.network.model.LatLng
import com.coco0o0o.artcart.core.network.model.NetworkLocation
import com.coco0o0o.artcart.core.network.model.PrimaryTypeDisplayName
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityTest {

    @Test
    fun networkLocationToDatabaseModel() {
        val networkLocation = NetworkLocation(
            id = "0",
            rating = 0f,
            displayName = DisplayName(
                text = "testname0",
                languageCode = "test language"
            ),
            formattedAddress = "test address 0",
            location = LatLng(0.0, 0.0),
            photos = emptyList(),
            primaryTypeDisplayName = PrimaryTypeDisplayName(
                text = "test type"
            ),
        )
        val entity = networkLocation.toLocationEntity() // TODO

        assertEquals("0", entity.id)
        assertEquals("testname0", entity.name)
        assertEquals("testurl0", entity.url)
    }
}
