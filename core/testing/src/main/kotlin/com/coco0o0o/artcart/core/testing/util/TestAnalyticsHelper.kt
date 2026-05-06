package com.coco0o0o.artcart.core.testing.util

import com.coco0o0o.artcart.core.analytics.AnalyticsEvent
import com.coco0o0o.artcart.core.analytics.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {

    private val events = mutableListOf<AnalyticsEvent>()
    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    fun hasLogged(event: AnalyticsEvent) = event in events
}
