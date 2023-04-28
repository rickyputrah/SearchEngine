package com.rickyputrah.search.ui.search

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.rickyputrah.search.HiltTestActivity
import com.rickyputrah.search.R
import com.rickyputrah.search.repository.searchRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class SearchScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @After
    fun teardown() {
        searchRepository.reset()
    }

    private fun setContent() {
        composeTestRule.setContent {
            Navigator(screen = SearchScreen())
        }
    }

    @Test
    fun when_open_search_screen_then_show_no_list() = runTest {

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.search_bar_placeholder))
            .assertIsDisplayed()
    }

    @Test
    fun given_search_recommendation_success_when_type_search_then_show_all_recommendation() = runTest {

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.search_bar_placeholder))
            .performTextInput("goo")

        composeTestRule.onAllNodesWithTag(SearchRecommendationItemTestTag)
            .assertCountEquals(3)
        composeTestRule.onNodeWithText("google")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("bing")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("duckduckgo")
            .assertIsDisplayed()
    }

    @Test
    fun given_search_recommendation_success_when_item_clicked_then_navigate_to_detail() = runTest {

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.search_bar_placeholder))
            .performTextInput("goo")

        composeTestRule.onNodeWithText("google")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.search_detail, "google"))
    }

    @Test
    fun given_search_recommendation_failed_when_type_search_then_show_no_recommendations() = runTest {
        searchRepository.result = Result.failure(Throwable("Failed"))

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.search_bar_placeholder))
            .performTextInput("goo")

        composeTestRule.onAllNodesWithTag(SearchRecommendationItemTestTag)
            .assertCountEquals(0)
    }
}