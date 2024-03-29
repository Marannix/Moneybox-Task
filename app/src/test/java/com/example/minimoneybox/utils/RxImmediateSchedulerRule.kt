package com.example.minimoneybox.utils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Required due to error occurring when unit testing rxjava
 * Caused by: java.lang.RuntimeException: Method getMainLooper in android.os.Looper not mocked.
 * See http://g.co/androidstudio/not-mocked for details.
 * Reference -> https://medium.com/@dbottillo/how-to-unit-test-your-rxjava-code-in-kotlin-d239364687c9
 */
class RxImmediateSchedulerRule : TestRule {
    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}