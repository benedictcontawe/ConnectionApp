plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.connectionapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.connectionapp"
        minSdk = 19
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
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    //region Android UI Layout Library and backward-compatible Library(Legacy)
    implementation("androidx.activity:activity-ktx:1.9.0-alpha01")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.multidex:multidex:2.0.1")
    //endregion
    //region Life Cycle Components
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-reactivestreams:2.6.2")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.6.2")
    //endregion
    //region RX Java 3
    implementation("io.reactivex.rxjava3:rxjava:3.1.1")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    //endregion
    //region Android Unit Test and U.I. Test Library
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //endregion
}