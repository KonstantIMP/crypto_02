package org.kimp.crypto2.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.databinding.ViewMatrixCellBinding

@AndroidEntryPoint
class MatrixCellView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private val binding: ViewMatrixCellBinding

    init {
        binding = ViewMatrixCellBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }
}