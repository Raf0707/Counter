plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "raf.console.counter"
    compileSdk = 34

    defaultConfig {
        applicationId = "raf.console.counter"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    implementation("androidx.lifecycle:lifecycle-service:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.0-alpha02")
    implementation("androidx.room:room-runtime:2.5.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.fragment:fragment:1.5.5")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}