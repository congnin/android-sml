package com.example.android.architecture.blueprints.todoapp.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.StatisticsFragBinding
import com.example.android.architecture.blueprints.todoapp.util.setupRefreshLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Main UI for the statistics screen.
 */
class StatisticsFragment : DaggerFragment() {

    private lateinit var viewDataBinding: StatisticsFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val statisticsViewModel by viewModels<StatisticsViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.statistics_frag, container,
            false
        )
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.viewmodel = statisticsViewModel
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        statisticsViewModel.start()
    }
}