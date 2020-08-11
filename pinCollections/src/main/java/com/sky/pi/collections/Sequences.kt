package com.sky.pi.collections


fun <T> Sequence<T>.findAndUpdate(newItem: T, predicate: (T) -> Boolean): Sequence<T> {
    return map { item ->
        if (predicate(item))
            newItem
        else item
    }
}