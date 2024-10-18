plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("maven-publish")
}

afterEvaluate{
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from (components["release"])
                groupId = "com.github.Rhpark"
                artifactId = "library_01"
                version = "0.1.0"
            }
        }
    }
}

android {
    namespace = "kr.open.rhpark.library"
    compileSdk = 34

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("debug") {
            java.srcDirs("src/debug/java")
        }
    }
    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
    buildFeatures {
        buildConfig = false
    }
    resourcePrefix = "rhpark_"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}