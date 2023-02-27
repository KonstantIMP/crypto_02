package org.kimp.crypto2.crypto

import org.kimp.crypto2.math.Matrix

abstract class HillCipherBase {
    fun encryptBlock(block: String, key: Matrix, alphabet: String = Alphabet.ENGLISH): CryptResult {
        val keySize = key.numberOfColumns

        require(key.isSquare()) { "You can use only square keys" }
        require(block.length <= keySize) {
            "Unable to process %d-sized block by %d-sized key".format(
                block.length, keySize
            )
        }

        val builder = StringBuilder(block.length)
        val skipped = HashSet<Int>()

        val blockMatrix = Matrix(keySize, 1)
        block.forEachIndexed { index, c ->
            if (alphabet.contains(c)) blockMatrix.setElement(index, 0, alphabet.indexOf(c).toLong())
            else {
                blockMatrix.setElement(index, 0, 0)
                skipped.add(index)
            }
        }

        if (block.length < keySize) {
            for (i in block.length until keySize) skipped.add(i)
        }

        val resultMatrix = (key * blockMatrix).mapAll { el -> el % alphabet.length }
        for (rIndex in 0 until keySize) {
            if (skipped.contains(rIndex)) builder.append(block[rIndex])
            else builder.append(alphabet[resultMatrix.getElement(rIndex, 0).toInt()])
        }

        return CryptResult(builder.toString(), skipped.toList())
    }

    fun decryptBlock(block: String, key: Matrix, alphabet: String = Alphabet.ENGLISH): CryptResult =
        encryptBlock(block, key.reversibleByMod(alphabet.length.toLong()), alphabet)
}