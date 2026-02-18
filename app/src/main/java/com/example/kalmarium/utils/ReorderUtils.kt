package com.example.kalmarium.ui.util

fun <T> reorderList(
    list: List<T>,
    from: Int,
    to: Int
): List<T> {

    if (from == to) return list
    if (from !in list.indices || to !in list.indices) return list

    val mutable = list.toMutableList()
    val item = mutable.removeAt(from)
    mutable.add(to, item)

    return mutable
}
