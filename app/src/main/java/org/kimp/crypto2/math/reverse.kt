package org.kimp.crypto2.math

infix fun Long.reverseByMod(mod: Long): Long {
    val gcdTriple = gcdExtended(this, mod)
    return (gcdTriple.second % mod + mod) % mod
}
