import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 34
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(files("libs\\poi-5.2.3.jar"))
    implementation(files("libs\\poi-examples-5.2.3.jar"))
    implementation(files("libs\\poi-excelant-5.2.3.jar"))
    implementation(files("libs\\poi-javadoc-5.2.3.jar"))
    implementation(files("libs\\poi-scratchpad-5.2.3.jar"))
    implementation(files("libs\\poi-ooxml-5.2.3.jar"))
    implementation(files("libs\\commons-logging-1.2.jar"))
    implementation(files("libs\\log4j-api-2.18.0.jar"))
    implementation(files("libs\\commons-io-2.11.0.jar"))
    implementation(files("libs\\commons-compress-1.21.jar"))
    implementation(files("libs\\xmlbeans-5.1.1.jar"))
    implementation(files("libs\\SparseBitSet-1.2.jar"))
    implementation(files("libs\\slf4j-api-1.7.36.jar"))
    implementation(files("libs\\jakarta.xml.bind-api-3.0.1.jar"))
    implementation(files("libs\\curvesapi-1.07.jar"))
    implementation(files("libs\\poi-ooxml-full-5.2.3.jar"))
    implementation(files("libs\\commons-collections4-4.4.jar"))
    implementation(files("libs\\commons-codec-1.15.jar"))
    implementation(files("libs\\commons-math3-3.6.1.jar"))



    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")

    implementation("com.loopj.android:android-async-http:1.4.10")

    implementation("com.squareup.picasso:picasso:2.71828")


    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
}