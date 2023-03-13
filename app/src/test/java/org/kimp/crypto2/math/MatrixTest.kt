package org.kimp.crypto2.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatrixTest {

    private fun multiplyTestSources(): Stream<Arguments> = Stream.of(
        Arguments.of(
            Matrix(arrayOf(longArrayOf(1))),
            Matrix(arrayOf(longArrayOf(1))),
            Matrix(arrayOf(longArrayOf(1)))
        ),
        Arguments.of(
            Matrix(arrayOf(longArrayOf(1, 2), longArrayOf(3, 4))),
            Matrix(arrayOf(longArrayOf(5, 6), longArrayOf(7, 8))),
            Matrix(arrayOf(longArrayOf(19, 22), longArrayOf(43, 50)))
        ),
        Arguments.of(
            Matrix(arrayOf(longArrayOf(8, 6, 5))),
            Matrix(arrayOf(longArrayOf(1, 2, 3), longArrayOf(4, 5, 6), longArrayOf(7, 8, 9))),
            Matrix(arrayOf(longArrayOf(67, 86, 105)))
        ),
        Arguments.of(
            Matrix(arrayOf(longArrayOf(1, 2, 3), longArrayOf(4, 5, 6), longArrayOf(7, 8, 9))),
            Matrix(arrayOf(longArrayOf(5), longArrayOf(6), longArrayOf(7))),
            Matrix(arrayOf(longArrayOf(38), longArrayOf(92), longArrayOf(146)))
        )
    )

    @ParameterizedTest
    @MethodSource("multiplyTestSources")
    fun multiplyTest(a: Matrix, b: Matrix, result: Matrix) {
        assertEquals(a * b, result)
    }


    private fun diagonalGenerationTestSources(): Stream<Arguments> = Stream.of(
        Arguments.of(1, Matrix(arrayOf(longArrayOf(1)))),
        Arguments.of(
            2,
            Matrix(arrayOf(longArrayOf(1, 0), longArrayOf(0, 1)))
        ),
        Arguments.of(
            3,
            Matrix(arrayOf(longArrayOf(1, 0, 0), longArrayOf(0, 1, 0), longArrayOf(0, 0, 1)))
        )
    )

    @ParameterizedTest
    @MethodSource("diagonalGenerationTestSources")
    fun diagonalGenerationTest(size: Int, expected: Matrix) {
        assertEquals(Matrix.diagonal(size), expected)
    }


    private fun reversedByModTestSources(): Stream<Arguments> = Stream.of(
        Arguments.of(7, 26)
    )

    @ParameterizedTest
    @MethodSource("reversedByModTestSources")
    fun reversedByModTest(a: Long, mod: Long) {
        assertEquals(((a reverseByMod mod) * a) % mod, 1)
    }

    private fun reversedMatrixSearchTestSources(): Stream<Arguments> = Stream.of(
        Arguments.of(Matrix(arrayOf(longArrayOf(2, 5), longArrayOf(1, 6))), 26),
        Arguments.of(Matrix(arrayOf(longArrayOf(1, 2), longArrayOf(3, 1))), 26),
        Arguments.of(Matrix(arrayOf(longArrayOf(1, 3), longArrayOf(2, 3))), 26),
        Arguments.of(Matrix(arrayOf(longArrayOf(1, 2, 3), longArrayOf(4, 5, 6), longArrayOf(5, 4, 2))), 26),
    )

    @ParameterizedTest
    @MethodSource("reversedMatrixSearchTestSources")
    fun reversedMatrixSearchTest(matrix: Matrix, mod: Long) {
        val reversedMatrix = matrix.reversibleByMod(mod)
        val multiplyResult = (matrix * reversedMatrix).mapAll { el -> el % mod }
        assertEquals(multiplyResult, Matrix.diagonal(matrix.numberOfColumns))
    }
}