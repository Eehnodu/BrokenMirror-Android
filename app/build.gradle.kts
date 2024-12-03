plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.brokenmirror"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.brokenmirror"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.airbnb.android:lottie:6.4.0")
    implementation("me.relex:circleindicator:2.1.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.airbnb.android:lottie:6.4.0")
    implementation ("jp.wasabeef:blurry:4.0.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // for chatting
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.neovisionaries:nv-websocket-client:2.14")
    implementation("org.java-websocket:Java-WebSocket:1.5.2")

    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    // implementation("ua.naiksoftware:stomp-client:2.0.9")

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // WorkManager (Java only)
    implementation("androidx.work:work-runtime:2.9.1")


    //implementation("")
    //implementation("")

}