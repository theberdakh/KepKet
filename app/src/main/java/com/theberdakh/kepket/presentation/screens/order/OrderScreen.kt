package com.theberdakh.kepket.presentation.screens.order

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ScreenOrderBinding
import com.theberdakh.kepket.presentation.adapters.FoodItemControllerAdapter
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.screens.login.LoginViewModel
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_SELECTED_FOODS = "SELECTED_FOODS"
private const val ARG_SELECTED_TABLE = "SELECTED_TABLE"
class OrderScreen: Fragment(R.layout.screen_order) {
    private val binding: ScreenOrderBinding by viewBinding()
    private val foodItemControllerAdapter by lazy { FoodItemControllerAdapter() }
    private val orderScreenViewModel by viewModel<OrderScreenViewModel>()
    private var selectedFoods: ArrayList<FoodItem>? = null
    private var selectedTable: TableItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             selectedFoods =  it.getParcelableArrayList(ARG_SELECTED_FOODS, FoodItem::class.java)
             selectedTable  = it.getParcelable(ARG_SELECTED_TABLE, TableItem::class.java)
         }
         else {
             selectedFoods =  it.getParcelableArrayList(ARG_SELECTED_FOODS)
             selectedTable = it.getParcelable(ARG_SELECTED_TABLE) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tableNumber.text = getString(R.string.table_number, selectedTable?.tableNumber.toString())
        binding.rvFoods.adapter = foodItemControllerAdapter

        binding.orderToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        Log.d("Foods", "$selectedFoods")
        Log.d("Foods", "$selectedTable")

        selectedFoods?.let {
            orderScreenViewModel.addFoods(it)
            orderScreenViewModel.getTotalPrice()
        }


        initObservers()

        foodItemControllerAdapter.setOnIncreaseClickListener { foodItem ->
            orderScreenViewModel.increaseQuantity(foodItem)
        }

        foodItemControllerAdapter.setOnDecreaseClickListener { foodItem ->
            orderScreenViewModel.decreaseQuantity(foodItem)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
      orderScreenViewModel.allFoods.onEach {
          foodItemControllerAdapter.submitList(it)
          foodItemControllerAdapter.notifyDataSetChanged()
          Log.d("allfoodsstate", "${it}")
      }.launchIn(viewLifecycleOwner.lifecycleScope)

        orderScreenViewModel.totalPriceState.onEach {
            binding.summa.text = it.toString()
            Log.d("allfoodsstate", "${it}")
        }.launchIn(viewLifecycleOwner.lifecycleScope)


    }

    companion object {

        @JvmStatic
        fun newInstance(foods: ArrayList<FoodItem>, tableItem: TableItem) = OrderScreen().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_SELECTED_FOODS, foods)
                putParcelable(ARG_SELECTED_TABLE, tableItem)
            }
        }
    }
}