plugins {
    alias(libs.plugins.artcart.android.feature.impl)
    alias(libs.plugins.artcart.android.library.compose)
}

android {
    namespace = "com.coco0o0o.artcart.feature.saved.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.saved.api)
    implementation(projects.feature.locationdetails.api)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
