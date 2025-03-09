package kr.open.rhpark.library.ui.view.recyclerview

public interface OnScrollDirectionChangedListener {
    public fun onScrollDirectionChanged(scrollDirection: ScrollDirection)
}

public interface OnEdgeReachedListener {
    public fun onEdgeReached(edge: ScrollEdge, isReached: Boolean)
}