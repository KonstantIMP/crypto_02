package org.kimp.crypto2.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation.AnimationListener
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.R
import org.kimp.crypto2.crypto.Alphabet
import org.kimp.crypto2.data.adapter.KeyMatrixAdapter
import org.kimp.crypto2.databinding.ViewKeyModuleBinding
import org.kimp.crypto2.math.Matrix
import org.kimp.crypto2.math.gcd
import timber.log.Timber

@AndroidEntryPoint
class KeyView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs), KeyMatrixAdapter.MatrixChangedListener {
    private val binding: ViewKeyModuleBinding

    private val errorHolderAnimationDuration: Long

    private var layoutManager: LayoutManager
    private var adapter: KeyMatrixAdapter

    var hasErrors: Boolean = false

    init {
        binding = ViewKeyModuleBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        errorHolderAnimationDuration = context.resources
            .getInteger(android.R.integer.config_longAnimTime).toLong()

        adapter = KeyMatrixAdapter(Matrix(1, 1, 1L), this)
        layoutManager = GridLayoutManager(context, 1)

        binding.matrixRecyclerView.layoutManager = layoutManager
        binding.matrixRecyclerView.adapter = adapter
    }

    override fun matrixEntered(matrix: Matrix) {
        val determiner = matrix.determiner()
        if (gcd(determiner, Alphabet.ENGLISH.length.toLong()) != 1L) {
            binding.errorTextView.text = context.getString(
                R.string.key_module_invalid_key,
                determiner, Alphabet.ENGLISH.length
            )
            registerError()
        } else {
            clearError()
        }
    }

    override fun elementErased(row: Int, column: Int) {
        binding.errorTextView.text = context.getString(R.string.key_module_erased_element_error, row, column)
        registerError()
    }

    private fun clearError() {
        binding.errorHolder.animate()
            .translationY(-1f)
            .alpha(0f)
            .setDuration(errorHolderAnimationDuration)
            .setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.errorHolder.visibility = GONE
                }
            })
        hasErrors = false
    }

    private fun registerError() {
        binding.errorHolder.apply {
            alpha = 0f
            visibility = VISIBLE

            animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(errorHolderAnimationDuration)
                .setListener(null)
        }
        hasErrors = true
    }
}