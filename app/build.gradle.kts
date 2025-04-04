plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.gaiaprototypev2"
    compileSdk = 34 //AVANT 34

    defaultConfig {
        applicationId = "com.example.gaiaprototypev2"
        minSdk = 28
        targetSdk = 34 //AVANT 34
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
        renderScript = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.picasso:picasso:2.71828")
}
