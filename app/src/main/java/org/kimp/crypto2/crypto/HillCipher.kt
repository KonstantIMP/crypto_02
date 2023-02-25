package org.kimp.crypto2.crypto

import org.kimp.crypto2.math.Matrix

class HillCipher: HillCipherBase() {
    fun encrypt(text: String, key: Matrix, alphabet: String = Alphabet.ENGLISH): CryptResult {
        val keySize = key.numberOfColumns

        val builder = StringBuilder(text.length)
        val skipped = ArrayList<Int>()

        text.chunked(keySize)
            .map { encryptBlock(it, key, alphabet) }
            .forEachIndexed { index, cryptResult ->
                builder.append(cryptResult.message)
                cryptResult.skipped.forEach {
                    skipped.add(it + keySize * index)
                }
            }

        return CryptResult(builder.toString(), skipped)
    }
}