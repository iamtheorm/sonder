# Android Integration Notes

This repository currently contains standalone Kotlin source files and does not yet include Gradle module files.

## Required Dependencies

Add these to your Android app module (`app/build.gradle.kts`):

```kotlin
dependencies {
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.0")
}
```

## Config Setup

- `AuthConfig.BASE_URL` is currently hardcoded for this repo snapshot.
- In a full Android module, replace it with a `BuildConfig` field per build type/flavor.

Example:

```kotlin
// build.gradle.kts
buildTypes {
    debug {
        buildConfigField("String", "SONDER_API_BASE_URL", "\"https://staging-api.sonder.app\"")
    }
    release {
        buildConfigField("String", "SONDER_API_BASE_URL", "\"https://api.sonder.app\"")
    }
}
```

Then map:

```kotlin
object AuthConfig {
    const val BASE_URL = BuildConfig.SONDER_API_BASE_URL
}
```
