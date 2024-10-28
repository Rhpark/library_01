package kr.open.rhpark.library.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.debug.logcat.Logx
import kotlin.math.abs

public class RecyclerScrollStateView : RecyclerView {

    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var isAtTop = false
    private var isAtBottom = false
    private var isAtLeft = false
    private var isAtRight = false

    private var edgeReachThreshold = 10 // Threshold for edge reach detection
    private var scrollDirectionThreshold = 20 // Threshold for scroll direction detection

    private var accumulatedDx = 0
    private var accumulatedDy = 0

    private var onReachEdgeListener: OnReachEdgeListener? = null
    private var onScrollDirectionListener: OnScrollDirectionListener? = null

    private var currentScrollDirection = ScrollDirection.IDLE

    private val scrollListener = object : OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == SCROLL_STATE_IDLE) {
                Logx.d("newState $newState Reset accumulatedDy Dx ")
                accumulatedDy = 0
                accumulatedDx = 0
                currentScrollDirection = ScrollDirection.IDLE
                onScrollDirectionListener?.onScrollDirectionChanged(currentScrollDirection)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            updateEdgeReach()
            updateScrollDirection(dx, dy)
        }
    }

    private fun updateEdgeReach() {
        when{
            isScrollVertical()-> updateTopBottomEdges()
            isScrollHorizontal() ->updateLeftRightEdges()
        }
    }

    private fun updateTopBottomEdges() {

        // Top,Bottom Reach Detection
        val newIsAtTop = computeVerticalScrollOffset() <= edgeReachThreshold
        if (newIsAtTop != isAtTop) {
            isAtTop = newIsAtTop
            onReachEdgeListener?.onReachEdge(Edge.TOP, isAtTop)
        }

        val isBottomReached = !canScrollVertically(1) && computeVerticalScrollExtent() + computeVerticalScrollOffset() + edgeReachThreshold >= computeVerticalScrollRange()
        if (isBottomReached != isAtBottom) {
            isAtBottom = isBottomReached
            onReachEdgeListener?.onReachEdge(Edge.BOTTOM, isAtBottom)
        }
    }

    private fun updateLeftRightEdges() {
        // Left,Right Reach Detection
        val newIsAtLeft = computeHorizontalScrollOffset() <= edgeReachThreshold
        if (newIsAtLeft != isAtLeft) {
            isAtLeft = newIsAtLeft
            onReachEdgeListener?.onReachEdge(Edge.LEFT, isAtLeft)
        }

        val isRightReached = !canScrollHorizontally(1) && computeHorizontalScrollExtent() + computeHorizontalScrollOffset() + edgeReachThreshold >= computeHorizontalScrollRange()
        if (isRightReached != isAtRight) {
            isAtRight = isRightReached
            onReachEdgeListener?.onReachEdge(Edge.RIGHT, isAtRight)
        }
    }

    private fun isScrollVertical() =  layoutManager?.canScrollVertically() ?: false
    private fun isScrollHorizontal() =  layoutManager?.canScrollHorizontally() ?: false

    private fun updateScrollDirection(dx: Int, dy: Int) {
        when{
            isScrollVertical()-> updateVerticalScrollDirection(dy)
            isScrollHorizontal()-> updateHorizontalScrollDirection(dx)
        }
    }

    private fun updateVerticalScrollDirection(dy: Int) {
        accumulatedDy += dy
        if (abs(accumulatedDy) >= scrollDirectionThreshold) {
            val newVerticalScrollDirection = when {
                accumulatedDy > 0 -> ScrollDirection.DOWN
                else -> ScrollDirection.UP
            }

            if (newVerticalScrollDirection != currentScrollDirection) {
                currentScrollDirection = newVerticalScrollDirection
                onScrollDirectionListener?.onScrollDirectionChanged(currentScrollDirection)
                accumulatedDy = 0
            }
        }
    }

    private fun updateHorizontalScrollDirection(dx: Int) {
        accumulatedDx += dx
        if (abs(accumulatedDx) >= scrollDirectionThreshold) {
            val newHorizontalScrollDirection = when {
                accumulatedDx > 0 -> ScrollDirection.RIGHT
                else -> ScrollDirection.LEFT
            }
            if (newHorizontalScrollDirection != currentScrollDirection) {
                currentScrollDirection = newHorizontalScrollDirection
                onScrollDirectionListener?.onScrollDirectionChanged(currentScrollDirection)
                accumulatedDx = 0
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Logx.d()
        addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Logx.d()
        removeOnScrollListener(scrollListener)
    }

    public fun setOnScrollDirectionListener(listener: OnScrollDirectionListener?) {
        this.onScrollDirectionListener = listener
    }

    public fun setOnReachEdgeListener(listener: OnReachEdgeListener?) {
        this.onReachEdgeListener = listener
    }

    /**
     * Check if the RecyclerView is currently scrolling. Minimum scroll movement range
     * default value is 50 (px)
     */
    public fun setMinimumScrollDirectionMovementRange(minimumScrollMovementRange: Int) {
        this.scrollDirectionThreshold = minimumScrollMovementRange
    }

    /**
     * Check if the RecyclerView is currently scrolling in edge. Edge reach threshold
     * default value is 25(px)
     */
    public fun setEdgeReachThreshold(edgeReachThreshold: Int) {
        this.edgeReachThreshold = edgeReachThreshold
    }

    public enum class Edge { TOP, BOTTOM, LEFT, RIGHT }

    public enum class ScrollDirection { UP, DOWN, LEFT, RIGHT, IDLE }

    public interface OnScrollDirectionListener {
        public fun onScrollDirectionChanged(scrollDirection: ScrollDirection)
    }

    public interface OnReachEdgeListener {
        public fun onReachEdge(edge: Edge, isReached: Boolean)
    }
}