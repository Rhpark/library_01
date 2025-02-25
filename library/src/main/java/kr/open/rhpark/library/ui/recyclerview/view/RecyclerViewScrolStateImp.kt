package kr.open.rhpark.library.ui.recyclerview.view

public interface OnScrollDirectionChangedListener {
    public fun onScrollDirectionChanged(scrollDirection: ScrollDirection)
}

public interface OnEdgeReachedListener {
    public fun onEdgeReached(edge: ScrollEdge, isReached: Boolean)
}