@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.shopwithswipee"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shopwithswipee"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Architectural Components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

// Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

// Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v261)
    implementation(libs.androidx.lifecycle.runtime.ktx)

// Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

// Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

// Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.fragment)
    kapt(libs.hilt.android.compiler)

// Paging
    implementation(libs.androidx.paging.runtime)

    implementation(libs.picasso)

    implementation(libs.lottie)


    implementation(libs.circleimageview)

}