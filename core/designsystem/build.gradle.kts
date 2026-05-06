plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.android.library.compose)
    alias(libs.plugins.artcart.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.coco0o0o.artcart.core.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    implementation(libs.coil.kt.compose)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.testManifest)
    
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
}
