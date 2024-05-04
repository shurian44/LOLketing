import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ezen.lolketing"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.ezen.lolketing"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ADDRESS_API_KEY", "${properties["address_api_key"]}")
        buildConfigField("String", "CLIENT_ID", "${properties["client_id"]}")
        buildConfigField("String", "CLIENT_SECRET", "${properties["client_secret"]}")

        manifestPlaceholders["KAKAO_APP_KEY"] = properties.getProperty("KAKAO_APP_KEY")
        manifestPlaceholders["MAP_CLIENT_ID"] = properties.getProperty("map_client_id")
        manifestPlaceholders["MAP_CLIENT_SECRET"] = properties.getProperty("map_client_secret")
        
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
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation fileTree(include: ["*.jar"], dir: "libs")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.compose.ui:ui:1.6.0-alpha08")
    implementation ("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.0-alpha08")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.24.13-rc")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.31.1-alpha")
    implementation ("com.google.accompanist:accompanist-pager:0.31.3-beta")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.31.3-beta")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")
    implementation ("androidx.activity:activity-compose:1.8.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.core:core-ktx:1.12.0")

    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")

    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    // Volley
    implementation ("com.android.volley:volley:1.2.1")
    // Glide
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    implementation ("com.github.skydoves:landscapist-glide:1.5.2")

    // 웹 크롤링
    implementation ("org.jsoup:jsoup:1.14.2")
    // QR 코드
    implementation ("com.journeyapps:zxing-android-embedded:3.6.0")


    // Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt ("com.google.dagger:hilt-android-compiler:2.48")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    val lifecycle_version = "2.6.2"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    val retrofitVersion = "2.9.0"
    val okhttp3Version = "4.11.0"
    // retrofit - http://square.github.io/retrofit/ (Apache 2.0)
    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    // okhttp - https://github.com/square/okhttp (Apache 2.0)
    implementation ("com.squareup.okhttp3:okhttp:$okhttp3Version")
    implementation ("com.squareup.okhttp3:logging-interceptor:$okhttp3Version")

    // KTX
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Ted Permission : https://github.com/ParkSangGwon/TedPermission
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    // PhotoView : https://github.com/Baseflow/PhotoView
    implementation ("com.github.chrisbanes:PhotoView:2.0.0")

    implementation ("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Room library
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")

    // Lottie Animation
    implementation ("com.airbnb.android:lottie:6.1.0")
    implementation ("com.airbnb.android:lottie-compose:6.1.0")

    implementation(project(path = ":network"))
    implementation(project(path = ":auth"))
    implementation(project(path = ":database"))
}