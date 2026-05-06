plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.android.library.jacoco)
    alias(libs.plugins.artcart.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.coco0o0o.artcart.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.network)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.testing)
}
