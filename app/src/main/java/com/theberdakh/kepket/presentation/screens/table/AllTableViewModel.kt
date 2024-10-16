package com.theberdakh.kepket.presentation.screens.table

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.data.remote.models.table.toTableItem
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.models.state.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AllTableViewModel(
    private val kepKetRepository: KepKetRepository,
    private val localPreferences: LocalPreferences
) : ViewModel() {

    private val _allTablesFlow = MutableStateFlow(NetworkState<List<TableItem>>())
    internal val allTablesFlow: StateFlow<NetworkState<List<TableItem>>> =
        _allTablesFlow.asStateFlow()

    fun getAllTables(restaurantId: String = localPreferences.getUserInfo().restaurantId) =
        viewModelScope.launch {
            kepKetRepository.getAllTables(restaurantId)
                .onStart { _allTablesFlow.value = NetworkState(isLoading = true) }
                .catch { e ->
                    _allTablesFlow.value =
                        NetworkState(isLoading = false, result = ResultModel.error(e))
                }
                .collect { data ->
                    when (data.status) {
                        Status.SUCCESS -> {
                            data.data?.let {  tableResponse ->
                                val tables = tableResponse.tables.map {
                                    it.toTableItem()
                                }
                                Log.d("Tables", tableResponse.toString())
                                Log.d("Tables", tables.toString())
                                _allTablesFlow.value = NetworkState(isLoading = false, result = ResultModel.success(tables))
                            }

                       /*     _allTablesFlow.value = NetworkState(
                                isLoading = false,
                                result = ResultModel.success(ts)
                            )*/

                        }

                        Status.ERROR -> {
                            _allTablesFlow.value = NetworkState(
                                isLoading = false,
                                result = data.errorThrowable?.let { ResultModel.error(it) })
                        }
                    }
                }
        }

}