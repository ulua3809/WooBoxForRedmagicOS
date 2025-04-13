package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.android.*
import com.u9521.wooboxforredmagicos.hook.app.android.corepatch.CorePatch
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android : AppRegister() {
    override val packageName: List<String> = listOf("android")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        //核心破解
        CorePatch().handleLoadPackage(lpparam)

        autoInitHooks(
            lpparam,
            DisableFlagSecure, //允许截图
            RemoveAlertWindowsNotification, //上层显示通知
            VolumeStepHook, //音量阶数Hook
            AllowUntrustedTouches, //允许不受信任的触摸
        )
    }

}