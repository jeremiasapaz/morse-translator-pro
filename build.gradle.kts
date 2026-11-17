plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

// The project lives in a OneDrive-synced folder, which locks files in the build
// directory during sync and breaks Gradle's incremental cleanup. Redirect all
// build output to a non-synced location to avoid intermittent I/O failures.
// Override with -PbuildOutRoot=/path if desired.
val buildOutRoot: String =
    (findProperty("buildOutRoot") as String?)
        ?: "${System.getProperty("user.home")}/.morse-build"

allprojects {
    layout.buildDirectory.set(file("$buildOutRoot/${project.name}"))
}
