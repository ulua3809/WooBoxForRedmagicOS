package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.settings.*
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Settings : AppRegister() {
    override val packageName: List<String> = listOf("com.android.settings")

    override val processName: List<String> = emptyList()

    override val logTag: String = "Woobox"
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            UsbInstallNoVerify, //取消usb安装账号验证
            FroceDisplayZvioceSwitch,//强制在手势设置中显示手势打开智慧语言
            FroceDisplayWakeupGoogleAssistantSwitch,//强制显示长按电源建打开Google助理
        )
    }

}