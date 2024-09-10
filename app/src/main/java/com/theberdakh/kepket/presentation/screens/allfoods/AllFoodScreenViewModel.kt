package com.theberdakh.kepket.presentation.screens.allfoods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.category.CategoryResponse
import com.theberdakh.kepket.data.remote.models.category.toChipItem
import com.theberdakh.kepket.data.remote.models.food.toFoodItem
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.models.ChipItem
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.state.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AllFoodScreenViewModel(
    private val localPreferences: LocalPreferences,
    private val kepKetRepository: KepKetRepository
) : ViewModel() {

    private val _allFoodCategoryFlow = MutableStateFlow(NetworkState<List<ChipItem>>())
    internal val allFoodCategoryFlow: StateFlow<NetworkState<List<ChipItem>>> =
        _allFoodCategoryFlow.asStateFlow()

    fun getAllFoodCategories(restaurantId: String = localPreferences.getUserInfo().restaurantId) = viewModelScope.launch {
            kepKetRepository.getAllCategories(restaurantId).onStart {
                _allFoodCategoryFlow.value = NetworkState(isLoading = true)
            }.catch { e ->
                _allFoodCategoryFlow.value =
                    NetworkState(isLoading = false, result = ResultModel.error(e))
            }.collect { data ->
                when (data.status) {
                    Status.SUCCESS -> {
                        val chips = data.data?.map { categoryResponse ->
                            categoryResponse.toChipItem()
                        }
                        _allFoodCategoryFlow.value =
                            NetworkState(isLoading = false, result = ResultModel.success(chips))
                    }

                    Status.ERROR -> _allFoodCategoryFlow.value =
                        NetworkState(isLoading = false, result = data.errorThrowable?.let {
                            ResultModel.error(it)
                        })
                }

            }
        }

    private val _allFoodsFlow = MutableStateFlow(NetworkState<List<FoodItem>>())
    internal val allFoodsFlow: StateFlow<NetworkState<List<FoodItem>>> = _allFoodsFlow.asStateFlow()
    fun getAllFoods(restaurantId: String = localPreferences.getUserInfo().restaurantId) = viewModelScope.launch {
        kepKetRepository.getAllFoods(restaurantId).onStart {
            _allFoodsFlow.value = NetworkState(isLoading = true)
        }.catch { e ->
            _allFoodsFlow.value =
                NetworkState(isLoading = false, result = ResultModel.error(e))
        }.collect {data ->
            when (data.status) {
                Status.SUCCESS -> {
                    val foods = data.data?.map { food ->
                        food.toFoodItem()
                    }
                    _allFoodsFlow.value =
                        NetworkState(isLoading = false, result = ResultModel.success(foods))
                }

                Status.ERROR -> _allFoodsFlow.value =
                    NetworkState(isLoading = false, result = data.errorThrowable?.let {
                        ResultModel.error(it)
                    })
            }
        }

    }

}