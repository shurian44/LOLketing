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
        maven("https://naver.jfrog.io/artifactory/maven/")
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
        maven("https://www.jitpack.io")
    }
}

rootProject.name = "LOLketing"
include(":app")
include(":auth")
include(":database")
include(":network")
