package com.coco0o0o.artcart.core.testing.util

import com.coco0o0o.artcart.core.data.util.TimeZoneMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.TimeZone

class TestTimeZoneMonitor : TimeZoneMonitor {

    private val timeZoneFlow = MutableStateFlow(defaultTimeZone)

    override val currentTimeZone: Flow<TimeZone> = timeZoneFlow

    /**
     * A test-only API to set the from tests.
     */
    fun setTimeZone(zoneId: TimeZone) {
        timeZoneFlow.value = zoneId
    }

    companion object {
        val defaultTimeZone: TimeZone = TimeZone.of("Europe/Warsaw")
    }
}
