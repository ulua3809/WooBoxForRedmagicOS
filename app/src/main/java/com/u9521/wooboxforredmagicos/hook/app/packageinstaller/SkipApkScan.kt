package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge

object SkipApkScan : HookRegister() {
    override fun init() = hasEnable("skip_apk_scan") {
        //skip scan
        // ZTE_FEATURE_ODM_VERTU
        MethodFinder.fromClass("com.android.packageinstaller.InstallStaging\$StagingAsyncTask")
            .filterByName("onPostExecute").filterByParamTypes(java.lang.Object::class.java).first()
            .createBeforeHook(block = {
                Log.i("[packageinstaller] package is about to scan")
                var vertuclzz = ClassUtils.loadClass("com.android.packageinstaller.PackageUtil")
                ClassUtils.setStaticObject(vertuclzz, "ZTE_FEATURE_ODM_VERTU", true)
            })
    }
}