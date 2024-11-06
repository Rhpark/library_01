# Android Easy Development Library (v0.4.0)

![image](https://jitpack.io/v/rhpark/library_01.svg)
<br>
</br>
This library helps you make easy and more simple code for Android developers

<br>
</br>

## How to use

### 1. Logx (Custom Logcat)

<br>
</br>

#### 1 - 1  config
```
Logx.apply {  
  
	/**
	* AppName include the Logcat Tag 
        * default value is RhPark 
	*/
	setAppName("RhPark")  
  
  
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
<br>
</br>

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
<br>
</br>

####  1 - 3 Result Logcat
![image](https://github.com/user-attachments/assets/b8176ca0-4f1b-4f59-a38d-38ef9ffe7d9c)

<br>
</br>
<br>
</br>

### 2. Activity (Custom Activity, CustomViewModel)


####  2 - 1 XML
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Activity_Binding_XML.png)
<br>
</br>

####  2 - 2 ViewModel
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Activity_Binding_ViewModel.png)
<br>
</br>

####  2 - 3 ViewModelEvent
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Activity_Binding_ViewModel_Event.png)
<br>
</br>

####  2 - 4 Activity (Binding)
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Activity_Binding_MainActivity.png)
<br>
</br>

####  2 - 5 Fragment (Binding)
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Fragment_Binding.png)

<br>
</br>
<br>
</br>

### 3. Adapter,ListAdapter


####  3 - 1 Adapter
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Binding_Adapter.png)
<br>
</br>

####  3 - 2 List Adapter
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Binding_List_Adapter.png)

<br>
</br>
<br>
</br>


### 4. RecyclerScrollStateView(Custom RecyclerView)

#### 4 - 1 RecyclerStateViewConfig (Activity)
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Binding_RecyclerScrollStateView.png)
<br>
</br>

####  4 - 2 RecyclerStateViewConfig (XML)
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_Binding_RecyclerStateView_XML.png)


<br>
</br>


### 5. SystemServiceManager 

####  5 - 1 VibratorController
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_SystemManager_Vibrator.png)


<br>
</br>

####  5 - 2 DisplayInfo
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_SystemManager_Display.png)



### 6. Toast, SnackBar
![image](https://github.com/Rhpark/library_01/blob/master/Sample_Base_SystemManager_Toast_Snackbar.png)



<br>
</br>
<br>
</br>
<br>
</br>

## Setting

#### 1. Build.Gradle (Module :App)

```
plugins {
    id ("kotlin-kapt")
}

...

android {
...
    buildFeatures {
        dataBinding = true
    }
...
}

...

dependencies {
	...
	implementation("com.github.rhpark:library_01:0.4.0")
	...
}
```
<br>
</br>

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



