plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
}

android {
    compileSdk = 33
    
    defaultConfig {
        applicationId = "dev.mpardo.dailycaloriecounter"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val composeVersion = "1.3.0-beta02"
val koin_version = "3.2.1"

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.5.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    
    implementation("com.chargemap.compose:numberpicker:1.0.3")
    implementation("br.com.devsrsouza.compose.icons.android:feather:1.0.0")
    implementation("com.github.tfaki:ComposableSweetToast:1.0.1")
    
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    
    implementation("io.insert-koin:koin-android:$koin_version")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}