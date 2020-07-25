package com.congnin.bottomnavigation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.congnin.bottomnavigation.R
import kotlinx.android.synthetic.main.fragment_afteracademy.like_logo
import kotlinx.android.synthetic.main.fragment_afteracademy.tv_like_count

class AfterAcademyFragment : Fragment() {
    private var count: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_afteracademy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        like_logo.setOnClickListener {
            count++
            tv_like_count.text = "$count"
        }
    }
}