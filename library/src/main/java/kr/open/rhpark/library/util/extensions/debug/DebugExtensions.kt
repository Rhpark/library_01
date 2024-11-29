package kr.open.rhpark.library.util.extensions.debug

import kr.open.rhpark.library.debug.logcat.Logx


public fun Any.logxD(): Unit = Logx.d1(this)
public fun Any.logxD(tag: String): Unit = Logx.d1(tag, this)

public fun Any.logxV(): Unit = Logx.v1(this)
public fun Any.logxV(tag: String): Unit = Logx.v1(tag, this)

public fun Any.logxW(): Unit = Logx.w1(this)
public fun Any.logxW(tag: String): Unit =  Logx.w1(tag,this)

public fun Any.logxI(): Unit = Logx.i1(this)
public fun Any.logxI(tag: String): Unit = Logx.i1(tag,this)

public fun Any.logxE(): Unit = Logx.e1(this)
public fun Any.logxE(tag: String): Unit = Logx.e1(tag,this)

public fun Any.logxJ(): Unit = Logx.j1(this.toString())
public fun Any.logxJ(tag: String): Unit = Logx.j1(tag,this.toString())

public fun Any.logxP(): Unit = Logx.p1(this)
public fun Any.logxP(tag:String): Unit = Logx.p1(tag,this)
