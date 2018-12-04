package me.alvince.arch.livepresenter.sample

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Created by alvince on 2018/12/3
 *
 * @author alvince.zy@gmail.com
 */
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        takeIf { !LeakCanary.isInAnalyzerProcess(this) }
                ?.also { installLeakCanary(it) }
    }
}