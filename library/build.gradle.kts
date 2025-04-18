plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("maven-publish")
    id ("kotlin-kapt")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.Rhpark"
            artifactId = "library_01"
            version = libs.versions.library.get()

            afterEvaluate {
                from(components["release"])
            }
        }

        register<MavenPublication>("debug") {
            groupId = "com.github.Rhpark"
            artifactId = "library_01"
            version = libs.versions.library.get()

            afterEvaluate {
                from(components["debug"])
            }
        }
    }
}

android {
    namespace = "kr.open.rhpark.library"
    compileSdk = 35

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
        multiDexEnabled = true
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
        debug {
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
        dataBinding = true
//        viewBinding = true
    }
//    resourcePrefix = "rhpark_"
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)

    implementation(libs.material)


    /**
     * Using for LifeCycle(ViewModelScope, CoroutineScope..)
     */
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)

}