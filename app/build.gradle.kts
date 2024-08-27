import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val apiKey: String = localProperties.getProperty("apiKey", "")

android {
    namespace = "com.dicoding.truenoblestoryapp.loginwithanimation"
    compileSdk = 34

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    defaultConfig {
        applicationId = "com.dicoding.truenoblestoryapp.loginwithanimation"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["apiKey"] = apiKey
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
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

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation("androidx.activity:activity-ktx:1.9.1")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.exifinterface:exifinterface:1.3.7")

    implementation("androidx.paging:paging-runtime-ktx:3.3.2")

    androidTestImplementation("androidx.arch.core:core-testing:2.2.0") //InstantTaskExecutorRule
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") //TestDispatcher

    testImplementation("androidx.arch.core:core-testing:2.2.0") // InstantTaskExecutorRule
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") //TestDispatcher
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito:mockito-core:5.6.0")
}