package com.theberdakh.kepket.presentation.screens.order

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.theberdakh.extensions.ViewExtensions.gone
import com.theberdakh.extensions.ViewExtensions.visible
import com.theberdakh.kepket.R
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.databinding.ScreenOrderBinding
import com.theberdakh.kepket.presentation.adapters.FoodItemControllerAdapter
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.screens.allfoods.AllFoodScreen
import com.theberdakh.kepket.presentation.screens.allorders.AllOrdersScreen
import com.theberdakh.kepket.presentation.screens.login.LoginViewModel
import com.theberdakh.navigation.NavigationExtensions.addFragment
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
    private var orderStatus = AllFoodScreen.KEY_ORDER_NOT_SEND
    val result = Bundle()

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



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    result.putString(AllFoodScreen.KEY_ORDER, orderStatus)
                    setFragmentResult(AllFoodScreen.REQUEST_KEY, result)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })


        binding.tableNumber.text = getString(R.string.table_number, selectedTable?.tableNumber.toString())
        binding.rvFoods.adapter = foodItemControllerAdapter



        binding.orderToolbar.setNavigationOnClickListener {
            result.putString(AllFoodScreen.KEY_ORDER, orderStatus)
            setFragmentResult(AllFoodScreen.REQUEST_KEY, result)
            requireActivity().supportFragmentManager.popBackStack()
        }

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

        binding.signInLoginBtn.setOnClickListener {
            selectedTable?.let {
                orderScreenViewModel.createOrder(it)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
      orderScreenViewModel.allFoods.onEach {
          foodItemControllerAdapter.submitList(it)
          foodItemControllerAdapter.notifyDataSetChanged()
      }.launchIn(viewLifecycleOwner.lifecycleScope)

        orderScreenViewModel.totalPriceState.onEach {
            binding.summa.text = it.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        orderScreenViewModel.createOrderState.onEach {
            if (it.isLoading){
                binding.sendOrderText.gone()
                binding.sendOrderProgress.visible()
            }
            if (it.result != null){
                when(it.result.status){
                    Status.SUCCESS -> {
                        binding.sendOrderProgress.gone()
                        binding.sendOrderText.visible()
                        Toast.makeText(requireContext(), "Order created!", Toast.LENGTH_SHORT).show()
                        orderStatus = AllFoodScreen.KEY_ORDER_SEND
                        result.putString(AllFoodScreen.KEY_ORDER, orderStatus)
                        setFragmentResult(AllFoodScreen.REQUEST_KEY, result)
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    Status.ERROR -> {
                        binding.sendOrderProgress.gone()
                        binding.sendOrderText.visible()
                        Toast.makeText(requireContext(), it.result.errorThrowable?.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    companion object {

        @JvmStatic
        fun newInstance(foods: ArrayList<FoodItem>, tableItem: TableItem) = OrderScreen().apply {
            selectedFoods = null
            selectedTable = null
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_SELECTED_FOODS, foods)
                putParcelable(ARG_SELECTED_TABLE, tableItem)
            }
        }
    }
}