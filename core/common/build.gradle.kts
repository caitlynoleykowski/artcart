plugins {
    alias(libs.plugins.artcart.jvm.library)
    alias(libs.plugins.artcart.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}