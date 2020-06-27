package com.example.android.drawableanimations

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding

/**
 * Retrieves a view binding handle in a Fragment. The field is available only after
 * [Fragment.onViewCreated].
 *
 * ```
 *     private val binding by dataBindings(HomeFragmentBinding::bind)
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         binding.someView.someField = ...
 *     }
 * ```
 */
inline fun <reified BindingT : ViewBinding> Fragment.viewBindings(
    crossinline bind: (View) -> BindingT
) = object : Lazy<BindingT> {

    private var cached: BindingT? = null

    private val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            cached = null
        }
    }

    override val value: BindingT
        get() = cached ?: bind(requireView()).also {
            viewLifecycleOwner.lifecycle.addObserver(observer)
            cached = it
        }

    override fun isInitialized() = cached != null
}