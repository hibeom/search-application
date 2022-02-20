package com.pinkcloud.searchapplication.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IntegrationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isDefaultQueryEntered() {
        onView(withId(R.id.search_text_input)).check(matches(withText("kakao")))
    }

    @Test
    fun clickThumbnails_isSaveButtonEnabled() {
        clickDocumentItemAtPosition(0)
        clickDocumentItemAtPosition(1)
        clickDocumentItemAtPosition(2)

        onView(withId(R.id.pick_button)).check(matches(isEnabled()))
    }

    @Test
    fun clickSaveButton_isClickedDocumentsSaved() {
        val storageText = getString(R.string.title_storage)

        clickDocumentItemAtPosition(0)
        clickDocumentItemAtPosition(1)

        onView(withId(R.id.pick_button)).perform(click())
        onView(withText(storageText)).perform(click())

        onView(withId(R.id.list)).check(matches(withRecyclerViewSize(2)))
    }

    private fun clickDocumentItemAtPosition(position: Int) {
        onView(withId(R.id.list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<DocumentPagingAdapter.ViewHolder>(
                    position,
                    click()
                )
            )
    }

    private fun getString(id: Int): String {
        return InstrumentationRegistry
            .getInstrumentation()
            .targetContext.resources
            .getString(id)
    }

    private fun withRecyclerViewSize(size: Int): TypeSafeMatcher<View> {
        return object: TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have $size items");
            }

            override fun matchesSafely(item: View?): Boolean {
                val recyclerView = item as? RecyclerView
                return recyclerView?.adapter?.run {
                    itemCount == size
                } ?: false
            }
        }
    }
}