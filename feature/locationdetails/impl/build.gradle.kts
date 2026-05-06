plugins {
    alias(libs.plugins.artcart.android.feature.impl)
    alias(libs.plugins.artcart.android.library.compose)
    alias(libs.plugins.artcart.android.library.jacoco)
}

android {
    namespace = "com.coco0o0o.artcart.feature.locationdetails.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.locationdetails.api)

    implementation(libs.androidx.compose.material3.adaptive.navigation3)

    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}