package com.theberdakh.kepket.presentation.screens.allfoods

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ScreenAllFoodBinding
import com.theberdakh.kepket.presentation.adapters.ChipItemAdapter
import com.theberdakh.viewbinding.viewBinding

class AllFoodScreen: Fragment(R.layout.screen_all_food) {
    private val binding by viewBinding<ScreenAllFoodBinding>()
    private val chipItemAdapter by lazy { ChipItemAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAllFood.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    companion object {
        fun newInstance(): AllFoodScreen {
            return AllFoodScreen()
        }
    }
}
