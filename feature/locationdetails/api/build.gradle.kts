plugins {
    alias(libs.plugins.artcart.android.feature.api)
    alias(libs.plugins.artcart.android.feature.impl)
    alias(libs.plugins.artcart.android.library.compose)
}

android {
    namespace = "com.coco0o0o.artcart.feature.locationdetails.api"
}