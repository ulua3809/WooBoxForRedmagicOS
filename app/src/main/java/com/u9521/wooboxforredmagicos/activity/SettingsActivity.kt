package com.u9521.wooboxforredmagicos.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextSummaryWithArrowV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.util.ShellUtils
import com.u9521.wooboxforredmagicos.util.XSPUtils

import kotlin.system.exitProcess

class SettingsActivity : MIUIActivity() {

    private val activity = this
    private var lsposedLoaded = true
    override fun onCreate(savedInstanceState: Bundle?) {
        checkLSPosed()
        getVersionCommit()
        super.onCreate(savedInstanceState)
    }

    //检测LSPosed是否激活
    @Suppress("DEPRECATION")
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("WooboxConfig", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            val viewonlySP = getSharedPreferences("viewonly", MODE_PRIVATE)
//            reset onstart
            viewonlySP.edit().clear().apply()
            lsposedLoaded = false
            setSP(viewonlySP)
            MIUIDialog(this) {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setLButton(R.string.ignore) {
                    dismiss()
                }
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    //获取APP Manifest versionCommit
    private fun getVersionCommit() {
        if (lsposedLoaded) {
            val keyList = arrayOf("PackageInstallCommit", "AlarmClockCommit")
            val packageList = arrayOf("com.android.packageinstaller", "com.android.htmlviewer")
            for ((index, value) in keyList.withIndex()) {
                val appcommit = getAppCommit(packageList[index]) ?: "null"
                safeSP.putAny(value, appcommit)
            }
        }
    }

    private fun getAppCommit(packageName: String): String? {
        return try {
            val packageManager = this.packageManager
            packageManager.getApplicationInfo(
                packageName, PackageManager.GET_META_DATA
            ).metaData.getString("versionCommit")
        } catch (exception: java.lang.Exception) {
            null
        }
    }


    private fun getLsposedInactiveTip(): TextSummaryV? {
        if (!lsposedLoaded) {
            return TextSummaryV(
                textId = R.string.lsposed_inactive_wraning,
                colorId = R.color.red,
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.matters_needing_attention)
                        setMessage(
                            R.string.lsposed_inactive_tips
                        )
                        setRButton(R.string.Done) {
                            dismiss()
                        }
                    }.show()
                })
        } else {
            return null
        }
    }

    init {
        initView {
            registerMain(getString(R.string.app_name), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.main_switch, colorId = R.color.purple_700),
                    SwitchV("main_switch", true)
                )
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.HideLauncherIcon),
                    SwitchV("hLauncherIcon", onClickListener = {
                        packageManager.setComponentEnabledSetting(
                            ComponentName(activity, "${BuildConfig.APPLICATION_ID}.launcher"),
                            if (it) {
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            } else {
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            },
                            PackageManager.DONT_KILL_APP
                        )
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.matters_needing_attention,
                        colorId = R.color.red,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.matters_needing_attention)
                                setMessage(
                                    R.string.matters_needing_attention_context
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                            }.show()
                        })
                )
                Line()
                TitleText(textId = R.string.scope)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemui,
                        tipsId = R.string.scope_systemui_summary,
                        onClickListener = { showFragment("scope_systemui") })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_android,
                        tipsId = R.string.scope_android_summary,
                        onClickListener = { showFragment("scope_android") })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_packageinstaller,
                        onClickListener = { showFragment("scope_packageinstaller") })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_launcher,
                        onClickListener = { showFragment("scope_launcher") })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemSettings,
                        onClickListener = { showFragment("scope_systemSettings") })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_other,
                        tipsId = R.string.scope_other_summary,
                        onClickListener = { showFragment("scope_other") })
                )
                Line()
                TitleText(textId = R.string.about)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.about_module,
                        tips = getString(R.string.about_module_summary),
                        onClickListener = { showFragment("about_module") })
                )

            }
            register("scope_systemui", getString(R.string.scope_systemui), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.statusbar)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep
                    ), SwitchV("status_bar_double_tap_to_sleep")
                )
                Line()
                TitleText(textId = R.string.status_bar_clock_format)
                val customClockBinding = GetDataBinding(
                    defValue = {
                        getSP()!!.getBoolean("custom_clock_switch", false)
                    }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextWithSwitch(
                    TextV(textId = R.string.custom_clock_switch, colorId = R.color.purple_700),
                    SwitchV(
                        "custom_clock_switch", dataBindingSend = customClockBinding.bindingSend
                    )
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_year),
                    SwitchV("status_bar_time_year"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_month),
                    SwitchV("status_bar_time_month"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_day),
                    SwitchV("status_bar_time_day"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_week),
                    SwitchV("status_bar_time_week"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_double_hour),
                    SwitchV("status_bar_time_double_hour"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_period),
                    SwitchV("status_bar_time_period", true),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_seconds),
                    SwitchV("status_bar_time_seconds"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_hide_space),
                    SwitchV("status_bar_time_hide_space"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_double_line),
                    SwitchV("status_bar_time_double_line"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.status_bar_time_double_line_center_align),
                    SwitchV("status_bar_time_double_line_center_align"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.status_bar_clock_size,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_clock_size",
                    0,
                    18,
                    0,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.status_bar_clock_double_line_size,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_clock_double_line_size",
                    0,
                    9,
                    0,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(textId = R.string.dropdown_status_bar_clock_display_seconds),
                    SwitchV("dropdown_status_bar_clock_display_seconds")
                )
                TextWithSwitch(
                    TextV(textId = R.string.remove_dropdown_status_bar_clock_redone),
                    SwitchV("remove_dropdown_status_bar_clock_redone")
                )
                Line()
                TitleText(textId = R.string.status_bar_icon)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_prompt_view,
                        tipsId = R.string.hide_prompt_view_summary
                    ), SwitchV("hide_prompt_view")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_battery_percentage_icon,
                        tipsId = R.string.hide_battery_percentage_icon_summary
                    ), SwitchV("hide_battery_percentage_icon")
                )
                TextWithSwitch(
                    TextV(textId = R.string.hide_wifi_activity_icon),
                    SwitchV("hide_wifi_activity_icon")
                )
                TextWithSwitch(
                    TextV(textId = R.string.hide_mobile_activity_icon),
                    SwitchV("hide_mobile_activity_icon")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_vpn_icon, tipsId = R.string.hide_vpn_icon_tips
                    ), SwitchV("hide_vpn_icon")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_bluetooth_icon_acts_global,
                        tipsId = R.string.status_bar_bluetooth_icon_acts_global_tips
                    ), SwitchV("status_bar_bluetooth_icon_acts_global")
                )
                Line()
                TitleText(textId = R.string.status_bar_network_speed)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_network_speed_refresh_speed,
                        tipsId = R.string.status_bar_network_speed_refresh_speed_summary
                    ), SwitchV("status_bar_network_speed_refresh_speed")
                )
                val statusBarDualRowNetworkSpeedBinding = GetDataBinding(
                    defValue = { getSP()!!.getBoolean("status_bar_dual_row_network_speed", false) }

                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_dual_row_network_speed,
                        tipsId = R.string.status_bar_dual_row_network_speed_summary
                    ), SwitchV(
                        "status_bar_dual_row_network_speed",
                        dataBindingSend = statusBarDualRowNetworkSpeedBinding.bindingSend
                    )
                )
                Text(
                    textId = R.string.status_bar_network_speed_dual_row_size,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_network_speed_dual_row_size",
                    6,
                    8,
                    6,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.status_bar_network_speed_dual_row_width,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_network_speed_dual_row_width",
                    28,
                    38,
                    35,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                Line()
                TitleText(textId = R.string.notification)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_finished_charging,
                    ), SwitchV("remove_finished_charging")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_usb_debugging,
                    ), SwitchV("remove_usb_debugging")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_dev_mode_is_on,
                    ), SwitchV("remove_dev_mode_is_on")
                )
                Line()
                TitleText(textId = R.string.features)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.enable_charging_ripple,
                    ), SwitchV("enable_charging_ripple")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.gesture_use_default_digital_assist,
                        tipsId = R.string.gesture_use_default_digital_assist_tips
                    ), SwitchV("gesture_use_default_digital_assist")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.no_vibrate_volKey_Long_press,
                        tipsId = R.string.no_vibrate_volKey_Long_press_tips
                    ), SwitchV("no_vibrate_volKey_Long_press")
                )
                Line()
                TitleText(textId = R.string.lockscreen)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_red_one,
                    ), SwitchV("remove_red_one")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lockscreen_allow_adjust_volume,
                        tipsId = R.string.lockscreen_allow_adjust_volume_tips
                    ), SwitchV("lockscreen_allow_adjust_volume")
                )
                Line()
                TitleText(textId = R.string.quick_settings_panel)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_footer_security_warn,
                    ), SwitchV("remove_footer_security_warn")
                )
                val oldQSCustomSwitchBinding = GetDataBinding(
                    defValue = { getSP()!!.getBoolean("qs_custom_switch", false) }

                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.qs_custom_switch,
                        tipsId = R.string.qs_custom_switch_summary,
                        colorId = R.color.purple_700
                    ), SwitchV(
                        "qs_custom_switch", dataBindingSend = oldQSCustomSwitchBinding.bindingSend
                    )
                )
                Text(
                    textId = R.string.qs_custom_rows,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_rows",
                    1,
                    9,
                    4,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.qs_custom_rows_horizontal,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_rows_horizontal",
                    1,
                    4,
                    2,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.qs_custom_columns,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_columns",
                    1,
                    9,
                    4,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    textId = R.string.qs_custom_columns_unexpanded,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_columns_unexpanded",
                    1,
                    10,
                    6,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
            }
            register("scope_android", getString(R.string.scope_android), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.corepacth)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.downgr, tipsId = R.string.downgr_summary
                    ), SwitchV("downgrade")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.authcreak, tipsId = R.string.authcreak_summary
                    ), SwitchV("authcreak")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.digestCreak, tipsId = R.string.digestCreak_summary
                    ), SwitchV("digestCreak")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.UsePreSig, tipsId = R.string.UsePreSig_summary
                    ), SwitchV("UsePreSig")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.enhancedMode, tipsId = R.string.enhancedMode_summary
                    ), SwitchV("enhancedMode")
                )
                Line()
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.disable_flag_secure,
                        tipsId = R.string.disable_flag_secure_summary
                    ), SwitchV("disable_flag_secure")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_oppo_default_app,
                        tipsId = R.string.remove_oppo_default_app_summary
                    ), SwitchV("remove_oppo_default_app")
                )
                Line()
                TitleText(textId = R.string.notification)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_vpn_activated,
                    ), SwitchV("remove_vpn_activated")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.delete_on_post_notification,
                        tipsId = R.string.delete_on_post_notification_summary
                    ), SwitchV("delete_on_post_notification")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.allow_untrusted_touches,
                        tipsId = R.string.take_effect_after_reboot
                    ), SwitchV("allow_untrusted_touches")
                )
                Line()
                TitleText(textId = R.string.sound)
                val mediaVolumeStepsSwitchBinding = GetDataBinding(
                    defValue = {
                        getSP()!!.getBoolean("media_volume_steps_switch", false)
                    }

                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.media_volume_steps_switch,
                        tips = "${getString(R.string.take_effect_after_reboot)}\n${getString(R.string.media_volume_steps_summary)}"
                    ), SwitchV(
                        "media_volume_steps_switch",
                        dataBindingSend = mediaVolumeStepsSwitchBinding.bindingSend
                    )
                )
                SeekBarWithText(
                    "media_volume_steps",
                    15,
                    30,
                    30,
                    dataBindingRecv = mediaVolumeStepsSwitchBinding.binding.getRecv(2)
                )
            }
            register("scope_packageinstaller", getString(R.string.scope_packageinstaller), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.scope_packageinstaller)
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.skip_apk_scan), SwitchV("skip_apk_scan")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_purify_switch,
                        tipsId = R.string.hide_purify_switch_summery
                    ), SwitchV("hide_purify_switch")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.installer_hide_store_hint),
                    SwitchV("installer_hide_store_hint")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.installer_cts_mode,
                        tipsId = R.string.installer_cts_mode_tips
                    ), SwitchV("installer_cts_mode")
                )
            }
            register("scope_launcher", getString(R.string.scope_launcher), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.scope_launcher)
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.unlock_recent_task_locks_quantity),
                    SwitchV("unlock_recent_task_locks_quantity")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.launcher_remove_update_dot,
                        tipsId = R.string.launcher_remove_update_dot_summary
                    ), SwitchV("launcher_remove_update_dot")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.rm_zvoice_uninstalled_popupwindow,
                        tipsId = R.string.rm_zvoice_uninstalled_popupwindow_tips
                    ), SwitchV("launcher_rm_zvoice_uninstall_dialog")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.launcher_Force_Support_ResizeActivity,
                        tipsId = R.string.launcher_Force_Support_ResizeActivity_tips
                    ), SwitchV("launcher_Force_Support_ResizeActivity")
                )

            }
            register("scope_systemSettings", getString(R.string.scope_systemSettings), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.scope_systemSettings)
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.usb_install_switch_skip_verify),
                    SwitchV("usb_install_switch_skip_verify")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.setting_Froce_Display_ZvioceSwitch),
                    SwitchV("setting_Froce_Display_ZvioceSwitch")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.setting_Froce_Display_WakeupGoogleAssistSwitch,
                        tipsId = R.string.setting_Froce_Display_WakeupGoogleAssistSwitch_tips
                    ), SwitchV("setting_Froce_Display_WakeupGoogleAssistSwitch")
                )
                Line()
                TitleText(textId = R.string.Fun_options)
                val customMaxyearBinding = GetDataBinding(
                    defValue = {
                        getSP()!!.getBoolean("setting_Fun_override_maxyear_switch", false)
                    }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.setting_Fun_override_maxyear_switch,
                        tipsId = R.string.setting_Fun_override_maxyear_tips
                    ), SwitchV(
                        "setting_Fun_override_maxyear_switch",
                        dataBindingSend = customMaxyearBinding.bindingSend
                    )
                )
                TextSummaryArrow(
                    dataBindingRecv = customMaxyearBinding.getRecv(2),
                    textSummaryV = TextSummaryV(
                        textId = R.string.setting_Fun_override_maxyear,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.setting_Fun_override_maxyear)
                                setMessage(R.string.setting_Fun_override_maxyear_tips)
                                var maxyear =
                                    getSP()!!.getInt("fun_override_max_year", 2099).toString()
                                setEditText(maxyear, "最大年份数字", editCallBacks = {
                                    maxyear = it
                                })
                                setLButton(R.string.cancel) {
                                    dismiss()
                                }
                                setRButton(R.string.Done) {
                                    var maxyearInt = maxyear.toIntOrNull()
                                    if (maxyearInt == null) {
                                        MIUIDialog(activity) {
                                            setCancelable(false)
                                            setTitle(R.string.format_error)
                                            setMessage(R.string.format_int_error)
                                            setRButton(R.string.Done) {
                                                dismiss()
                                            }
                                        }.show()
                                        dismiss()
                                        return@setRButton
                                    }
                                    getSP()!!.edit().putInt("fun_override_max_year",maxyearInt).apply()
                                    dismiss()
                                }
                            }.show()
                        }),
                )

            }
            register("scope_other", getString(R.string.scope_other), false) {
                getLsposedInactiveTip()?.let { TextSummaryArrow(it) }
                TitleText(textId = R.string.scope_security_center)
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.unlock_self_start_quantity),
                    SwitchV("unlock_self_start_quantity")
                )
                Line()
                TitleText(textId = R.string.scope_alarmclock)
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.remove_clock_widget_redone),
                    SwitchV("remove_clock_widget_redone")
                )

            }
            register("about_module", getString(R.string.about_module), true) {
                Author(
                    authorHead = getDrawable(R.drawable.app_icon)!!,
                    authorName = getString(R.string.app_name),
                    authorTips = BuildConfig.VERSION_NAME + "(" + BuildConfig.BUILD_TYPE + ")",
                    onClickListener = {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("coolmarket://apk/com.lt2333.wooboxforcoloros")
                                )
                            )
                            Toast.makeText(
                                activity, "恳求一个五星好评，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            val uri =
                                Uri.parse("https://github.com/Xposed-Modules-Repo/com.lt2333.wooboxforcoloros/releases")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    })
                Line()
                TitleText(text = getString(R.string.developer))
                Author(
                    authorHead = getDrawable(R.drawable.lt)!!,
                    authorName = "乌堆小透明",
                    authorTips = "LittleTurtle2333",
                    onClickListener = {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, Uri.parse("coolmarket://u/883441")
                                )
                            )
                            Toast.makeText(
                                activity, "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT)
                                .show()
                            val uri = Uri.parse("http://www.coolapk.com/u/883441")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    })
                Line()
                TitleText(text = getString(R.string.thank_list))
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.contributor_list, onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS/graphs/contributors")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.third_party_open_source_statement, onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS#%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BC%80%E6%BA%90%E5%BC%95%E7%94%A8")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
                Line()
                TitleText(text = getString(R.string.discussions))
                TextSummaryArrow(TextSummaryV(textId = R.string.qq_channel, onClickListener = {
                    try {
                        val uri =
                            Uri.parse("https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=29Mu64&from=246610&biz=ka")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                }))
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.tg_channel,
                        tipsId = R.string.tg_channel_summary,
                        onClickListener = {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("tg://resolve?domain=simplicityrom")
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(
                                    activity, "本机未安装Telegram应用", Toast.LENGTH_SHORT
                                ).show()
                                val uri = Uri.parse("https://t.me/simplicityrom")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.issues, tipsId = R.string.issues_url, onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS/issues")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
                Line()
                TitleText(getString(R.string.other))
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.app_coolapk_url,
                        tipsId = R.string.app_coolapk_url_summary,
                        onClickListener = {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("coolmarket://apk/com.lt2333.wooboxforcoloros")
                                    )
                                )
                                Toast.makeText(
                                    activity, "恳求一个五星好评，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.opensource,
                        tipsId = R.string.github_url,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.participate_in_translation,
                        tipsId = R.string.participate_in_translation_summary,
                        onClickListener = {
                            try {
                                val uri = Uri.parse("https://crowdin.com/project/wooboxforcoloros")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            }
            registerMenu(getString(R.string.menu)) {
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf("reboot")
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot_host, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot_scope)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf(
                                    "killall com.android.systemui",
                                    "killall com.android.launcher",
                                    "killall com.coloros.alarmclock",
                                    "killall com.android.packageinstaller",
                                    "killall com.oplus.safecenter",
                                )
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
            }
        }
    }


}