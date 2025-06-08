plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "kadyshev.dmitry.musicplayerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "kadyshev.dmitry.musicplayerapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    allprojects {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    kapt {
        useBuildCache = true
        javacOptions {
            option("--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
        }
    }

}

dependencies {

    kapt(libs.dagger.compiler)
    implementation (libs.dagger)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(project(":core-navigation"))
    implementation(project(":core-di"))
    implementation(project(":core-ViewModelFactory"))
    implementation(project(":player-service"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core-player"))
    implementation(project(":ui-search-tracks"))
    implementation(project(":ui-saved-tracks"))
    implementation(project(":ui-player"))
    implementation(project(":ui-tracks-core"))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}