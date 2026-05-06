plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.coco0o0o.artcart.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.guava)

    testImplementation(projects.core.testing)
}