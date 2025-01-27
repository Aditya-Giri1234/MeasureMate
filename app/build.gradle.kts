plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization.plugin)
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.aditya.measuremate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aditya.measuremate"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
   /* composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }*/
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "images/dashboard.JPEG"
            excludes += "images/details.JPEG"
            excludes += "images/signin.JPEG"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.annotation)
    annotationProcessor(libs.room.annotation)

    //Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compse)
    ksp(libs.hilt.ksp)
    annotationProcessor(libs.room.annotation)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)

    //Jetpack Compose recompose debugger by log
    // Solution of this dependency available to all module :- https://stackoverflow.com/a/48443958/17464278
    implementation(libs.rebugger)
    implementation(libs.androidx.material.icons.extended)

    //Google gson
    implementation(libs.gson)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    //Coil
    implementation(libs.coil.compose)

    //For Compose Runtime Lifecycle support (collectAsStateWithLifeCycle())
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Kotlin - Serialization
    implementation(libs.kotlin.serialization)

    //Kotlin - reflection
    implementation(libs.kotlin.reflect)

    //Downloaded Font
    implementation(libs.androidx.ui.text.google.fonts)

    //For Adaptive Layout
    implementation(libs.androidx.material3.windowSizeClass)

    //splash screen
    implementation(libs.androidx.core.splashscreen)

    //credential manager
    implementation(libs.androidx.credentials)

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}