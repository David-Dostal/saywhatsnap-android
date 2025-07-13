plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // For Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    // Navigation using: https://github.com/raamcosta/compose-destinations
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "cz.mendelu.pef.xdostal8.saywhatsnap"
    compileSdk = 34

    defaultConfig {
        applicationId = "cz.mendelu.pef.xdostal8.saywhatsnap"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "cz.mendelu.pef.xdostal8.saywhatsnap.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    implementation("androidx.compose.material3:material3:1.1.0-alpha04")

    // extra
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.8.42-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.8.42-beta")

    // Accompanist (Permission)
    implementation("com.google.accompanist:accompanist-permissions:0.31.3-beta")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")

    // Test - permissions
    androidTestImplementation("androidx.test:rules:1.4.0")

    // Test helpers for LiveData
    androidTestImplementation("android.arch.core:core-testing:1.1.0")

    // Mockito
    androidTestImplementation("org.mockito:mockito-android:5.8.0")
    androidTestImplementation("io.mockk:mockk-android:1.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Moshi for json converter
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // DataStore
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Navigation - native compose
    implementation("androidx.navigation:navigation-compose:2.5.3")


    // Coil
    implementation("io.coil-kt:coil-compose:2.1.0")

    //  Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    // AppIntro
    implementation("com.github.AppIntro:AppIntro:6.1.0")

    // Splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Room
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-runtime:2.4.3")

    //Google Services & Maps
    implementation("com.google.maps.android:maps-compose:3.1.1")
    implementation("androidx.compose.foundation:foundation:1.6.0-alpha07")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:maps-compose:3.1.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:android-maps-utils:3.5.3")
    implementation("com.google.maps.android:maps-compose-widgets:3.1.1")
    implementation("com.google.maps.android:maps-compose-utils:3.1.1")

    // DataStore
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // CameraX
    implementation("androidx.camera:camera-camera2:1.3.0-alpha06")
    implementation("androidx.camera:camera-lifecycle:1.3.0-alpha06")
    implementation("androidx.camera:camera-view:1.3.0-alpha06")

    // MLKit
    implementation("com.google.mlkit:face-detection:16.1.5")
    implementation("com.google.mlkit:image-labeling:17.0.7")
    implementation("com.google.mlkit:object-detection:17.0.0")
    implementation("com.google.mlkit:barcode-scanning:17.1.0")
    implementation("com.google.mlkit:text-recognition:16.0.0-beta6")
    implementation("com.google.mlkit:face-mesh-detection:16.0.0-beta1")

    // Moshi for json converter
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
}

// For Hilt
kapt {
    correctErrorTypes = true
}
