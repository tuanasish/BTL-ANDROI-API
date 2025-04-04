plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.btl"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.btl"
        minSdk = 28
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

    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter (dùng Gson để chuyển đổi JSON thành Object)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // OkHttp Logging Interceptor (dễ dàng debug request/response)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    /*implementation ("androidx.credentials:credentials:1.2.0-alpha03")
    implementation ("androidx.credentials:credentials-play-services-auth:1.2.0-alpha03")
    implementation ("com.google.android.libraries.identity.googleid:1.0.0")*/
    implementation ("com.google.android.material:material:1.6.0")// hoặc version mới hơn
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google Maps dependency
    implementation("com.google.android.gms:play-services-maps:19.0.0") // Cập nhật phiên bản mới nhất

    // Google Location Services (Required for getting user location)
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}


