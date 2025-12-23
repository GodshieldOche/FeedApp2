package com.example.feedapp.data

import com.example.feedapp.model.Post
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PostRepositoryTest {

    private val repository = PostRepository()

    @Test
    fun getPosts_returns50Items() = runTest {
        val posts = repository.getPosts()
        assertEquals("Should return exactly 50 posts", 50, posts.size)
        assertEquals("First post title mismatch", "Post #0", posts[0].title)
        assertEquals("Last post title mismatch", "Post #49", posts[49].title)
    }
}