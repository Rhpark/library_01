package kr.open.rhpark.library.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.R
import kotlin.math.abs

/**
 * A custom RecyclerView that provides edge reach and scroll direction detection.
 *
 * This view extends RecyclerView and adds functionality to detect when the user
 * has scrolled to the edge of the view and the direction of the scroll.
 */
public open class RecyclerScrollStateView : RecyclerView {

    private var isAtTop = false
    private var isAtBottom = false
    private var isAtLeft = false
    private var isAtRight = false

    private val defaultEdgeReachThreshold = 10
    private var edgeReachThreshold = defaultEdgeReachThreshold // Threshold for edge reach detection
    private val defaultScrollDirectionThreshold = 20
    private var scrollDirectionThreshold = defaultScrollDirectionThreshold // Threshold for scroll direction detection

    private var accumulatedDx = 0
    private var accumulatedDy = 0

    private var onEdgeReachedListener: OnEdgeReachedListener? = null
    private var onScrollDirectionChangedListener: OnScrollDirectionChangedListener? = null

    private var currentScrollDirection = ScrollDirection.IDLE

    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initTypeArray(attrs)
    }
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initTypeArray(attrs)
    }

    private fun initTypeArray(attrs: AttributeSet?) {

        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.RecyclerScrollStateView).apply {
                getString(R.styleable.RecyclerScrollStateView_scrollDirectionThreshold).also {
                    setScrollDirectionThreshold(it?.toInt() ?: defaultScrollDirectionThreshold)
                }

                getString(R.styleable.RecyclerScrollStateView_edgeReachThreshold).also {
                    setEdgeReachThreshold(it?.toInt() ?: defaultEdgeReachThreshold)
                }
                recycle()
            }
        }
    }

    private val scrollListener = object : OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == SCROLL_STATE_IDLE) {
                resetScrollAccumulation()
                setScrollDirection(ScrollDirection.IDLE)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkEdgeReach()
            updateScrollDirection(dx, dy)
        }
    }

    private fun resetScrollAccumulation() {
        accumulatedDx = 0
        accumulatedDy = 0
    }

    private fun setScrollDirection(direction: ScrollDirection) {
        currentScrollDirection = direction
        onScrollDirectionChangedListener?.onScrollDirectionChanged(currentScrollDirection)
    }

    private fun checkEdgeReach() = onEdgeReachedListener?.let {
        when {
            isScrollVertical() -> checkVerticalEdges()
            isScrollHorizontal() -> checkHorizontalEdges()
        }
    }

    /**
     * Top,Bottom Reach Detection
     */
    private fun checkVerticalEdges() {

        val newIsAtTop = computeVerticalScrollOffset() <= edgeReachThreshold
        if (newIsAtTop != isAtTop) {
            isAtTop = newIsAtTop
            onEdgeReachedListener?.onEdgeReached(Edge.TOP, isAtTop)
        }

        val isBottomReached = !canScrollVertically(1) && computeVerticalScrollExtent() + computeVerticalScrollOffset() + edgeReachThreshold >= computeVerticalScrollRange()
        if (isBottomReached != isAtBottom) {
            isAtBottom = isBottomReached
            onEdgeReachedListener?.onEdgeReached(Edge.BOTTOM, isAtBottom)
        }
    }

    /**
     * Left,Right Reach Detection
     */
    private fun checkHorizontalEdges() {

        val newIsAtLeft = computeHorizontalScrollOffset() <= edgeReachThreshold
        if (newIsAtLeft != isAtLeft) {
            isAtLeft = newIsAtLeft
            onEdgeReachedListener?.onEdgeReached(Edge.LEFT, isAtLeft)
        }

        val isRightReached = !canScrollHorizontally(1) && computeHorizontalScrollExtent() + computeHorizontalScrollOffset() + edgeReachThreshold >= computeHorizontalScrollRange()
        if (isRightReached != isAtRight) {
            isAtRight = isRightReached
            onEdgeReachedListener?.onEdgeReached(Edge.RIGHT, isAtRight)
        }
    }

    private fun isScrollVertical() =  layoutManager?.canScrollVertically() ?: false
    private fun isScrollHorizontal() =  layoutManager?.canScrollHorizontally() ?: false

    private fun updateScrollDirection(dx: Int, dy: Int) = onScrollDirectionChangedListener?.let {
        when {
            isScrollVertical() -> updateVerticalScrollDirection(dy)
            isScrollHorizontal() -> updateHorizontalScrollDirection(dx)
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
                setScrollDirection(newVerticalScrollDirection)
            }
            accumulatedDy = 0
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
                setScrollDirection(newHorizontalScrollDirection)
            }
            accumulatedDx = 0
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnScrollListener(scrollListener)
    }

    /**
     * Sets a listener to be notified of scroll direction changes.
     * 스크롤 방향 변경 알림을 받을 리스너를 설정합니다.
     *
     * This method allows you to register a callback that will be invoked
     * whenever the scroll direction of the `RecyclerScrollStateView` changes.
     * The scroll direction is determined based on the accumulated scroll distance
     * and the configured[scrollDirectionThreshold].
     *
     * The listener will receive a [ScrollDirection] value indicating the
     * current scroll direction:
     * - [ScrollDirection.UP, ScrollDirection.DOWN, ScrollDirection.LEFT, ScrollDirection.RIGHT, ScrollDirection.IDLE]
     *
     * @param listener The listener to be notified of scroll direction changes,
     */
    public fun setOnScrollDirectionListener(listener: OnScrollDirectionChangedListener?) {
        this.onScrollDirectionChangedListener = listener
    }

    public fun setOnScrollDirectionListener(scrollDirectionChangedListener: (scrollDirection: ScrollDirection) -> Unit) {
        this.onScrollDirectionChangedListener = object : OnScrollDirectionChangedListener {
            override fun onScrollDirectionChanged(scrollDirection: ScrollDirection) {
                scrollDirectionChangedListener(scrollDirection)
            }
        }
    }

    /**
     * Sets a listener to be notified when the RecyclerView reaches an edge.
     * RecyclerView가 가장자리에 도달했을 때 알림을 받을 리스너를 설정합니다.
     *
     * This method allows you to register a callback that will be invoked
     * whenever the `RecyclerScrollStateView` reaches one of its edges: top, bottom, left, or right.
     * The edge reach detection isdetermined based on the configured [edgeReachThreshold].
     *
     * The listener will receive an [Edge] value indicating which edge was reached,
     * and a boolean value indicating whether the edge is currently reached (`true`) or not (`false`).
     *
     * @param listener The listener to be notified of edge reach events,
     */
    public fun setOnReachEdgeListener(listener: OnEdgeReachedListener?) {
        this.onEdgeReachedListener = listener
    }

    public fun setOnReachEdgeListener(edgeReachedListener: (edge: Edge, isReached: Boolean) -> Unit) {
        this.onEdgeReachedListener = object : OnEdgeReachedListener {
            override fun onEdgeReached(edge: Edge, isReached: Boolean) {
                edgeReachedListener(edge, isReached)
            }
        }
    }

    /**
     * Sets the minimum scroll movement range required to trigger a scroll direction change event.
     * 스크롤 방향 변경 이벤트를 트리거하는 데 필요한 최소 스크롤 이동 범위(px)를 설정합니다.
     *
     * This method allows you to define the minimum distance (in pixels) that the
     * `RecyclerScrollStateView` must be scrolled in a particular direction
     * before a scroll direction change event is triggered.*
     * By default, the minimum scroll movement range is set to 20 pixels.
     * You can adjust this value to control the sensitivity of scroll direction
     * detection. A higher value will make the detection less sensitive, while a
     * lower value will make it more sensitive.
     *
     * @param minimumScrollMovementRange The minimum scroll movement range in pixels.
     * This value should be >= 0
     */
    public fun setScrollDirectionThreshold(minimumScrollMovementRange: Int) {
        if(minimumScrollMovementRange >= 0) {
            this.scrollDirectionThreshold = minimumScrollMovementRange
        }
        else throw IllegalArgumentException("minimumScrollMovementRange must be >= 0, but input value is $minimumScrollMovementRange")
    }

    /**
     * Sets the threshold distance (in pixels) from an edge that is considered as reaching the edge.
     * 가장자리에 도달한 것으로 가장자리로부터의 임계 거리(픽셀 단위)를 설정.
     *
     * This method allows you to define the distance from an edge (top, bottom, left, or right)
     * within which the `RecyclerScrollStateView` is considered to have reached thatedge.
     * By default, the edge reach threshold is set to 10 pixels.
     *
     * You can adjust this value to control the sensitivity of edge reach detection.
     * A higher value will make the detection less sensitive, while a lower value will
     * make it more sensitive.
     *
     * @param edgeReachThreshold The edge reach threshold distance in pixels.
     * This value should be >= 0
     */
    public fun setEdgeReachThreshold(edgeReachThreshold: Int) {
        if(edgeReachThreshold >= 0) {
            this.edgeReachThreshold = edgeReachThreshold
        }
        else throw IllegalArgumentException("edgeReachThreshold must be >= 0, but input value is $edgeReachThreshold")
    }

    public enum class Edge { TOP, BOTTOM, LEFT, RIGHT }

    public enum class ScrollDirection { UP, DOWN, LEFT, RIGHT, IDLE }

    public interface OnScrollDirectionChangedListener {
        public fun onScrollDirectionChanged(scrollDirection: ScrollDirection)
    }

    public interface OnEdgeReachedListener {
        public fun onEdgeReached(edge: Edge, isReached: Boolean)
    }
}