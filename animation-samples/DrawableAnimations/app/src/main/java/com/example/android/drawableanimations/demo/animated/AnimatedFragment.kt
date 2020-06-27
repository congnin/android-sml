package com.example.android.drawableanimations.demo.animated

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.android.drawableanimations.R
import com.example.android.drawableanimations.databinding.AnimatedFragmentBinding
import com.example.android.drawableanimations.viewBindings

class AnimatedFragment : Fragment(R.layout.animated_fragment) {

    private val binding by viewBindings(AnimatedFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val icon = AnimatedVectorDrawableCompat.create(
            requireContext(),
            R.drawable.ic_hourglass_animated
        )!!
        icon.registerAnimationCallback(object: Animatable2Compat.AnimationCallback() {
            override fun onAnimationStart(drawable: Drawable?) {
                binding.start.isEnabled = false
                binding.stop.isEnabled = true
            }

            override fun onAnimationEnd(drawable: Drawable?) {
                binding.start.isEnabled = true
                binding.stop.isEnabled = false
            }
        })
        binding.icon.setImageDrawable(icon)
        binding.start.setOnClickListener { icon.start() }
        binding.stop.setOnClickListener { icon.stop() }
    }
}