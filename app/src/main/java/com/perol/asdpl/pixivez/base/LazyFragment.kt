package com.perol.asdpl.pixivez.base

import androidx.fragment.app.Fragment

abstract class LazyFragment : Fragment() {
    var isLoaded = false
    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            isLoaded = true
            loadData()
        }
    }

    protected abstract fun loadData()
}
