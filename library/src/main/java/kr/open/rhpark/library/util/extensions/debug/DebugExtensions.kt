package kr.open.rhpark.library.util.extensions.debug

import kr.open.rhpark.library.debug.logcat.Logx

public /*inline*/ fun Any.logxD(): Unit = Logx.d1(this)
public /*inline*/ fun Any.logxD(tag: String): Unit = Logx.d1(tag, this)

public /*inline*/ fun Any.logxV(): Unit = Logx.v1(this)
public /*inline*/ fun Any.logxV(tag: String): Unit = Logx.v1(tag, this)

public /*inline*/ fun Any.logxW(): Unit = Logx.w1(this)
public /*inline*/ fun Any.logxW(tag: String): Unit =  Logx.w1(tag,this)

public /*inline*/ fun Any.logxI(): Unit = Logx.i1(this)
public /*inline*/ fun Any.logxI(tag: String): Unit = Logx.i1(tag,this)

public /*inline*/ fun Any.logxE(): Unit = Logx.e1(this)
public /*inline*/ fun Any.logxE(tag: String): Unit = Logx.e1(tag,this)

public /*inline*/ fun String.logxJ(): Unit = Logx.j1(this)
public /*inline*/ fun String.logxJ(tag: String): Unit = Logx.j1(tag,this)

public /*inline*/ fun Any.logxP(): Unit = Logx.p1(this)
public /*inline*/ fun Any.logxP(tag:String): Unit = Logx.p1(tag,this)
