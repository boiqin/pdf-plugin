package com.boiqin.pdf.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * This provides methods to help Activities load their UI.
 */
object ActivityUtils {
    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    @JvmStatic
    fun addFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int
    ) {
        Objects.checkNotNull(
            fragmentManager
        )
        Objects.checkNotNull(fragment)
        val transaction =
            fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int
    ) {
        Objects.checkNotNull(
            fragmentManager
        )
        Objects.checkNotNull(fragment)
        val transaction =
            fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }
}