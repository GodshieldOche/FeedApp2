package com.example.feedapp.ui

import com.example.feedapp.data.PostRepository
import com.example.feedapp.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private lateinit var repository: PostRepository
    private lateinit var viewModel: FeedViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = PostRepository() // Using the real repo since it's just a mock data generator
        viewModel = FeedViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialLoad_updatesPostsState() = runTest(testDispatcher) {
        // Initial state is empty
        assertEquals(0, viewModel.posts.value.size)

        // Advance coroutines to let init { loadPosts() } finish
        advanceUntilIdle()

        // Verify state updated
        assertEquals(50, viewModel.posts.value.size)
    }
}