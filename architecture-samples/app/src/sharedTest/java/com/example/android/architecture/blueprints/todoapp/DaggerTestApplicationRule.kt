package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit rule that creates a [TestApplicationComponent] and injects the [TestTodoApplication].
 *
 * Note that the `testInstrumentationRunner` property needs to point to [CustomTestRunner].
 */
class DaggerTestApplicationRule : TestWatcher() {

    lateinit var component: TestApplicationComponent
        private set

    override fun starting(description: Description?) {
        super.starting(description)

        val app = ApplicationProvider.getApplicationContext<Context>() as TestTodoApplication
        component = DaggerTestApplicationComponent.factory().create(app)
        component.inject(app)
    }
}