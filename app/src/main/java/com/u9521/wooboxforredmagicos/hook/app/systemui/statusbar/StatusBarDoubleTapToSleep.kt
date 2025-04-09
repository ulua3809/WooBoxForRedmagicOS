package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object StatusBarDoubleTapToSleep : HookRegister() {

    override fun init() = hasEnable("status_bar_double_tap_to_sleep") {
        return@hasEnable
//        findMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView") {
//            name == "onFinishInflate"
//        }.hookBefore {
//            val view = it.thisObject as ViewGroup
//            XposedHelpers.setAdditionalInstanceField(view, "currentTouchTime", 0L)
//            XposedHelpers.setAdditionalInstanceField(view, "currentTouchX", 0f)
//            XposedHelpers.setAdditionalInstanceField(view, "currentTouchY", 0f)
//            view.setOnTouchListener(OnTouchListener { v, event ->
//                if (event.action != MotionEvent.ACTION_DOWN) return@OnTouchListener false
//                var currentTouchTime =
//                    XposedHelpers.getAdditionalInstanceField(view, "currentTouchTime") as Long
//                var currentTouchX =
//                    XposedHelpers.getAdditionalInstanceField(view, "currentTouchX") as Float
//                var currentTouchY =
//                    XposedHelpers.getAdditionalInstanceField(view, "currentTouchY") as Float
//                val lastTouchTime = currentTouchTime
//                val lastTouchX = currentTouchX
//                val lastTouchY = currentTouchY
//                currentTouchTime = System.currentTimeMillis()
//                currentTouchX = event.x
//                currentTouchY = event.y
//                if (currentTouchTime - lastTouchTime < 250L && Math.abs(currentTouchX - lastTouchX) < 100f && Math.abs(
//                        currentTouchY - lastTouchY
//                    ) < 100f
//                ) {
//                    XposedHelpers.callMethod(
//                        v.context.getSystemService(Context.POWER_SERVICE),
//                        "goToSleep",
//                        SystemClock.uptimeMillis()
//                    )
//                    currentTouchTime = 0L
//                    currentTouchX = 0f
//                    currentTouchY = 0f
//                }
//                XposedHelpers.setAdditionalInstanceField(
//                    view,
//                    "currentTouchTime",
//                    currentTouchTime
//                )
//                XposedHelpers.setAdditionalInstanceField(view, "currentTouchX", currentTouchX)
//                XposedHelpers.setAdditionalInstanceField(view, "currentTouchY", currentTouchY)
//                v.performClick()
//                false
//            })
//        }
    }
}
