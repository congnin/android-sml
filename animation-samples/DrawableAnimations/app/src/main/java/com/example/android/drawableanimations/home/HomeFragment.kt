package com.example.android.drawableanimations.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.android.drawableanimations.R
import com.example.android.drawableanimations.databinding.HomeFragmentBinding
import com.example.android.drawableanimations.demo.Demo
import com.example.android.drawableanimations.demo.animated.AnimatedFragment
import com.example.android.drawableanimations.demo.seekable.SeekableFragment
import com.example.android.drawableanimations.viewBindings

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBindings(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DemoListAdapter { demo ->
            activity?.let { activity ->
                activity.supportFragmentManager.commit {
                    replace(R.id.main, demo.createFragment())
                    addToBackStack(null)
                }
                activity.title = demo.title
            }
        }
        binding.list.adapter = adapter
        adapter.submitList(listOf(
            Demo("AnimatedVectorDrawableCompat") { AnimatedFragment() },
            Demo("SeekableAnimatedVectorDrawable") { SeekableFragment() }
        ))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.setTitle(R.string.app_name)
    }
}