pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.buildFileName = "build.gradle.kts"
rootProject.name = "Trustar"

include(
    "Annotation",
    "BaseLib",
    "TrustarApp"
)
