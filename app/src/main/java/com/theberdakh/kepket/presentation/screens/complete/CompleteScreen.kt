package com.theberdakh.kepket.presentation.screens.complete

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.extensions.ViewExtensions.gone
import com.theberdakh.extensions.ViewExtensions.visible
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenCompleteBinding
import com.theberdakh.kepket.presentation.adapters.MealItemAdapter
import com.theberdakh.kepket.presentation.models.MealItem
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_FOODS = "foods"
private const val ARG_TABLE_NUMBER = "table_number"
private const val ARG_TOTAL_PRICE = "total_price"
private const val ARG_ORDER_ID = "order_id"
private const val ARG_ORDER_STATUS = "order_status"
class CompleteScreen: Fragment(R.layout.screen_complete) {
    private val binding by viewBinding<ScreenCompleteBinding>()
    private val ordersAdapter by lazy { MealItemAdapter() }
    private var foods: ArrayList<MealItem>? = null
    private var tableNumber: Int = 0
    private var totalPrice: Int = 0
    private var orderId: String = ""
    private var orderStatus: String = ""
    private val completeScreenViewModel by viewModel<CompleteScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            orderId = it.getCharSequence(ARG_ORDER_ID).toString()
            orderStatus = it.getCharSequence(ARG_ORDER_STATUS).toString()

            tableNumber = it.getInt(ARG_TABLE_NUMBER)
            totalPrice = it.getInt(ARG_TOTAL_PRICE)


            foods = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_FOODS, MealItem::class.java)
            } else {
                it.getParcelableArrayList(ARG_FOODS)
            }

            Log.d("Foods", foods.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFoods.adapter = ordersAdapter
        ordersAdapter.submitList(foods)
        binding.tableNumber.text = tableNumber.toString()

        binding.orderToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        if (orderStatus == "Pending") {
            binding.completeOrderButton.visible()
        } else {
            binding.completeOrderButton.gone()
        }

        binding.completeOrderButton.setOnClickListener {
            completeScreenViewModel.completeOrder(orderId)
        }

        initObservers()

    }

    private fun initObservers() {
        completeScreenViewModel.completeOrderState.onEach {
                orderResponseNetworkState ->
            if(orderResponseNetworkState.isLoading){
                binding.completeOrderProgress.visible()
                binding.completeOrderText.gone()
            }

            if (orderResponseNetworkState.result != null){
                when(orderResponseNetworkState.result.status){
                    Status.SUCCESS -> {
                        Log.d("Orders", "${orderResponseNetworkState.result}")
                        binding.completeOrderButton.gone()
                        binding.completeOrderProgress.gone()
                        binding.completeOrderText.visible()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), orderResponseNetworkState.result.errorThrowable?.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.completeOrderProgress.gone()
                        binding.completeOrderText.visible()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {

        @JvmStatic
        fun newInstance(foods: ArrayList<MealItem>, tableNumber: Int, totalPrice: Int, orderId: String, orderStatus: String)  = CompleteScreen().apply {
            arguments = Bundle().apply {
                putInt(ARG_TABLE_NUMBER, tableNumber)
                putInt(ARG_TOTAL_PRICE, totalPrice)
                putCharSequence(ARG_ORDER_ID, orderId)
                putParcelableArrayList(ARG_FOODS, foods)
                putCharSequence(ARG_ORDER_STATUS, orderStatus)
            }
        }
    }
}