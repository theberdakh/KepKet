package com.theberdakh.kepket.presentation.screens.allorders

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenAllOrdersBinding
import com.theberdakh.kepket.presentation.adapters.OrderItemAdapter
import com.theberdakh.kepket.presentation.screens.complete.CompleteScreen
import com.theberdakh.kepket.presentation.screens.table.AllTableFragment
import com.theberdakh.navigation.NavigationExtensions.addFragmentToBackStack
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class AllOrdersScreen: Fragment(R.layout.screen_all_orders) {
    private val binding by viewBinding<ScreenAllOrdersBinding>()
    private val allOrdersViewModel by viewModel<AllOrdersViewModel>()
    private val orderItemAdapter by lazy { OrderItemAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOrders.adapter = orderItemAdapter
        binding.swipeRefreshOrders.setOnRefreshListener { actionRefresh() }
        binding.fabAdd.setOnClickListener { requireActivity().supportFragmentManager.addFragmentToBackStack(R.id.main, AllTableFragment.newInstance()) }
        orderItemAdapter.setOnOrderItemClickListener { orderItem ->
            requireActivity().supportFragmentManager.addFragmentToBackStack(R.id.main, CompleteScreen.newInstance(
                ArrayList(orderItem.meals),
                tableNumber = orderItem.tableNumber,
                totalPrice = orderItem.totalPrice,
                orderId = orderItem.id,
                orderStatus = orderItem.status
            ))
        }


    }

    override fun onStart() {
        super.onStart()
        initObservers()

    }

    private fun initObservers() {

        allOrdersViewModel.getWaiterNotifications()
        allOrdersViewModel.waiterNotificationsState.onEach {
                orderResponseNetworkState ->
            if(orderResponseNetworkState.isLoading){
                binding.swipeRefreshOrders.isRefreshing = true
            }

            if (orderResponseNetworkState.result != null){
                when(orderResponseNetworkState.result.status){
                    Status.SUCCESS -> {
                        Log.d("Orders", "${orderResponseNetworkState.result}")
                        orderItemAdapter.submitList(orderResponseNetworkState.result.data)
                        binding.swipeRefreshOrders.isRefreshing = false
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), orderResponseNetworkState.result.errorThrowable?.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.swipeRefreshOrders.isRefreshing = false
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun actionRefresh() {
        allOrdersViewModel.getWaiterNotifications()
        orderItemAdapter.submitList(null)
        binding.swipeRefreshOrders.isRefreshing = false
    }

    companion object {
        fun newInstance(): AllOrdersScreen {
            return AllOrdersScreen()
        }
    }
}
