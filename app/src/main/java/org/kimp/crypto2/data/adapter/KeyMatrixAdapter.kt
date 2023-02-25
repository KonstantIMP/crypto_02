package org.kimp.crypto2.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import org.kimp.crypto2.databinding.ViewMatrixCellBinding
import org.kimp.crypto2.math.Matrix

class KeyMatrixAdapter(
    val matrix: Matrix,
    val matrixChangedListener: MatrixChangedListener
): RecyclerView.Adapter<KeyMatrixAdapter.KeyMatrixCellViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyMatrixCellViewHolder {
        return KeyMatrixCellViewHolder(
            ViewMatrixCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = matrix.numberOfColumns * matrix.numberOfRows

    override fun onBindViewHolder(holder: KeyMatrixCellViewHolder, position: Int) {
        val row = position / matrix.numberOfColumns
        val col = position % matrix.numberOfColumns

        holder.binding.inputEditText.setText("${matrix.getElement(row, col)}")
        holder.binding.inputLayout.hint = "${row + 1}:${col + 1}"

        Pair(row, col).let { coordinates ->
            holder.binding.inputEditText.setOnKeyListener { v, _, _ ->
               val editText = v as TextInputEditText
                editText.text.toString().also {
                    if (it.isNotEmpty()) {
                        matrix.setElement(
                            coordinates.first, coordinates.second, it.toLong()
                        )
                        matrixChangedListener.matrixEntered(matrix)
                    } else {
                        matrixChangedListener.elementErased(
                            coordinates.first + 1, coordinates.second + 1
                        )
                    }
                }
               false
            }
        }
    }

    inner class KeyMatrixCellViewHolder(
        val binding: ViewMatrixCellBinding
    ): RecyclerView.ViewHolder(binding.root) {}

    interface MatrixChangedListener {
        fun matrixEntered(matrix: Matrix)
        fun elementErased(row: Int, column: Int)
    }
}
