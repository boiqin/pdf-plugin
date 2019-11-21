package com.boiqin.pdf.utils

import java.util.*

object Objects {
    fun equal(a: Any?, b: Any?): Boolean {
        return a === b || a != null && a == b
    }

    fun hashCode(vararg objects: Any?): Int {
        return Arrays.hashCode(objects)
    }

    fun <T> checkNotNull(reference: T?): T {
        return if (reference == null) {
            throw NullPointerException()
        } else {
            reference
        }
    }

    fun <T> checkNotNull(reference: T?, errorMessage: Any?): T {
        return reference ?: throw NullPointerException(errorMessage.toString())
    }
}