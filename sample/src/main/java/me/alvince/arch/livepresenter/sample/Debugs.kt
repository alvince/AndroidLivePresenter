/*
 * Created by alvince on 2018/12/4
 *
 * @author alvince.zy@gmail.com
 */

package me.alvince.arch.livepresenter.sample

import android.app.Application
import com.squareup.leakcanary.AndroidHeapDumper
import com.squareup.leakcanary.HeapDumper
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.squareup.leakcanary.internal.LeakCanaryInternals
import java.io.File
import java.util.concurrent.TimeUnit

internal lateinit var leaksWatcher: RefWatcher

internal class TogglableHeapDumper(private val defaultDumper: HeapDumper) : HeapDumper {

    private var enabled = true

    override fun dumpHeap(): File {
        return if (enabled) defaultDumper.dumpHeap() else HeapDumper.RETRY_LATER
    }

    fun toggle() {
        enabled = !enabled
    }
}

fun installLeakCanary(context: Application) {
    leaksWatcher = LeakCanary.refWatcher(context).apply {
        maxStoredHeapDumps(32)
        watchDelay(3, TimeUnit.SECONDS)

        val defaultDumper = AndroidHeapDumper(
                context, LeakCanaryInternals.getLeakDirectoryProvider(context))
        heapDumper(TogglableHeapDumper(defaultDumper))
    }.buildAndInstall()
}