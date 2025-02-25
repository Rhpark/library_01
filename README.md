# Android Easy Development Library (v0.9.1)

![image](https://jitpack.io/v/rhpark/library_01.svg)
<br>
</br>
This library helps you make easy and more simple code for Android developers
<br>
</br>
<br>
</br>
Android 개발자를 위해
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
        <kr.open.rhpark.library.ui.recyclerview.view.RecyclerScrollStateView
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getVibratorController().createWaveform(times, amplitudes)
                } else {
                    toastShowShort("Requires Os Version 31(S), but your Os Version is ${Build.VERSION.SDK_INT}")
                }
            }

            btnPredefined.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    getVibratorController().createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                } else {
                    toastShowShort("Requires Os Version 26(O), but your Os Version is ${Build.VERSION.SDK_INT}")
                }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
            } else {
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
![image](https://github.com/Rhpark/library_01/blob/master/sample_img/Sample_Base_SystemManager_FloatingController.png)



<br>
</br>
<br>
</br>

### 6. Toast, SnackBar
![image](https://github.com/Rhpark/library_01/blob/master/sample_img/Sample_Base_SystemManager_Toast_Snackbar.png)



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
	implementation("com.github.rhpark:library_01:0.9.1")
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



