plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
}

// The project may live in a OneDrive-synced folder, which locks files in the
// build directory during sync and breaks Gradle's incremental cleanup. In that
// case only, redirect build output to a non-synced location. On CI / normal
// paths the default `<module>/build` is used so tooling (and artifact uploads)
// find outputs where expected. Override explicitly with -PbuildOutRoot=/path.
val buildOutRoot: String? =
    (findProperty("buildOutRoot") as String?)
        ?: if (rootDir.absolutePath.contains("OneDrive", ignoreCase = true))
            "${System.getProperty("user.home")}/.morse-build" else null

if (buildOutRoot != null) {
    allprojects {
        layout.buildDirectory.set(file("$buildOutRoot/${project.name}"))
    }
}
