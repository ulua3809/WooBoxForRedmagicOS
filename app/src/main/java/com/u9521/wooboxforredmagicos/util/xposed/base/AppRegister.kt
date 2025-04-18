package com.u9521.wooboxforredmagicos.util.xposed.base

import com.github.kyuubiran.ezxhelper.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class AppRegister : IXposedHookLoadPackage {

    abstract val packageName: List<String>
    abstract val processName: List<String>
    abstract val logTag: String

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {}

    protected fun autoInitHooks(
        lpparam: XC_LoadPackage.LoadPackageParam,
        vararg hook: HookRegister
    ) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.setLoadPackageParam(lpparam)
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.fold(
                onSuccess = {},
                onFailure = { Log.ix(it, "Failed init hook: ${it.javaClass.simpleName}") })
        }
    }

}