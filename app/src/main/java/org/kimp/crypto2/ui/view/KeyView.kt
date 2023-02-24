package org.kimp.crypto2.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.databinding.ViewKeyModuleBinding

@AndroidEntryPoint
class KeyView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private val binding: ViewKeyModuleBinding

    init {
        binding = ViewKeyModuleBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }
}