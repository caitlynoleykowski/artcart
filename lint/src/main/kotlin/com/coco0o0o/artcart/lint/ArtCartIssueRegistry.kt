package com.coco0o0o.artcart.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class ArtCartIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "ArtCart",
        feedbackUrl = "https://github.com/caitlynoleykowski/artcart/issues",
        contact = "https://github.com/caitlynoleykowski/artcart",
    )
}
