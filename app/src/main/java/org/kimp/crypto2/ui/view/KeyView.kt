package org.kimp.crypto2.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.R
import org.kimp.crypto2.crypto.Alphabet
import org.kimp.crypto2.data.adapter.KeyMatrixAdapter
import org.kimp.crypto2.databinding.ViewKeyModuleBinding
import org.kimp.crypto2.math.Matrix
import org.kimp.crypto2.math.gcd
import kotlin.math.min

@AndroidEntryPoint
class KeyView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),
    KeyMatrixAdapter.MatrixChangedListener {
    private val binding: ViewKeyModuleBinding

    private val errorHolderAnimationDuration: Long

    private var layoutManager: GridLayoutManager
    private lateinit var adapter: KeyMatrixAdapter

    var sizeChangedListener: KeySizeChangedListener? = null
    var hasErrors: Boolean = false

    init {
        binding = ViewKeyModuleBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        errorHolderAnimationDuration = context.resources
            .getInteger(android.R.integer.config_longAnimTime).toLong()

        layoutManager = GridLayoutManager(context, 1)
        binding.matrixRecyclerView.layoutManager = layoutManager

        migrateMatrixSize(0, 1)

        binding.sizeSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                migrateMatrixSize(adapter.matrix.numberOfColumns, position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun getEnteredMatrix(): Matrix = adapter.matrix

    override fun matrixEntered(matrix: Matrix) {
        val alphabetLength = Alphabet.ENGLISH.length
        val determiner = (matrix.determiner() + alphabetLength) % alphabetLength
        if (gcd(determiner, alphabetLength.toLong()) != 1L) {
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
        binding.errorTextView.text =
            context.getString(R.string.key_module_erased_element_error, row, column)
        registerError()
    }

    fun migrateMatrixSize(previousSize: Int, size: Int, call: Boolean = true) {
        if (size == previousSize) return
        val newMatrix = Matrix(size, size)

        val toCopy = min(size, previousSize)
        for (i in 0 until toCopy) {
            for (j in 0 until toCopy) {
                newMatrix.setElement(
                    i, j, adapter.matrix.getElement(i, j)
                )
            }
        }

        KeyMatrixAdapter(newMatrix, this).also { newAdapter ->
            binding.matrixRecyclerView.adapter = newAdapter
            adapter = newAdapter
        }
        layoutManager.spanCount = size
        matrixEntered(newMatrix)

        if (call) sizeChangedListener?.keySizeChanged(previousSize, size)
    }

    private fun clearError() {
        binding.errorHolder.animate()
            .translationY(-1f)
            .alpha(0f)
            .setDuration(errorHolderAnimationDuration)
            .setListener(object : AnimatorListenerAdapter() {
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

    interface KeySizeChangedListener {
        fun keySizeChanged(from: Int, to: Int)
    }
}