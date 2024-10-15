package com.theberdakh.kepket.presentation.screens.allfoods

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenAllFoodBinding
import com.theberdakh.kepket.presentation.adapters.ChipItemAdapter
import com.theberdakh.kepket.presentation.adapters.FoodItemAdapter
import com.theberdakh.kepket.presentation.models.ChipItem
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.screens.order.OrderScreen
import com.theberdakh.navigation.NavigationExtensions.addFragmentToBackStack
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_SELECTED_TABLE = "SELECTED_TABLE"

class AllFoodScreen : Fragment(R.layout.screen_all_food) {
    private val binding by viewBinding<ScreenAllFoodBinding>()
    private val viewModel by viewModel<AllFoodScreenViewModel>()
    private val chipItemAdapter by lazy { ChipItemAdapter() }
    private val foodItemAdapter by lazy { FoodItemAdapter() }
    private var allFoods = arrayListOf<FoodItem>()
    private var selectedTable: TableItem? = null
    private val selectedFoods = arrayListOf<FoodItem>()
    private val chips = arrayListOf(ChipItem(title = "Ha'mmesi"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedTable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_SELECTED_TABLE, TableItem::class.java)
            } else {
                it.getParcelable(ARG_SELECTED_TABLE)
            }
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(REQUEST_KEY, this){ requestKey, bundle ->
            Log.d("Key", requestKey)
            if (requestKey == REQUEST_KEY) {
                val result = bundle.getString(KEY_ORDER)
                Log.d("Key", "result: $result")
                result?.let {
                    Log.d("Key", "resylt: $it")
                    if (it == KEY_ORDER_SEND){
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }


        }

        parentFragmentManager.addOnBackStackChangedListener {
            if (isVisible) {
                selectedFoods.clear()

            }
        }


        binding.searchFoods.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val filteredFoods = allFoods.filter {
                        it.name.lowercase().startsWith(query.lowercase())
                    }
                    foodItemAdapter.submitList(filteredFoods)
                    foodItemAdapter.notifyDataSetChanged()
                }
                if (query == null){
                    foodItemAdapter.submitList(allFoods)
                }
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredFoods = allFoods.filter {
                        it.name.lowercase().startsWith(newText.lowercase())
                    }
                    foodItemAdapter.submitList(filteredFoods)
                    foodItemAdapter.notifyDataSetChanged()
                }
                if (newText == null){
                    foodItemAdapter.submitList(allFoods)
                }
                return true
            }

        })
        chipItemAdapter.setOnChipItemClickListener { chipItem ->
            val filteredFoods = if(chipItem.title == chips[0].title) {
                allFoods
            } else {
                allFoods.filter {
                    it.category.lowercase().startsWith(chipItem.title.lowercase())
                }
            }
            foodItemAdapter.submitList(filteredFoods)
            foodItemAdapter.notifyDataSetChanged()
        }

        binding.toolbarAllFood.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvChips.adapter = chipItemAdapter
        binding.rvFoods.adapter = foodItemAdapter

        foodItemAdapter.setOnFoodItemClickListener { food ->
            if (food.quantity == 0) {
                selectedFoods.add(food)
                food.quantity +=1
            }
        }

        binding.fabBasket.setOnClickListener {
            selectedTable?.let {
                val orderScreen = OrderScreen.newInstance(foods = selectedFoods, tableItem = selectedTable!!)
                requireActivity().supportFragmentManager.addFragmentToBackStack(R.id.main,
                    orderScreen
                )
            }
        }


    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getAllFoodCategories()
        viewModel.allFoodCategoryFlow.onEach { data ->
            if (data.isLoading) {
                Log.d("Categories", "Loading...")
            }
            if (data.result != null) {
                when (data.result.status) {
                    Status.SUCCESS -> {
                        data.result.data?.let {
                            if (chips.size == 1){
                                chips.addAll(it)
                            }
                            chipItemAdapter.submitList(chips)
                        }

                    }
                    Status.ERROR -> Toast.makeText(
                        requireContext(),
                        data.result.errorThrowable?.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getAllFoods()
        viewModel.allFoodsFlow.onEach { data ->
            if (data.isLoading) {
                Log.d("Categories", "Loading...")
            }
            if (data.result != null) {
                when (data.result.status) {
                    Status.SUCCESS -> {
                        data.result.data?.let {
                            if (allFoods.isEmpty()) {
                                allFoods.addAll(it)
                            }
                        }
                        foodItemAdapter.submitList(data.result.data)
                    }

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

        const val REQUEST_KEY = "requestKey"
        const val KEY_ORDER = "order_key"
        const val KEY_ORDER_SEND = "order_send"
        const val KEY_ORDER_NOT_SEND = "order_not_send"

        @JvmStatic
        fun newInstance(tableItem: TableItem) = AllFoodScreen().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SELECTED_TABLE, tableItem)
            }
        }
    }
}
