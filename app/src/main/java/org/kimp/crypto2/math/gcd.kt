package org.kimp.crypto2.math

fun gcd(a: Long, b: Long): Long =
    if (a == 0L) b else gcd(b % a, a);

fun gcdExtended(a: Long, b: Long): Triple<Long, Long, Long> {
    if (a == 0L) return Triple(b, 0L, 1L)
    val recursiveResult = gcdExtended(b % a, a)
    return Triple(
        recursiveResult.first,
        recursiveResult.third - b * recursiveResult.second / a,
        recursiveResult.second
    )
}

