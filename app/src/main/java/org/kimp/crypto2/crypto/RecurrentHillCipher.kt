package org.kimp.crypto2.crypto

import org.kimp.crypto2.math.Matrix
import kotlin.math.ceil

class RecurrentHillCipher: HillCipherBase() {
    fun crypt(text: String, keys: List<Matrix>, alphabet: String = Alphabet.ENGLISH): CryptResult {
        if (keys.isEmpty()) return CryptResult("", listOf())

        val keySize = keys[0].numberOfColumns

        val builder = StringBuilder(text.length)
        val skipped = ArrayList<Int>()

        text.chunked(keySize)
            .zip(keys)
            .map { encryptBlock(it.first, it.second, alphabet) }
            .forEachIndexed { index, cryptResult ->
                builder.append(cryptResult.message)
                cryptResult.skipped.forEach {
                    skipped.add(it + keySize * index)
                }
            }

        return CryptResult(builder.toString(), skipped)
    }

    fun buildKeys(size: Int, key1: Matrix, key2: Matrix, alphabet: String = Alphabet.ENGLISH): List<Matrix> {
        val result = mutableListOf(key1, key2)
        for (i in 2 until size) {
            result.add(result[i - 2] * result[i - 1])
        }
        return result.map { it.mapAll { el -> ((el % alphabet.length) + alphabet.length) % alphabet.length } }
    }

    fun encrypt(text: String, key1: Matrix, key2: Matrix, alphabet: String = Alphabet.ENGLISH): CryptResult {
        val numberOfKeys = ceil(text.length.toDouble() / key1.numberOfColumns.toDouble()).toInt()
        return crypt(text, buildKeys(numberOfKeys, key1, key2), alphabet)
    }

    fun decrypt(text: String, key1: Matrix, key2: Matrix, alphabet: String = Alphabet.ENGLISH): CryptResult {
        val numberOfKeys = ceil(text.length.toDouble() / key1.numberOfColumns.toDouble()).toInt()
        return crypt(text, buildKeys(numberOfKeys, key1, key2).map { k -> k.reversibleByMod(alphabet.length.toLong()) }, alphabet)
    }
}