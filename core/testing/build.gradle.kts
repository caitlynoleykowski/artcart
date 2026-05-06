plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.hilt)
}

android {
    namespace = "com.coco0o0o.artcart.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.common)
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.datetime)
}
