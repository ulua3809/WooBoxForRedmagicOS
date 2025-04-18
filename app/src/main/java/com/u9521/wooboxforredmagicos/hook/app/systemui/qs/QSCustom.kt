package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.content.res.Configuration
import android.view.ViewGroup
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object QSCustom : HookRegister() {
    override fun init() = hasEnable("qs_custom_switch") {
        return@hasEnable
        val mRows = XSPUtils.getInt("qs_custom_rows", 4)
        val mRowsHorizontal = XSPUtils.getInt("qs_custom_rows_horizontal", 2)
        val mColumns = XSPUtils.getInt("qs_custom_columns", 4)
        val mColumnsUnexpanded = XSPUtils.getInt("qs_custom_columns_unexpanded", 6)

//        findMethod("com.android.systemui.qs.QuickQSPanel") {
//            name == "getNumQuickTiles"
//        }.hookBefore {
//            //未展开时的列数
//            it.result = mColumnsUnexpanded
//        }
//
//        findMethod("com.android.systemui.qs.TileLayout") {
//            name == "updateColumns"
//        }.hookAfter {
//            it.thisObject.putObject("mColumns", mColumns)
//        }
//
//        findMethod("com.android.systemui.qs.TileLayout") {
//            name == "updateResources"
//        }.hookAfter {
//            //展开时的行数
//            val viewGroup = it.thisObject as ViewGroup
//            val mConfiguration: Configuration = viewGroup.context.resources.configuration
//            if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                it.thisObject.putObject("mMaxAllowedRows", mRows)
//            } else {
//                it.thisObject.putObject("mMaxAllowedRows", mRowsHorizontal)
//
//            }
//            viewGroup.requestLayout()
//        }
    }
}