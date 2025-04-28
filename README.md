# Android Easy Development Library (v1.5.6)

![image](https://jitpack.io/v/rhpark/library_01.svg)
<br>
</br>
This library helps you make easy and more simple code for Android developers
<br>
</br>
<br>
</br>
안드로이드 개발자를 위해
<br>
</br>
좀 더 간단히 확인 할 수 있거나.
<br>
</br>
좀 더 간단히 만들 수 있거나.
<br>
</br>

<br>
</br>

## 목적 (Purpose)
 안드로이드 개발을 보다 쉽게 만들어주는 유틸리티 라이브러리입니다. 이 라이브러리는 권한 처리, 로그 관리, 시스템 기능 제어, UI 보일러플레이트 감소 등 반복적인 작업을 간소화하는 것을 목적으로 합니다.개발자는 자주 쓰이는 기능들을 손쉽게 구현할 수 있어 생산성을 높일 수 있습니다.
<br>
</br>
 This library is designed to simplify common tasks in Android development, such as handling permissions, managing logs, controlling system features, and reducing UI boilerplate code. By using Library_01, Developers can increase productivity by leveraging ready-to-use components for frequent tasks instead of writing them from scratch.

<br>
</br>

## 주요 구성 요소 (Key Components)
 라이브러리는 여러 패키지로 구성되어 있으며, 각 패키지는 특화된 기능을 제공합니다. 아래는 핵심 컴포넌트와 클래스의 개요입니다.
 <br>
</br>
The library is organized into several packages, each providing specialized functionality. Key components and classes include
<br>
</br>
<br>
</br>

### Shared Preferences - BaseSharedPreference: SharedPreferences

 사용을 간편화하기 위한 추상 클래스입니다. 키-값 쌍 저장/로드, 일괄 apply 및 commit 지원, 그리고 Double타입이나 Set<String>타입까지 한번에 처리할 수 있습니다​. 이를 상속하여 애플리케이션별 설정 저장 클래스를 만들 수 있습니다.
<br>
</br>
An abstract class to simplify SharedPreferences operations. It provides easy get/set for key-value pairs (including support for Double and Set<String> types) and utility methods for batch apply and coroutine-safe commit operations​. You can extend this class to create your own app-specific preferences manager.
<br>
</br>
<br>
</br>

### Logging Utility - Logx (with LogxFileManager, LogxWriter)

 앱 로그 출력을 개선해주는 커스텀 Logcat 유틸리티입니다. Logx 객체를 통해 편리하게 로그를 출력하고, JSON 포매팅(LogxType.JSON), 호출부 표시(PARENT 타입), 현재 쓰레드 ID 출력(T_ID 타입) 등 확장된 로그 타입을 지원합니다​. 또한 로그를 파일로 저장하거나(LogxFileManager, LogxWriter) 태그별로 관리할 수 있어 디버깅에 유용합니다.
<br>
</br>

A custom logcat utility for enhanced logging. Using the Logx object, you can easily print logs with extended types such as JSON formatting (LogxType.JSON), parent method context (PARENT type), and current thread ID (T_ID type)​. It also supports writing logs to a file (via LogxFileManager/LogxWriter) and tag management, making debugging easier. 
<br>
</br>
<br>
</br>

### System Manager – BaseSystemService 
 안드로이드 시스템 기능을 쉽게 제어하기 위한 베이스 클래스와 컨트롤러 클래스 모음입니다. BaseSystemService는 시스템 서비스 관련 클래스의 기본이 되는 추상 클래스이며, 필요 시 권한 체크 기능도 내장하고 있습니다​. 이를 바탕으로 다음과 같은 컨트롤러들을 제공합니다:
 <br>
</br>
A base class and set of controller classes to easily control Android system features. BaseSystemService is an abstract base for system service controllers with built-in permission requirement checks​. On top of this, the library provides controllers such as:
<br>
</br>
<br>
</br>

### AlarmController: AlarmManager

 래핑하여 손쉽게 알람 스케줄 및 취소 기능 제공 (예약 작업 간소화).
provide easy scheduling and cancellation of alarms (simplifies scheduling background tasks).
<br>
</br>
<br>
</br>

### SimpleNotificationController: 

 간단한 안드로이드 알림(Notification)을 생성하고 표시하는 컨트롤러.
<br>
</br>
build and display simple Android notifications with minimal code.
<br>
</br>
<br>
</br>

### SoftKeyboardController

 화면의 소프트 키보드 표시/숨김을 손쉽게 제어하는 유틸.
<br>
</br>
Utility to easily show or hide the soft keyboard in the UI.
<br>
</br>
<br>
</br>

### VibratorController: 

 진동기능을 쉽게 사용하도록 도와주는 진동 컨트롤러 (예: vibratorController.vibrate(500)는 0.5초 진동).
<br>
</br>
A vibration controller to use the device vibrator with simple calls (e.g., vibratorController.vibrate(500) vibrates for 0.5 sec).
<br>
</br>
<br>
</br>

### Device/System Info – BatteryInfo, DisplayInfo, NetworkStateInfo 등:

 domain.common.systemmanager.info 패키지에는 배터리 상태, 디스플레이 정보, 위치 및 네트워크 상태 등을 손쉽게 얻을 수 있는 유틸리티 클래스들이 포함되어 있습니다.
<br>
</br>
 The domain.common.systemmanager.info package provides utility classes to easily retrieve information such as battery status, display metrics, location, and network connectivity.
<br>
</br>
<br>
</br>

### Permission Handling – PermissionManager:

 런타임 권한 요청을 간소화하는 매니저 클래스입니다. ActivityResult API와 연동하여, 요청해야 할 권한 목록을 전달하면 남은 권한만 선별(remainPermissions)하여 한 번에 요청하고 그 결과를 콜백으로 받아 처리합니다​. 특별 권한인 SYSTEM_ALERT_WINDOW(오버레이 권한)도 감지하여 별도 인텐트를 통해 요청을 지원합니다​. 이 클래스를 사용하면 사용자 권한 요청 로직을 깔끔하게 관리할 수 있습니다.
<br>
</br>
A manager class to simplify runtime permission requests. Working with the Activity Result API, you provide a list of permissions to request; it filters out already granted ones (remainPermissions) and requests the rest in one go, then delivers the result via a callback​. It even detects special permission SYSTEM_ALERT_WINDOW (draw over other apps) and launches the appropriate intent for it​. This helps keep your permission request logic clean and straightforward.
<br>
</br>
<br>
</br>

### UI Base Classes – BaseActivity, BaseFragment, BaseDialogFragment (및 Binding 버전): 
 
 공통적으로 사용하는 Activity/Fragment의 상속 베이스를 제공합니다. BaseActivity와 BaseFragment는 기본적인 초기화 코드를 캡슐화하고, BaseBindingActivity<Binding> / BaseBindingFragment<Binding>은 View Binding 또는 Data Binding을 손쉽게 활용할 수 있도록 합니다. 예를 들어, BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main)를 상속하면 세팅된 레이아웃에 대해 binding 객체를 바로 사용할 수 있어 findViewById를 생략할 수 있습니다.
