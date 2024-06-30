package com.devj.gestantescontrolcompose.features.scheduler.data

import android.content.Context
import android.os.PowerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PowerManagerService @Inject constructor(@ApplicationContext private val context: Context) {
    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    fun isIgnoringBatteryOptimizations(): Boolean {
        val packageName = context.packageName
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

}