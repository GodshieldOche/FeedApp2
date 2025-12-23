package com.example.feedapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import coil.imageLoader
import coil.request.ImageRequest
import com.example.feedapp.model.Post

class CustomFeedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var posts: List<Post> = emptyList()
    private val imageBitmaps = mutableMapOf<String, Bitmap>()
    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        isAntiAlias = true
    }
    private val backgroundPaint = Paint().apply {
        color = Color.WHITE
    }
    private val separatorPaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }

    private var scrollOffset = 0f
    private val itemHeight = 800f
    private var totalContentHeight = 0f

    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            scrollBy(0, distanceY.toInt())
            return true
        }

        override fun onDown(e: MotionEvent): Boolean = true
    })

    fun setPosts(newPosts: List<Post>) {
        this.posts = newPosts
        this.totalContentHeight = newPosts.size * itemHeight
        posts.forEach { post ->
            if (!imageBitmaps.containsKey(post.id)) {
                loadBitmap(post)
            }
        }
        invalidate()
    }

    private fun loadBitmap(post: Post) {
        val imageLoader = context.imageLoader
        val request = ImageRequest.Builder(context)
            .data(post.imageUrl)
            .target {
                imageBitmaps[post.id] = (it as BitmapDrawable).bitmap
                invalidate()
            }
            .build()
        imageLoader.enqueue(request)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun scrollBy(x: Int, y: Int) {
        val maxScroll = (totalContentHeight - height).coerceAtLeast(0f)
        scrollOffset = (scrollOffset + y).coerceIn(0f, maxScroll)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val firstVisibleIndex = (scrollOffset / itemHeight).toInt()
        val lastVisibleIndex = ((scrollOffset + height) / itemHeight).toInt().coerceAtMost(posts.size - 1)

        canvas.save()
        canvas.translate(0f, -scrollOffset)

        for (i in firstVisibleIndex..lastVisibleIndex) {
            val top = i * itemHeight
            val post = posts[i]

            canvas.drawRect(0f, top, width.toFloat(), top + itemHeight, backgroundPaint)

            // Draw Image
            imageBitmaps[post.id]?.let {
                val imageLeft = (width - it.width) / 2f
                canvas.drawBitmap(it, imageLeft, top + 50f, null)
            }

            // Draw Text
            canvas.drawText(post.title, 50f, top + 700f, paint)

            canvas.drawLine(0f, top + itemHeight, width.toFloat(), top + itemHeight, separatorPaint)
        }

        canvas.restore()
    }
}