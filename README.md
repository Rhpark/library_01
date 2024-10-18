# Android Base Library_01

![image](https://jitpack.io/v/rhpark/library_01.svg)
<br>
</br>
This library drives easier development of Android.

<br>
</br>

## Preview

### 1. Logx (Custom Logcat)
![image](https://github.com/user-attachments/assets/b8176ca0-4f1b-4f59-a38d-38ef9ffe7d9c)

<br>
</br>

## Setting

#### 1. Build.Gradle (Module :App)

```
dependencies {
	...
	implementation("com.github.rhpark:library_01:0.1.0")
	...
}
```
#### 2. Setting.Gradle

```
repositories {
	...
	maven { url = uri("https://jitpack.io") }
	...
}
```

<br>
</br>


## How to use

### 1. Logx (Custom Logcat)


#### 1 - 1  config
```
Logx.apply {  
  
	/**
	* AppName include the Logcat Tag 
        * default value is RhPark 
	*/
	setAppName("MyhApp")  
  
  
	/**  
	* true is logcat message show, else is gone
	*  
	* default value is true 
	*/
	isDebug = true  
  
  
	/**  
	* true is logcat save file , else is gone 
	* required permission WRITE_EXTERNAL_STORAGE
	* default value is false 
	*/
	isDebugSave = false  
  
  
	/**  
	* Logcat show only added tagList.
	* The isDebugFilter value must be true for operation 
	*/		
	setDebugTagCheckList(  
        listOf(  
            "MainActivity",  
			"MainActivityVm",  
			"..."  
		)  
	)  
  
        isDebugFilter = false  
  
  
	/**  
	* Logcat save file path 
	* default value is Environment.getExternalStorageDirectory().path 
	*/
	saveFilePath = "/sdcard/"  
  
  
	/**  
	* Logcat show only added LogType values. 
	* default typelist is All LogTypeList 
	* Logat printed, Only added LogxType list 
	* default is All LogTypeList 
	*/
	setDebugLogTypeList(  
		listOf(  
		    LogxType.VERBOSE,  
			LogxType.DEBUG,  
			LogxType.INFO,  
			LogxType.WARN,  
			LogxType.ERROR,  
			LogxType.PARENT,  
			LogxType.JSON,  
			LogxType.THREAD_ID,  
		)  
    )  
}
```

####  1 - 2 call
```

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		...
		test()
	}

	fun test() {  
		Logx.d()  
		Logx.d("msg")  
		Logx.d("Tag","mmssgg")  
		Logx.p()  
		Logx.t()
	}
}
```
