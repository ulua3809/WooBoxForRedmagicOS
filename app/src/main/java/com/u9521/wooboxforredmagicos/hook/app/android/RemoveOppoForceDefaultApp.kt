package com.u9521.wooboxforredmagicos.hook.app.android

import android.content.ComponentName
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveOppoForceDefaultApp : HookRegister() {
    override fun init() = hasEnable("remove_oppo_default_app") {
        return@hasEnable
//        findMethod("com.android.server.pm.OplusOsPackageManagerHelper") {
//            name == "isOplusForceApp" && parameterTypes[0] == ComponentName::class.java
//        }.hookReturnConstant(false)
    }
}