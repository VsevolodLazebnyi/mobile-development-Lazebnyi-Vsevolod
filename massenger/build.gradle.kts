plugins {
    id("com.android.application") version "8.1.0" apply false
}
allprojects {
    configurations.all {
        resolutionStrategy {
            eachDependency {
                when (requested.group) {
                    "org.jetbrains.kotlin" -> useVersion("1.8.22")
                }
            }
            force(
                "org.jetbrains.kotlin:kotlin-stdlib:1.8.22",
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22",
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22"
            )
        }
    }
}