<br>
</br>
 These provide base classes for common Activities/Fragments. BaseActivity and BaseFragment encapsulate routine setup code, and BaseBindingActivity<Binding> / BaseBindingFragment<Binding> make it easy to use View Binding or Data Binding. For example, by extending BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main), you can directly use the binding object for that layout, eliminating the need for manual findViewById calls.
<br>
</br>
<br>
</br>
	 
 ### Adapter Utilities – BaseRcvAdapter, BaseRcvListAdapter, BaseRcvViewHolder: RecyclerView 

 어댑터 작성을 단순화하기 위한 베이스 클래스들입니다. 제네릭으로 데이터 타입과 ViewBinding 타입을 받아들이며, ViewHolder와 Adapter boilerplate 코드를 크게 줄여줍니다. BaseRcvListAdapter는 DiffUtil을 통한 ListAdapter 구현을 쉽게 할 수 있게 도와주며, BaseRcvViewHolder는 뷰 홀더에서 바인딩 객체에 바로 접근할 수 있도록 합니다. 이를 통해 RecyclerView를 위한 반복 코드를 최소화합니다.
<br>
</br>
Base classes to simplify writing RecyclerView adapters. They take generics for your data type and ViewBinding type, reducing boilerplate in ViewHolder and Adapter code. BaseRcvListAdapter helps you implement a ListAdapter with DiffUtil easily, and BaseRcvViewHolder provides direct access to the binding in view holders. This minimizes the repetitive code needed for setting up RecyclerView adapters.
<br>
</br>
<br>
</br>

### Custom RecyclerView Features – RecyclerScrollStateView: 

이 커스텀 뷰는 RecyclerView의 스크롤 상태와 연계된 UI 컴포넌트로, 예를 들어 목록이 비었을 때 공백 뷰를 보여주거나, 스크롤 진행 상태를 표시하는 등의 기능을 지원합니다. RecyclerViewScrollStateImp와 RecyclerViewScrollStateSead와 함께 사용되어 RecyclerView의 스크롤 상태 UI를 손쉽게 구현하도록 도와줍니다.
<br>
</br>
 A custom view tied to a RecyclerView's scroll state, useful for showing UI feedback like an empty view when the list is empty or indicating scroll position/status. Used in conjunction with RecyclerViewScrollStateImp and RecyclerViewScrollStateSead, it helps implement scroll state UI for RecyclerViews with minimal effort.
<br>
</br>
<br>
</br>

### Etc Utilities – 
그 외에도 Snackbar/Toast 헬퍼, Fragment 간 네비게이션 함수, Context.remainPermissions()와 Context.getAlarmManager()같은 컨텍스트 확장 함수 등 다양한 유틸리티 함수들이 포함되어 있습니다. 이러한 작은 유틸 기능들이 모여 개발 편의성을 높여줍니다.
<br>
</br>
 Additionally, the library includes various helper functions and classes such as Snackbar/Toast helpers, convenient Fragment navigation functions, extension functions like Context.remainPermissions() and Context.getAlarmManager(), and more. These utilities collectively improve development convenience for everyday tasks.
<br>
</br>
<br>
</br>

## 설치 방법 (Installation)
 본 라이브러리는 Gradle을 통해 손쉽게 프로젝트에 추가할 수 있습니다. (만약 중앙 저장소에 아직 배포되지 않았다면 JitPack 등을 활용 가능합니다.)
 <br>
</br>
You can easily add this library to your project via Gradle. (If it’s not published on a central repository, you can use JitPack or include the library module directly.) 
<br>
</br>

### Gradle 의존성 추가 - Using Gradle (via JitPack):
프로젝트의 settings.gradle 또는 프로젝트 Gradle 설정에 JitPack 저장소를 추가합니다:
<br>
</br>
Add the JitPack repository in your root settings.gradle or project-level Gradle config:
<br>
</br>
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
<br>
</br>

모듈의 build.gradle에 라이브러리 의존성을 추가합니다 (최신 버전: 1.5.6):
<br>
</br>
Add the library dependency in your app module build.gradle (latest version: 1.5.6):
<br>
</br>
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
	implementation("com.github.rhpark:library_01:1.5.6")
	...
}
```
<br>
</br>
위와 같이 추가하면 Gradle이 GitHub의 라이브러리_01 저장소에서 라이브러리 모듈을 가져와 빌드합니다. 버전 번호는 릴리즈 태그에 따라 변경될 수 있습니다. 참고: 라이브러리가 중앙 Maven 저장소나 JCenter에 배포된 경우, 해당 저장소와 그룹 아이디를 사용하면 됩니다. 위 예시는 JitPack을 이용한 방법입니다.
<br>
</br>
This will fetch the library_01 library from the GitHub repository via JitPack. Adjust the version number as needed if newer releases are available. Note: If the library is published on a central repository (e.g., Maven Central or JCenter), use the provided group ID and repository instead. The above example uses JitPack as an alternative.







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
		Logx.d() // or "".logxD()
		Logx.d("msg")  // or "msg"logxD()
		Logx.d("Tag","mmssgg")  // or "mmssgg".logxD()
		Logx.p()  // or "".logxP()
		Logx.t() // or "".logxT()
                
	}
}
```
<br>
</br>

