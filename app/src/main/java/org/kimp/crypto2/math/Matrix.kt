package org.kimp.crypto2.math

class Matrix(
    val numberOfRows: Int,
    val numberOfColumns: Int,
) {
    private val data: Array<LongArray> = Array(numberOfRows) { LongArray(numberOfColumns) { 0L } }

    init {
        require(numberOfColumns >= 0 && numberOfColumns >= 0) {
            "Invalid size for matrix was given (${numberOfRows}, ${numberOfColumns})"
        }
    }

    constructor(data: Array<LongArray>) : this(data.size, if (data.isEmpty()) 0 else data[0].size) {
        data.forEachIndexed { rIndex, row ->
            row.forEachIndexed { cIndex, value ->
                this.data[rIndex][cIndex] = value
            }
        }
    }

    fun setElement(row: Int, column: Int, value: Long) {
        validateCoordinates(row, column)
        data[row][column] = value
    }

    fun getElement(row: Int, column: Int): Long {
        validateCoordinates(row, column)
        return data[row][column]
    }

    fun isSquare(): Boolean = numberOfRows == numberOfColumns

    fun mapAll(func: (Long) -> Long): Matrix {
        data.forEach { row ->
            row.forEachIndexed { index, value ->
                row[index] = func(value)
            }
        }
        return this
    }

    fun getMinor(row: Int, column: Int): Matrix {
        validateCoordinates(row, column)
        return Matrix(
            data.map {
                    it.toMutableList()
                        .apply { removeAt(column) }
                        .toLongArray()
                }
                .toMutableList()
                .apply { removeAt(row) }
                .toTypedArray()
        )
    }

    operator fun times(b: Matrix): Matrix {
        require(numberOfColumns == b.numberOfRows) {
            "Unable multiply ${numberOfColumns}-column matrix to ${b.numberOfRows}-row matrix"
        }
        
        val resultData = Array(numberOfRows) { LongArray(b.numberOfColumns) { 0L } }
        resultData.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ ->
                for (r in 0 until numberOfColumns) {
                    resultData[i][j] += getElement(i, r) * b.getElement(r, j)
                }
            }
        }
        return Matrix(resultData)
    }

    fun determiner(): Long {
        require(isSquare()) { "Unable to calculate determiner for non-square matrix" }

        val matrixSize = numberOfRows

        if (matrixSize == 0) return 0
        if (matrixSize == 1) return getElement(0, 0)
        if (matrixSize == 2) {
            return getElement(0, 0) * getElement(1, 1) - getElement(0, 1) * getElement(1, 0)
        }

        var result: Long = 0
        for (c in 0 until matrixSize) {
            result += getElement(0, c) * getMinor(0, c).determiner() * (if (c % 2 == 0) 1 else -1)
        }
        return result
    }

    override fun toString() =
        data.asSequence()
            .map { it.joinToString (separator = ", ", prefix = "[ ", postfix = " ]") }
            .joinToString(separator = ",\n  ", prefix = "[ ", postfix = " ]")

    private fun validateCoordinates(row: Int, column: Int) {
        require((0 <= row) && (0 <= column) && (row < numberOfRows) && (column < numberOfColumns)) {
            "Unable to access element at (${row}, ${column}) for the matrix with size (${numberOfRows}, ${numberOfColumns})"
        }
    }
}
