package com.example.playlistmaker.player.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playButton: Drawable?
    private val pauseButton: Drawable?
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playButton = getDrawable(R.styleable.PlaybackButtonView_playButton)
                pauseButton = getDrawable(R.styleable.PlaybackButtonView_pauseButton)
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val image = if (isPlaying) pauseButton else playButton
        image?.apply {
            setBounds(
                imageRect.left.toInt(),
                imageRect.top.toInt(),
                imageRect.right.toInt(),
                imageRect.bottom.toInt()
            )
            draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setImage(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        invalidate()
    }

}