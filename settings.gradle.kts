pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MusicPlayerApp"
include(":app")
include(":data")
include(":domain")
include(":ui-search-tracks")
include(":ui-tracks-core")
include(":core-player")
include(":ui-saved-tracks")
include(":ui-player")
include(":core-navigation")
include(":player-service")
