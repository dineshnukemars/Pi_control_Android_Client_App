package com.sky.pi.client.libs.utils


fun <T> Sequence<T>.findAndUpdate(newItem: T, predicate: (T) -> Boolean): Sequence<T> {
    return map { item ->
        if (predicate(item))
            newItem
        else item
    }
}