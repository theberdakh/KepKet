package com.theberdakh.kepket.presentation.screens.allfoods

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenAllFoodBinding
import com.theberdakh.kepket.presentation.adapters.ChipItemAdapter
import com.theberdakh.kepket.presentation.adapters.FoodItemAdapter
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.screens.order.OrderScreen
import com.theberdakh.navigation.NavigationExtensions.addFragmentToBackStack
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_SELECTED_TABLE = "SELECTED_TABLE"

class AllFoodScreen: Fragment(R.layout.screen_all_food) {
    private val binding by viewBinding<ScreenAllFoodBinding>()
    private val viewModel by viewModel<AllFoodScreenViewModel>()
    private val chipItemAdapter by lazy { ChipItemAdapter() }
    private val foodItemAdapter by lazy { FoodItemAdapter() }

    private var selectedTable: TableItem? = null
    private val selectedFoods = arrayListOf<FoodItem>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedTable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.getParcelable(ARG_SELECTED_TABLE, TableItem::class.java)
            } else {
                it.getParcelable(ARG_SELECTED_TABLE)
            }

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAllFood.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvChips.adapter = chipItemAdapter
        binding.rvFoods.adapter = foodItemAdapter

        foodItemAdapter.setOnFoodItemClickListener { food ->
            selectedFoods.add(food)
        }

        binding.fabBasket.setOnClickListener {
            Log.d("allfood", "$selectedFoods")
            selectedTable?.let {
                val orderScreen = OrderScreen.newInstance(foods = selectedFoods, tableItem = selectedTable!!)
                requireActivity().supportFragmentManager.addFragmentToBackStack(R.id.main, orderScreen)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getAllFoodCategories("66dde49f4119697e189a1452")
        viewModel.allFoodCategoryFlow.onEach { data ->
            if(data.isLoading) {
                Log.d("Categories", "Loading...")
            }
            if (data.result != null){
                when(data.result.status){
                    Status.SUCCESS -> chipItemAdapter.submitList(data.result.data)
                    Status.ERROR -> Toast.makeText(requireContext(), data.result.errorThrowable?.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getAllFoods("66dde49f4119697e189a1452")
        viewModel.allFoodsFlow.onEach { data ->
            if(data.isLoading) {
                Log.d("Categories", "Loading...")
            }
            if (data.result != null){
                when(data.result.status){
                    Status.SUCCESS -> foodItemAdapter.submitList(data.result.data)
                    Status.ERROR -> {
                        Log.d("AllFoodScreen", "${data.result.errorThrowable?.errorMessage}")
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

        @JvmStatic
        fun newInstance(tableItem: TableItem) = AllFoodScreen().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SELECTED_TABLE, tableItem)

                }
            }
    }
}
