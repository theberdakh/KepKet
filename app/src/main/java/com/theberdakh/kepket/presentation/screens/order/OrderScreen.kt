package com.theberdakh.kepket.presentation.screens.order

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ScreenOrderBinding
import com.theberdakh.kepket.presentation.adapters.FoodItemControllerAdapter
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.viewbinding.viewBinding

private const val ARG_SELECTED_FOODS = "SELECTED_FOODS"
private const val ARG_SELECTED_TABLE = "SELECTED_TABLE"
class OrderScreen: Fragment(R.layout.screen_order) {
    private val binding: ScreenOrderBinding by viewBinding()
    private val foodItemControllerAdapter by lazy { FoodItemControllerAdapter() }

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
        foodItemControllerAdapter.submitList(selectedFoods)
        binding.orderToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        Log.d("Foods", "$selectedFoods")
        Log.d("Foods", "$selectedTable")

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