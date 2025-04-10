plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.apitransactions"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.apitransactions"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit for API requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Biometric
    implementation("androidx.biometric:biometric:1.1.0")

// EncryptedSharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha03")


// RecyclerView for list display
    implementation("androidx.recyclerview:recyclerview:1.3.1")

}