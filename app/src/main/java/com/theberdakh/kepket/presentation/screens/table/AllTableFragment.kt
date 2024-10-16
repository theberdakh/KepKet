package com.theberdakh.kepket.presentation.screens.table

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenAllTableBinding
import com.theberdakh.kepket.presentation.adapters.TableItemAdapter
import com.theberdakh.kepket.presentation.screens.allfoods.AllFoodScreen
import com.theberdakh.navigation.NavigationExtensions.replaceFragment
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTableFragment: Fragment(R.layout.screen_all_table) {
    private val binding: ScreenAllTableBinding by viewBinding()
    private val viewModel by viewModel<AllTableViewModel>()
    private val tableAdapter by lazy { TableItemAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTables.adapter = tableAdapter
        tableAdapter.setOnTableItemClickListener {
            if (it.isBusy){
                Toast.makeText(requireContext(), "This table is busy.", Toast.LENGTH_SHORT).show()
            } else {
                requireActivity().supportFragmentManager.replaceFragment(R.id.main, AllFoodScreen.newInstance(it))
            }

            Log.d("Table", it.toString())
        }

        binding.swipeRefreshOrders.setOnRefreshListener {
            viewModel.getAllTables()
        }

    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getAllTables()
        viewModel.allTablesFlow.onEach { data ->
            binding.swipeRefreshOrders.isRefreshing = data.isLoading
            if(data.isLoading) {
                Log.d("Categories", "Loading...")
            }
            if (data.result != null){
                when(data.result.status){
                    Status.SUCCESS -> tableAdapter.submitList(data.result.data)
                    Status.ERROR -> {
                        Log.d("Tables", "${data.result.errorThrowable?.errorMessage}")
                        Toast.makeText(
                            requireContext(),
                            data.result.errorThrowable?.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        fun newInstance(): AllTableFragment{
            return AllTableFragment()
        }
    }
}