package net.swiftzer.eric.twopanedemo

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by eric on 26/3/2018.
 */
class LinearSpacingItemDecoration(
        private val topEdgePadding: Int = 0,
        private val rightEdgePadding: Int = 0,
        private val bottomEdgePadding: Int = 0,
        private val leftEdgePadding: Int = 0,
        private val horizontalInnerSpacing: Int = 0,
        private val verticalInnerSpacing: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        check(parent.layoutManager is LinearLayoutManager) { "LinearSpacingItemDecoration only supports LinearLayoutManager" }

        val layoutManager = parent.layoutManager as LinearLayoutManager
        val position = if (layoutManager.reverseLayout) {
            state.itemCount - 1 - parent.getChildAdapterPosition(view)
        } else {
            parent.getChildAdapterPosition(view)
        }

        when (layoutManager.orientation) {
            LinearLayoutManager.VERTICAL -> {
                outRect.left = leftEdgePadding
                outRect.right = rightEdgePadding
                outRect.top = if (position == 0) topEdgePadding else verticalInnerSpacing
                outRect.bottom = if (position == state.itemCount - 1) bottomEdgePadding else 0
            }
            LinearLayoutManager.HORIZONTAL -> {
                outRect.left = if (position == 0) leftEdgePadding else horizontalInnerSpacing
                outRect.right = if (position == state.itemCount - 1) rightEdgePadding else 0
                outRect.top = topEdgePadding
                outRect.bottom = bottomEdgePadding
            }
        }
    }
}
