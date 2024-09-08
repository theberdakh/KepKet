package com.theberdakh.kepket.presentation.screens.allfoods

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
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_TABLE_ID = "TABLE_ID"
private const val ARG_TABLE_NUMBER = "TABLE_NUMBER"

class AllFoodScreen: Fragment(R.layout.screen_all_food) {
    private val binding by viewBinding<ScreenAllFoodBinding>()
    private val chipItemAdapter by lazy { ChipItemAdapter() }
    private val viewModel by viewModel<AllFoodScreenViewModel>()

    private var tableId: String? = null
    private var tableNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            tableId = it.getString(ARG_TABLE_ID)
            tableNumber = it.getInt(ARG_TABLE_NUMBER)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAllFood.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvChips.adapter = chipItemAdapter



    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getAllFoodCategories("66d6ec6db577d62a4df49eb7")
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
    }

    companion object {

        @JvmStatic
        fun newInstance(tableItem: TableItem) = AllFoodScreen().apply {
                arguments = Bundle().apply {
                    putCharSequence(ARG_TABLE_ID, tableItem.id)
                    putInt(ARG_TABLE_NUMBER, tableItem.tableNumber)
                }
            }
    }
}