####  1 - 3 Result Logcat
![image](https://github.com/Rhpark/library_01/blob/master/sample_img/SampleLogx.PNG)

<br>
</br>
<br>
</br>

### 2. Activity (Custom Activity, CustomViewModel)


####  2 - 1 XML
```

<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context=".MainActivity">

    <data>

        <variable
            name="vm"
            type="kr.open.rhpark.app.MainActivityVm" />

    </data>

    <FrameLayout style="@style/MainActivity.Root">

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <androidx.appcompat.widget.LinearLayoutCompat
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Hello World!" />

                <Button
                    style="@style/MainActivity.Button"
                    android:onClick="@{()->vm.onClickPermission()}"
                    android:text="permission" />

                <Button
                    style="@style/MainActivity.Button"
                    android:onClick="@{()->vm.onClickShowFragment()}"
                    android:text="Show Fragment" />

                <Button
                    style="@style/MainActivity.Button"
                    android:onClick="@{()->vm.onClickShowRecyclerviewActivity()}"
                    android:text="Show RecyclerView Activity" />
//...

```
<br>
</br>

<br>
</br>

####  2 - 2 ViewModel
```

class MainActivityVm : BaseViewModelEvent<MainActivityVmEvent>() {

    fun onClickPermission() {
        sendEventVm(
            MainActivityVmEvent.OnPermissionCheck(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
                )
            )
        )
    }

    private fun showActivity(activity: Class<*>) {
        sendEventVm(MainActivityVmEvent.OnShowActivity(activity))
    }

    fun onClickNetworkActivity() {
        showActivity(NetworkActivity::class.java)
    }
//..

```

<br>
</br>

####  2 - 3 ViewModelEvent
```

sealed class MainActivityVmEvent {
    data class OnPermissionCheck(val permissionList: List<String>) : MainActivityVmEvent()
    data class OnShowActivity(val activity: Class<*>) : MainActivityVmEvent()
}


```

<br>
</br>

####  2 - 4 Activity (Binding)
```

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainActivityVm by lazy { getViewModel<MainActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        test()

        lifecycleScope.launch {
            vm.mEventVm.collect {
                eventVM(it)
            }
        }
    }

    private fun eventVM(event: MainActivityVmEvent) = when (event) {
        is MainActivityVmEvent.OnPermissionCheck -> {
            requestPermissions(1231, event.permissionList) { requestCode, deniedPermissionList ->
                Logx.d("requestCode $requestCode, deniedPermissionList $deniedPermissionList")
            }
        }
        is MainActivityVmEvent.OnShowActivity -> startActivity(event.activity)
    }
//..

```

<br>
</br>

<br>
</br>

####  2 - 5 Fragment (Binding)
```

class FirstFragment:BaseBindingFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    private val vm: FirstFragmentVm by lazy { getViewModel<FirstFragmentVm>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logx.d()
        binding.vm = vm

        lifecycleScope.launch {
            vm.mEventVm.collect{
                when (it) {
                    is FirstFragmentVmEvent.OnPermissionCheck -> {
                        requestPermissions(12313, it.permissionList) { requestCode, deniedPermissions ->
                            Logx.d("requestCode $requestCode, \n deniedPermissions $deniedPermissions")
                        }
                    }
                    is FirstFragmentVmEvent.OnShowSnackBar -> {
                        snackBarShowShort(it.msg)
                    }
                    is FirstFragmentVmEvent.OnShowToast -> {
                        toastShowShort(it.msg)
                    }
                }
            }
        }
    }
}

```

<br>
</br>
<br>
</br>

### 3. Adapter,ListAdapter


####  3 - 1 Adapter
```
class RcvAdapter : BaseRcvAdapter<RcvItem, BaseRcvViewHolder<ItemRecyclerviewBinding>>() {

    override fun diffUtilAreItemsTheSame(oldItem: RcvItem, newItem: RcvItem): Boolean {
        return oldItem.key === newItem.key
    }

    override fun diffUtilAreContentsTheSame(oldItem: RcvItem, newItem: RcvItem): Boolean {
        return oldItem.key === newItem.key
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseRcvViewHolder<ItemRecyclerviewBinding> = BaseRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseRcvViewHolder<ItemRecyclerviewBinding>,
        position: Int,
        item: RcvItem
    ) {
        holder.binding.item = item
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }
}

```
<br>
</br>

<br>
</br>

####  3 - 2 List Adapter
```
class RcvListAdapter : BaseRcvListAdapter<RcvItem, BaseRcvViewHolder<ItemRecyclerviewBinding>>(
        RcvListDifUtilCallBack<RcvItem>(
            itemsTheSame = { oldItem, newItem -> oldItem.key == newItem.key },
            contentsTheSame = { oldItem, newItem -> oldItem.key === newItem.key }
        )
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseRcvViewHolder<ItemRecyclerviewBinding> =
        BaseRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseRcvViewHolder<ItemRecyclerviewBinding>, position: Int, item: RcvItem
    ) {
        holder.binding.item = item
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }
}

```

<br>
</br>
<br>
</br>

####  3 - 3 Simple Adapter
```
class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

//..
    private val rcvSimpleAdapter = RcvSimpleAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview) {
        holder, item, position -> holder.binding.item = item
    }.apply {
        setDiffUtilItemSame { oldItem, newItem -> oldItem.key === newItem.key }
        setDiffUtilContentsSame { oldItem, newItem -> oldItem.key == newItem.key }
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("simpleAdapter On Click posision $i, item $rcvItem")
        }
    }
//..

```

<br>
</br>

<br>
</br>

####  3 - 4 Simple List Adapter
```
class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

//..
    private val rcvListSimpleAdapter =
        RcvListSimpleAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview,
            onBind = { holder, item, position -> holder.binding.item = item },
            RcvListDifUtilCallBack<RcvItem>(
                { oldItem, newItem -> oldItem.key === newItem.key },
                { oldItem, newItem -> oldItem.key == newItem.key }
            )
        ).apply {
            setOnItemClickListener { i, rcvItem, view ->
                Logx.d("rcvListSimpleAdapter On Click posision $i, item $rcvItem")
            }
        }
//..

```

<br>
</br>

<br>
</br>

### 4. RecyclerScrollStateView(Custom RecyclerView)

#### 4 - 1 RecyclerStateViewConfig (Activity)
```
class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

//..
    private fun recyclerviewScrollStateSet() {
        lifecycleScope.launch {
            launch {
                binding.rcvList.sfEdgeReachedFlow.collect{
                    toastShowShort("edge : ${it.first}, isReached : ${it.second}")
                }
            }
            launch {
                binding.rcvList.sfScrollDirectionFlow.collect {
                    toastShowShort("scrollDirection : $it")
                }
            }
        }
//..

```

<br>
</br>

<br>
</br>

####  4 - 2 RecyclerStateViewConfig (XML)
```
//..
        <kr.open.rhpark.library.ui.view.recyclerview.RecyclerScrollStateView
            android:id="@+id/rcvList"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            app:edgeReachThreshold="10"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/rgRcvAdapterType"
            app:scrollDirectionThreshold="30" />
//..

```
<br>
</br>


<br>
</br>


### 5. SystemServiceManager 

####  5 - 1 VibratorController
```
class VibratorActivity : BaseBindingActivity<ActivityVibratorBinding>(R.layout.activity_vibrator) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(listOf(android.Manifest.permission.VIBRATE)){
            requestCode, deniedPermissions->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            initListener()
        }
    }

    private fun initListener() {
        binding.run {
            btnOneShort.setOnClickListener {
                if (edtKey.text.isEmpty()) {
                    toastShowShort("Input Timer")
                } else {
                    getVibratorController().createOneShot(edtKey.text.toString().toLong())
                }
            }

            btnWaveForm.setOnClickListener {
                val times = LongArray(3).apply { this[0] = 1000L; this[1] = 1000L; this[2] = 1000L }
                val amplitudes = IntArray(3).apply { this[0] = 64; this[1] = 255; this[2] = 128 }
                checkSdkVersion(Build.VERSION_CODES.S,
                    positiveWork = {
                        getVibratorController().createWaveform(times, amplitudes)
                    },
                    negativeWork = {
                        toastShowShort("Requires Os Version 31(S), but your Os Version is ${Build.VERSION.SDK_INT}")
                    }
                )
            }

            btnPredefined.setOnClickListener {
                checkSdkVersion(Build.VERSION_CODES.Q,
                    positiveWork = {
                        getVibratorController().createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                    },
                    negativeWork = {
                        toastShowShort("Requires Os Version 26(O), but your Os Version is ${Build.VERSION.SDK_INT}")
                    }
                )
            }

            btbCancel.setOnClickListener { getVibratorController().cancel() }
        }
    }
}

```


<br>
</br>

####  5 - 2 DisplayStateInfo
```
class DisplayActivity : BaseBindingActivity<ActivityDisplayBinding>(R.layout.activity_display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.run {
            btnDpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "DP to SP ${number.dpToSp(this@DisplayActivity)},\nDP to PX ${number.dpToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btnPxToDp.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "PX to DP ${number.pxToDp(this@DisplayActivity)},\nPX to SP ${number.pxToSp(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbSpToPx.setOnClickListener {
                if(!editNumberIsEmpty()) {
                    val number = binding.edtNumber.text.toString().toFloat()
                    val dpToPx = "SP to DP ${number.spToDp(this@DisplayActivity)},\nSP to PX ${number.spToPx(this@DisplayActivity)}\n"
                    tvResult.text = dpToPx
                }
            }

            btbScreenInfo.setOnClickListener {
                val getFullScreen =
                    "FullScreen = ${getDisplayInfo().getFullScreenSize()}\n" +
                            "ScreenWithStatusBar = ${getDisplayInfo().getScreenWithStatusBar()}\n" +
                            "Screen = ${getDisplayInfo().getScreen()}\n" +
                            "StatusBar height = ${statusBarHeight}\n" +
                            "NavigationBar height = ${navigationBarHeight}\n" +
                            "getStatusBarHeight = ${this@DisplayActivity.statusBarHeight}\n" +
                            "getNavigationBarHeight = ${this@DisplayActivity.navigationBarHeight}\n"

                tvResult.text = getFullScreen
            }
        }
    }
//..
}

```



<br>
</br>

####  5 - 3 BatteryStateInfo
```
class BatteryActivity : BaseBindingActivity<ActivityBatteryBinding>(R.layout.activity_battery) {

    private val batteryStateInfo: BatteryStateInfo by lazy { applicationContext.getBatteryStateInfo(lifecycleScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(listOf(android.Manifest.permission.BATTERY_STATS)) {
            requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions ")
            initListener()
        }
    }

    private fun initListener() {
        binding.btnBatteryStatusStart.setOnClickListener {
            batteryStateInfo.registerBatteryUpdate(updateCycleTime = 1000)
            collectBatteryInfo()
        }
    }

    private fun collectBatteryInfo() = lifecycleScope.launch {
        batteryStateInfo.sfUpdate.collect { type ->
            when (type) {
                is BatteryStateEvent.OnCapacity -> binding.tvCapacity.text = "Capacity = ${type.percent}"
                is BatteryStateEvent.OnChargeCounter -> binding.tvChargeCounter.text = "ChargeCounter = ${type.counter}"
                is BatteryStateEvent.OnChargePlug -> binding.tvChargeChargePlugStr.text = "ChargePlugStr = ${type.type}"
                is BatteryStateEvent.OnChargeStatus -> binding.tvChargeStatus.text = "OnChargeStatus = ${type.status}"
                is BatteryStateEvent.OnCurrentAmpere -> binding.tvCurrentAmpere.text = "Current Ampere = ${type.current} mA"
                is BatteryStateEvent.OnCurrentAverageAmpere -> binding.tvCurrentAverageAmpere.text = "Current AverageAmpere = ${type.current} mA"
                is BatteryStateEvent.OnEnergyCounter -> binding.tvEnergyCounte.text = "EnergyCounte = ${type.energy}"
                is BatteryStateEvent.OnHealth -> binding.tvHealth.text = "Health = ${type.health}"
                is BatteryStateEvent.OnPresent -> binding.tvPresent.text = "Present = ${type.present}"
                is BatteryStateEvent.OnTemperature -> binding.tvTemperature.text = "Temperature = ${type.temperature}"
                is BatteryStateEvent.OnTotalCapacity -> binding.tvTotalCapacity.text = "TotalCapacity = ${type.totalCapacity} "
                is BatteryStateEvent.OnVoltage -> binding.tvVoltage.text = "Charge voltage = ${type.voltage} v"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        batteryStateInfo.onDestroy()
    }
}

```

<br>
</br>


####  5 - 4 NetworkStateInfo

```
class NetworkActivity : BaseBindingActivity<ActivityNetworkBinding>(R.layout.activity_network) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(getPermissions()) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            if(deniedPermissions.isEmpty()) {
                initNetworkStateInfo()
            } else {
                toastShowShort("deniedPermissions $deniedPermissions ")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initNetworkStateInfo() {
        registerNetworkCallback()
        registerDefaultNetworkCallback()
        registerTelephonyCallback()
        registerLocationState()
        binding.tvTelephonyMaximumActiveSimCount.text = "MaximumUSimCount ${getNetworkStateInfo().getMaximumUSimCount()}\n\n"
        binding.tvTelephonyIsAbleEsim.text = "isAble Esim = ${getNetworkStateInfo().isESimSupport()}\n\n"
    }

    private fun registerNetworkCallback() {
        getNetworkStateInfo().registerNetworkCallback(Handler(Looper.getMainLooper()),
            onNetworkAvailable = { network: Network ->  updateAvailable("NetworkAvailable ",binding.tvConnectState, network) },
            onNetworkLosing = { network: Network, maxMsToLive: Int ->  updateAvailable("onNetworkLosing ",binding.tvConnectState, network)},
            onNetworkLost = { network: Network -> updateAvailable("onNetworkLost ",binding.tvConnectState, network)},
            onNetworkCapabilitiesChanged = { network: Network, networkCapabilities: NetworkCapabilitiesData ->
                binding.tvCapabilities.text = "networkCapabilities\n ${networkCapabilities.toResString()}" },
            onLinkPropertiesChanged = { network: Network, linkProperties: NetworkLinkPropertiesData ->
                binding.tvLinkProperties.text = "LinkProperties\n ${linkProperties.toResString()}"},
            onBlockedStatusChanged = { network: Network, blocked: Boolean ->
                Logx.d("onBlockedStatusChanged network $network blocked $blocked")
            }
        )
    }

    private fun registerDefaultNetworkCallback() {
        getNetworkStateInfo().registerDefaultNetworkCallback(Handler(Looper.getMainLooper()),
            onNetworkAvailable = { network: Network -> updateAvailable("Default NetworkAvailable ",binding.tvConnectDefaultState, network) },
            onNetworkLosing = { network: Network, maxMsToLive: Int -> updateAvailable("Default NetworkLosing ",binding.tvConnectDefaultState, network) },
            onNetworkLost = { network: Network -> updateAvailable("Default NetworkLost ",binding.tvConnectDefaultState, network) },
            onNetworkCapabilitiesChanged = { network: Network, networkCapabilities: NetworkCapabilitiesData ->
                binding.tvDefaultCapabilities.text = "Default networkCapabilities\n ${networkCapabilities.toResString()}" },
            onLinkPropertiesChanged = { network: Network, linkProperties: NetworkLinkPropertiesData ->
                binding.tvDefaultLinkProperties.text = "Default LinkProperties\n ${linkProperties.toResString()}"},
            onBlockedStatusChanged = { network: Network, blocked: Boolean ->
                Logx.d("onBlockedStatusChanged $blocked")
            }
        )
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerTelephonyCallback(isGpsOn: Boolean = false) {
        Logx.d("isGpsOn $isGpsOn")
        try {
            checkSdkVersion(Build.VERSION_CODES.S,
                positiveWork = {
                    getNetworkStateInfo().registerTelephonyCallBackFromDefaultUSim(
                        this@NetworkActivity.mainExecutor, isGpsOn,
                        onActiveDataSubId = { subId: Int -> updateActiveDataSubId(subId) },
                        onDataConnectionState = { state: Int, networkType: Int -> updateConnectState(state,networkType) },
                        onCellInfo = { currentCellInfo: CurrentCellInfo -> updateCellInfo(currentCellInfo) },
                        onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->updateSignalStrength(currentSignalStrength) },
                        onServiceState = { currentServiceState: CurrentServiceState -> updateServiceState(currentServiceState) },
                        onCallState = { callState: Int, phoneNumber: String? ->updateCallState(callState,phoneNumber) },
                        onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo -> updateDisplayInfo(telephonyDisplayInfo) },
                        onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState -> updateNetworkState(telephonyNetworkState) })
                },
                negativeWork = {
                    getNetworkStateInfo().registerTelephonyListenFromDefaultUSim(isGpsOn,
                        onActiveDataSubId = { subId: Int -> updateActiveDataSubId(subId) },
                        onDataConnectionState = { state: Int, networkType: Int -> updateConnectState(state,networkType) },
                        onCellInfo = { currentCellInfo: CurrentCellInfo -> updateCellInfo(currentCellInfo) },
                        onSignalStrength = { currentSignalStrength: CurrentSignalStrength ->updateSignalStrength(currentSignalStrength) },
                        onServiceState = { currentServiceState: CurrentServiceState -> updateServiceState(currentServiceState) },
                        onCallState = { callState: Int, phoneNumber: String? ->updateCallState(callState,phoneNumber) },
                        onDisplayInfo = { telephonyDisplayInfo: TelephonyDisplayInfo -> updateDisplayInfo(telephonyDisplayInfo) },
                        onTelephonyNetworkState = { telephonyNetworkState: TelephonyNetworkState -> updateNetworkState(telephonyNetworkState) })
                }
            )
        } catch (e:IllegalArgumentException) {
            e.printStackTrace()
            toastShowShort(e.toString())
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun registerLocationState() {
        getGpsStateInfo().apply {
            registerLocationOnOffState()
            lifecycleScope.launch {
                sfUpdate.collect{ type->
                    when(type) {
                        is LocationStateEvent.OnGpsEnabled ->registerTelephonyCallback(type.isEnabled)
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getNetworkStateInfo().onDestroy()
    }

    @RequiresPermission(READ_PHONE_STATE)
    private fun getSimStatus(): String {
        var simStatus = ""
        getNetworkStateInfo().getActiveSimSlotIndexList().forEachIndexed { index, i ->
            simStatus += "index $index, status $i\n"
        }
        return simStatus
    }

    private fun getPermissions() = checkSdkVersion(Build.VERSION_CODES.S,
        positiveWork = {
            listOf(
                ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION,
                READ_PHONE_STATE, READ_PHONE_NUMBERS
            )
        }, negativeWork = {
            listOf(
                ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_PHONE_STATE,
            )
        })


    @RequiresPermission(READ_PHONE_STATE)
    private fun updateActiveDataSubId(subId: Int) {
        binding.tvTelephonyActiveSubId.text = "\n Telephony\n\n ActiveDataSubId\n $subId\n\n"
        binding.tvTelephonyActiveSimCount.text = "getActiveSimCount ${getNetworkStateInfo().getActiveSimCount()}"
        binding.tvTelephonyActiveSimStatus.text = "Sim Status ${getSimStatus()}"
    }

    private fun updateAvailable(msg:String ,tv: TextView, network: Network) {
        tv.text = "$msg network $network, " +
                "is Wifi Connect ${getNetworkStateInfo().isConnectedWifi()}, isMobileConnect ${getNetworkStateInfo().isConnectedMobile()}\n\n"
    }
    private fun updateCellInfo(currentCellInfo: CurrentCellInfo) {
        binding.tvTelephonyCellInfo.text = "Default CellInfo \n ${currentCellInfo.toResString()}\n"
    }

    private fun updateSignalStrength(currentSignalStrength: CurrentSignalStrength) {
        binding.tvTelephonySignalStrength.text = "Default SignalStrength\n ${currentSignalStrength.toResString()}\n"
    }

    private fun updateConnectState(state: Int, networkType: Int) {
        binding.tvTelephonyConnectState.text = "Default ConnectState \n $state , networkType $networkType\n\n"
    }

    private fun updateServiceState(currentServiceState: CurrentServiceState) {
        binding.tvTelephonyServiceState.text = "Default ServiceState\n ${currentServiceState.toResString()}\n\n"
    }

    private fun updateCallState(callState: Int, phoneNumber: String?) {
        binding.tvTelephonyCallState.text = "Default callState\n $callState, phoneNumber $phoneNumber\n\n"
    }

    private fun updateDisplayInfo(telephonyDisplayInfo: TelephonyDisplayInfo) {
        binding.tvTelephonyDisplayInfo.text = "Default DisplayInfo\n $telephonyDisplayInfo\n\n"
    }

    private fun updateNetworkState(telephonyNetworkState: TelephonyNetworkState) {
        binding.tvTelephonyNetworkState.text = "Default TelephonyNetworkState\n $telephonyNetworkState\n\n"
    }
    private fun getNetworkStateInfo() = applicationContext.getNetworkStateInfo()
    private fun getGpsStateInfo() = applicationContext.getLocationStateInfo(lifecycleScope)
}

```

<br>
</br>

<br>
</br>

####  5 - 5 FloatingViewController
```
public class WindowActivity : BaseBindingActivity<ActivityWindowBinding>(R.layout.activity_window) {

    private val windowManagerController by lazy { applicationContext.getFloatingViewControllerController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(listOf(Manifest.permission.SYSTEM_ALERT_WINDOW)) { requestCode, deniedPermissionList ->
            Logx.d("requestCode $requestCode, deniedPermissionList $deniedPermissionList")
            if(deniedPermissionList.isEmpty()) {
                initListener()
            }
        }
    }

    private fun initListener() = binding.run {
        btnAddDragView.setOnClickListener {
            addFloatingListener()
        }

        binding.btnAddFixedView.setOnClickListener {
            val fixedView: ImageView = getImageView(R.drawable.ic_floating_fixed_close)
            windowManagerController.setFloatingFixedView(
                FloatingDragView(
                    fixedView,
                    (getDisplayInfo().getFullScreenSize().x / 2),
                    (getDisplayInfo().getFullScreenSize().y / 2),
                )
            )
            fixedView.setGone()
        }

        btnRemoveView.setOnClickListener { windowManagerController.removeAllFloatingView() }
    }

    private fun addFloatingListener() {
        Logx.d()
        val dragView = getImageView(R.drawable.ic_launcher_foreground).apply {
            setBackgroundColor(Color.WHITE)
        }
        windowManagerController.addFloatingDragView(FloatingDragView(dragView, 0, 0).apply {
            lifecycleScope.launch {
                sfCollisionStateFlow.collect { item ->
                    when (item.first) {
                        FloatingViewTouchType.TOUCH_DOWN -> { showFloatingView() }

                        FloatingViewTouchType.TOUCH_MOVE -> { moveFloatingView(item) }

                        FloatingViewTouchType.TOUCH_UP -> { upFloatingView(this@apply,item) }
                    }
                }
            }
        })
    }

    private fun showFloatingView() {
        windowManagerController.getFloatingFixedView()?.view?.let {
            it.setVisible()
            showAnimScale(it, null)
        }
    }

    private fun moveFloatingView(item: Pair<FloatingViewTouchType, FloatingViewCollisionsType>) {
        windowManagerController.getFloatingFixedView()?.view?.let {
            if (item.second == FloatingViewCollisionsType.OCCURING) {
                val rotationAnim =
                    ObjectAnimator.ofFloat(it, "rotation", 0.0f, 180.0f)
                rotationAnim.duration = 300
                rotationAnim.start()
            }
        }
    }

    private fun upFloatingView(floatingView:FloatingDragView,item: Pair<FloatingViewTouchType, FloatingViewCollisionsType>) {
        windowManagerController.getFloatingFixedView()?.view?.let {
            hideAnimScale(it, object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    windowManagerController.getFloatingFixedView()?.let { it.view.setGone() }
                    if (item.second == FloatingViewCollisionsType.OCCURING) {
                        windowManagerController.removeFloatingDragView(floatingView)
                    }
                }
            })
        }
    }

    private fun showAnimScale(view: View, listener: Animator.AnimatorListener?) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = 300
            listener?.let { addListener(it) }
            start()
        }
    }

    private fun hideAnimScale(view: View, listener: Animator.AnimatorListener?) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = 300
            listener?.let { addListener(it) }
            start()
        }
    }

    private fun getImageView(res: Int): ImageView = ImageView(applicationContext).apply {
        setImageResource(res)
        setOnClickListener { Logx.d("OnClick Listener") }
    }
}

```


<br>
</br>

<br>
</br>

####  5 - 6 Notification
```
class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {

    private val CHANNEL_ID = "Channel_ID_01"
    private val CHANNEL_NAME = "Channel_NAME_01"
    private val data01 = "PutData_01"
    private val dataAction01 = "PutData_Action_01"
    private val dataAction02 = "PutData_Action_02"

    private val notificationController by lazy { getNotificationController() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        requestPermissions(listOf(POST_NOTIFICATIONS)) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {
                initListener()
            }
        }

        intent.extras?.run {
            val data01 = getString(data01)
            val data02 = getString(dataAction01)
            val data03 = getInt(dataAction02)
            binding.tvReceiveIntentData.text = "intent extras = $data01, action 01 =  $data02, action 02 =  ${data03}"
        } ?: Logx.d("intent.extras is Null")
    }

    private fun initListener() {
        binding.run {
            initNotificationChannel()

            btnBigTextNotification.setOnClickListener {

                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 2
                val getNotificationIntent1 = getNotificationIntent(4, dataAction01, "action01 Click", "Action01",notificationId)
                val getNotificationIntent2 = getNotificationIntent(4, dataAction02, 12313, "Action02",16)


                notificationController.showNotificationBigTextForActivity(
                    notificationId,
                    "BigTextNotification",
                    "BigText",
                    true,
                    R.drawable.ic_floating_fixed_close,
                    snippet = "Fesafasdfasefdsfasefesadf asef sadf asdf asefas fdsf seaf safawef aa feasf sadfasefasdf asdf asf aesf asdf asfe saedf asef sadf asef asdf asefseaf",
                    clickIntent = clickIntent,
                    largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                    actions = listOf(getNotificationIntent1, getNotificationIntent2)
                )
            }

            btnBitImageNotification.setOnClickListener {

                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 4
                val getNotificationIntent1 = getNotificationIntent(4, dataAction01, "action01 Click", "Action01",notificationId)
                val getNotificationIntent2 = getNotificationIntent(4, dataAction02, 12313, "Action02",6)


                notificationController.showNotificationBigImageForActivity(
                    notificationId,
                    "BigImageNotification",
                    "BigImage",
                    true,
                    R.drawable.ic_floating_fixed_close,
                    clickIntent = clickIntent,
                    largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.bg_notifi),
                    actions = listOf(getNotificationIntent1, getNotificationIntent2)
                )
            }

            btnShowProgressNotification.setOnClickListener {
                val clickIntent = Intent(applicationContext, NotificationActivity::class.java)
                val notificationId = 16
                val actionID = 26

                val actionIntent = Intent(applicationContext, NotificationActivity::class.java).apply {
                        putExtra(data01, "Pending01")
                        putExtra(dataAction01, 123)
                }

                val builder = notificationController.showProgressNotificationForActivity(
                    notificationId,
                    "TitleProgress01",
                    "ContentsProgress01",
                    false,
                    R.drawable.ic_floating_fixed_close,
                    clickIntent = clickIntent,
                    progressPercent = 0,
                )

                thread {
                    for (i in 0..10) {
                        builder.setProgress(100, i * 10, false)
                        notificationController.notify(notificationId, builder.build())
                        sleep(1000)
                    }
                    builder.addAction(getNotificationIntent(notificationId, dataAction01, "action01 Click", "Action01",actionID))
                    notificationController.notify(notificationId, builder.build())
                }
            }

            btnAllClearNotification.setOnClickListener {
                notificationController.cancelAll()
            }
        }
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Channel_Description_01"
            setShowBadge(true)

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audio = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(uri, audio)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 100, 200)
        }

        notificationController.createChannel(channel)
    }

    private fun getNotificationIntent(notificationId:Int, actionKey:String, actionValue:Any, title:String,actionId:Int): NotificationCompat.Action {

        val actionIntent1 = Intent(applicationContext, NotificationActivity::class.java).apply {
            when(actionValue) {
                is Int -> putExtra(actionKey, actionValue)
                is String -> putExtra(actionKey, actionValue)
                is Byte -> putExtra(actionKey, actionValue)
                is Char -> putExtra(actionKey, actionValue)
                is Float -> putExtra(actionKey, actionValue)
                is Long -> putExtra(actionKey, actionValue)
                is Double -> putExtra(actionKey, actionValue)
                is Boolean -> putExtra(actionKey, actionValue)
            }
        }
        val actionPendingIntent1 = NotificationCompat.Action(
            notificationId,
            title,
            notificationController.getClickShowActivityPendingIntent(actionId, actionIntent1)
        )
        return actionPendingIntent1
    }
}

```


<br>
</br>

<br>
</br>

####  5 - 7 Alarm
```
class AlarmActivity :
    BaseBindingActivity<ActivityAlarmBinding>(R.layout.activity_alarm) {

    private val alarmController by lazy { getAlarmController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(getPermissionList()) { requestCode, deniedPermissions ->
            Logx.d("requestCode $requestCode, deniedPermissions $deniedPermissions")
            if (deniedPermissions.isEmpty()) {

            }
            initListener()
        }
    }

    private fun initListener() {
        binding.run {
            btnAlarmRegister.setOnClickListener {
                val edit = edtTimer.text
                if (edit.isNullOrEmpty()) {
                    toastShowShort("input Min timer")
                } else if (edit.toString().toInt() < 1) {
                    toastShowShort("over than 0")
                }else {
                    Logx.d()
                    val min = edtTimer.text.toString().toInt()
                    val localDataTime = LocalDateTime.now()
                    val a = AlarmDTO(
                        key = 1,
                        title = "test002",
                        msg = "msg002",
                        isActive = true,
                        isAllowIdle = true,
                        vibrationEffect = longArrayOf(0,250,500,250),
                        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                        hour =localDataTime.hour,
                        minute = localDataTime.minute + min ,
                        second = 0
                    )
                    AlarmSharedPreference(applicationContext).saveAlarm(a)
                    alarmController.registerAlarmClock(AlarmReceiver::class.java, a)
                }
            }
        }
    }

    private fun getPermissionList(): List<String> {
        val list = mutableListOf<String>()
        list.add(RECEIVE_BOOT_COMPLETED)
        list.add(WAKE_LOCK)
        checkSdkVersion(Build.VERSION_CODES.TIRAMISU) {
            list.add(USE_EXACT_ALARM)
            list.add(POST_NOTIFICATIONS)
        }
        checkSdkVersion(Build.VERSION_CODES.S) {
            list.add(SCHEDULE_EXACT_ALARM)
        }
        return list.toList()
    }

}

public class AlarmReceiver() : BaseAlarmReceiver() {

//    override val registerType = RegisterType.ALARM_EXACT_AND_ALLOW_WHILE_IDLE
    override val registerType = RegisterType.ALARM_CLOCK

    override val classType: Class<*> = this::class.java

    override val powerManagerAcquireTime: Long get() = 5000L

    override fun loadAllAlarmDtoList(context:Context): List<AlarmDTO> {
        //data load from realm or room or sharedpreference or other
        return emptyList<AlarmDTO>()
    }

    override fun loadAlarmDtoList(context:Context, intent: Intent, alarmKey: Int): AlarmDTO? {
        Logx.d("alarmKey is " + alarmKey)
        if(alarmKey == AlarmVO.ALARM_KEY_DEFAULT_VALUE) {
            Logx.e("Error Alarm Key $alarmKey")
            return null
        }

        //data load from realm or room or  other
        return AlarmSharedPreference(context).loadAlarm()
    }

    override fun createNotificationChannel(context: Context, alarmDto: AlarmDTO) {
        Logx.d()
        notificationController = context.getNotificationController().apply {
            createChannel(
                NotificationChannel("Alarm_ID", "Alarm_Name", NotificationManager.IMPORTANCE_HIGH).apply {
//            setShowBadge(true)
                    alarmDto.vibrationEffect?.let {
                        enableVibration(true)
                        vibrationPattern = it
                    }
                    alarmDto.sound?.let {
                        setSound(
                            it, AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                        )
                    }
                }
            )
        }
    }
    override fun showNotification(context: Context, alarmDto: AlarmDTO) {
        Logx.d()

        notificationController.showNotificationForBroadcast(
            alarmDto.key,
            alarmDto.title,
            alarmDto.msg,
            false,
            R.drawable.ic_floating_fixed_close,
            null,
            null,
            null
        )
    }
}

```

<br>
</br>

<br>
</br>

### 6. Toast, SnackBar, softkeyboardContorller
```
class ToastSnackBarActivity :
    BaseBindingActivity<ActivityToastSnackbarBinding>(R.layout.activity_toast_snackbar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            applicationContext.getSoftKeyboardController().showDelay(editText,200L)

            btnDefaultToast.setOnClickListener {
                toastShowShort("Toast Show Short")
            }

            checkSdkVersion(
                Build.VERSION_CODES.R,
                positiveWork = { btnCustomToast.setGone() },
                negativeWork = { btnCustomToast.setVisible() }
            )

            btnCustomToast.setOnClickListener {
                toastShort("Option").apply {
                    setGravity(Gravity.CENTER_VERTICAL,0,0)
                    view?.setBackgroundColor(Color.YELLOW)
                }.show()
            }

            btnDefaultSnackBar.setOnClickListener { v -> v.snackBarShowShort("Default SnackBar") }

            btnActionSnackBar.setOnClickListener { v ->
                v.snackBarShowShort("TestMsg", actionText = "Actino_1") { toastShowShort("Click Action_1") }
            }

            btnOptionSnackBar.setOnClickListener { v->
                v.snackBarShowIndefinite(
                    "Option_Test",
                    animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE,
                    bgTint = Color.WHITE,
                    textColor = Color.RED,
                    actionTextColor = Color.BLUE,
                    actionText = "Action_01",
                    ) { toastShowShort("OnCLick Action_01") }
            }
        }
    }
}

```



<br>
</br>
<br>
</br>

### 7. Style.xml

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    

    <style name="Example" />
    <style name="Example.Layout" parent="Layout.MatchWarp.Vertical">
        <item name="android:textSize">12sp</item>
    </style>

    <style name="Example.TextView" parent="TextView.MatchWarp.Bold">
        <item name="android:textSize">12sp</item>
    </style>

    <style name="Example.Button" parent="View.MatchWarp">
    </style>

</resources>

```

<br>
</br>

### 8. If Inline

```
{
        ifGreaterThan(2,1){
            Logx.d("is positive")
        }
        ifGreaterThan(2,2,
            positiveWork = {
                Logx.d("is positive")
            },
            negativeWork = {
                Logx.d("is negative")
            }
        )
        ifGreaterThanOrEqual(2L,2L){
            Logx.d("is positive")
        }
        ifGreaterThanOrEqual(2L,2L,
            positiveWork = {
                Logx.d("is positive")
            },
            negativeWork = {
                Logx.d("is negative")
            }
        )

        ifEquals(1.1,1.1){
            Logx.d("is positive")
        }

        ifEquals(0.1, 1.1,
            positiveWork = {
                Logx.d("is positive")
            },
            negativeWork = {
                Logx.d("is negative")
            }
        )

        checkSdkVersion(Build.VERSION_CODES.S,
            positiveWork = { Logx.d("is positive")},
            negativeWork = { Logx.d("is negative") }
        )

        checkSdkVersion(Build.VERSION_CODES.S){ Logx.d("is positive") }
}

```

