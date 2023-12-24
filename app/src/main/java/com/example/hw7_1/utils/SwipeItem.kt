package com.example.hw7_1.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.abs
import java.util.LinkedList
import kotlin.math.max

@SuppressLint("ClickableViewAccessibility")
abstract class SwipeItem (
    private val rv: RecyclerView) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {
    private var swiped = -1
    private val buttonsBuffer: MutableMap<Int, List<Button>> = mutableMapOf()
    private val recoverQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            if (contains(element)) return false
            return super.add(element)
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private val touch = View.OnTouchListener { _, _ ->
        if (swiped < 0) return@OnTouchListener false
        buttonsBuffer[swiped]?.forEach { it }
        recoverQueue.add(swiped)
        swiped = -1
        recoverSwipedItem()
        true
    }

    private fun List<Button>.intrinsicWidth(): Float {
        if (isEmpty()) return 0.0f
        return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
    }

    init {
        rv.setOnTouchListener(touch)
    }


    private fun show(
        canvas: Canvas,
        buttons: List<Button>,
        itemView: View,
        dX: Float
    ) {
        var right = itemView.right
        buttons.forEach { button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
            val left = right - width
            button.draw(
                canvas,
                RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )

            right = left.toInt()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var maxDX = dX
        val position = viewHolder.adapterPosition
        val itemView = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                if (!buttonsBuffer.containsKey(position)) {
                    buttonsBuffer[position] = instantiateUnderlayButton(position)
                }

                val buttons = buttonsBuffer[position] ?: return
                if (buttons.isEmpty()) return
                maxDX = max(-buttons.intrinsicWidth(), dX)
                show(c, buttons, itemView, maxDX)
            }
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            maxDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val position = recoverQueue.poll() ?: return
            rv.adapter?.notifyItemChanged(position)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (swiped != position) recoverQueue.add(swiped)
        swiped = position
        recoverSwipedItem()
    }

    abstract fun instantiateUnderlayButton(position: Int): List<Button>
    class Button(
        private val context: Context,
        title: String,
        textSize: Float,
        @DrawableRes private val src: Int,
    ) {
        private var clickableRegion: RectF? = null
        private val textSizeInPixel: Float =
            textSize * context.resources.displayMetrics.density // dp to px
        private val horizontalPadding = 50.0f
        val intrinsicWidth: Float

        init {
            val paint = Paint()
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            intrinsicWidth = titleBounds.width() + 2 * horizontalPadding
        }

        fun draw(canvas: Canvas, rect: RectF) {
            if (src != 0) {
                val bitmap = ContextCompat.getDrawable(context, src)?.let { drawable(it) }
                bitmap?.let {
                    canvas.drawBitmap(
                        it,
                        rect.centerX() - (bitmap.width / 2f),
                        rect.centerY() - (bitmap.height / 2f),
                        null
                    )
                }
            }
            clickableRegion = rect
        }

        private fun drawable(drawable: Drawable): Bitmap {

            val bitmap: Bitmap =
                if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                    Bitmap.createBitmap(
                        1,
                        1,
                        Bitmap.Config.ARGB_8888
                    )
                } else {
                    Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}