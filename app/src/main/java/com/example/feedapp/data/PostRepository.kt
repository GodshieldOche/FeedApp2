package com.example.feedapp.data

import com.example.feedapp.model.Post
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor() {
    // Simulate a network delay and return 50 items
    suspend fun getPosts(): List<Post> {
        delay(500) // Simulate network
        return List(50) { index ->
            Post(
                id = index.toString(),
                title = "Post #$index",
                imageUrl = "https://picsum.photos/400?random=$index"
            )
        }
    }

    fun getPostById(id: String): Post {
        return Post(id, "Post #$id", "https://picsum.photos/400?random=$id")
    }
}