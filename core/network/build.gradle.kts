import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.artcart.android.library)
    alias(libs.plugins.artcart.android.library.jacoco)
    alias(libs.plugins.artcart.hilt)
    id("kotlinx-serialization")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.coco0o0o.artcart.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.kotlinx.datetime)
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    testImplementation(libs.kotlinx.coroutines.test)
}

val backendUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["BACKEND_URL"]
}.orElse("http://example.com")

val placesApiKey = providers.fileContents(
    isolated.rootProject.projectDirectory.file("secrets.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["PLACES_API_KEY"]
}.orElse("API_KEY")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put("BACKEND_URL", backendUrl.map { value ->
            BuildConfigField(type = "String", value = """"$value"""", comment = null)
        })
        it.buildConfigFields!!.put("PLACES_API_KEY", placesApiKey.map { value ->
            BuildConfigField(type = "String", value = """"$value"""", comment = null)
        })
    }
}
