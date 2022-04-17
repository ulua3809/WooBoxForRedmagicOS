package com.lt2333.wooboxforcoloros.hook.app

import com.lt2333.wooboxforcoloros.hook.app.launcher.*
import com.lt2333.wooboxforcoloros.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Launcher : AppRegister() {
    override val packageName: List<String> = listOf("com.oppo.launcher","com.android.launcher")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            UnlockRecentLocks, //解除后台锁上限
        )
    }
}