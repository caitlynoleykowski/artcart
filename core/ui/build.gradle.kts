plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.android.library.compose)
    alias(libs.plugins.artcart.android.library.jacoco)
}

android {
    namespace = "com.coco0o0o.artcart.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.androidx.browser)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
