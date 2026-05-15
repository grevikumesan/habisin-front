package com.example.habisin.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.data.remote.dto.AddFoodRequest
import com.example.habisin.ui.uistate.AddProductScanUiStates
import com.example.habisin.ui.uistate.AddProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit

class AddProductViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val openFoodRepository = container.openFoodRepository

    private val _uiState = MutableStateFlow(AddProductUiState())
    private val _uiStateBarcode = MutableStateFlow<AddProductScanUiStates>(AddProductScanUiStates.Idle)

    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()
    val uiStateBarcode: StateFlow<AddProductScanUiStates> = _uiStateBarcode.asStateFlow()

    // Kept for when image upload is implemented
    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun onItemNameChange(name: String) {
        _uiState.value = _uiState.value.copy(itemName = name, errorMessage = null)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun onBestBeforeDateChange(date: Date) {
        _uiState.value = _uiState.value.copy(
            bestBeforeDate = date,
            daysLeft       = calculateDaysLeft(date),
            errorMessage   = null
        )
    }

    fun onQuantityChange(qty: Int) {
        if (qty >= 1) _uiState.value = _uiState.value.copy(quantity = qty)
    }

    fun addProduct() {
        val state = _uiState.value

        if (state.itemName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter item name")
            return
        }
        if (state.bestBeforeDate == null) {
            _uiState.value = state.copy(errorMessage = "Please pick best before date")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)

            val request = AddFoodRequest(
                foodName        = state.itemName.trim(),
                descriptionFood = "",
                bestBefore      = state.bestBeforeDate,
                quantity        = state.quantity,
                category        = state.category.uppercase()
            )

            container.foodRepository.create(request)
                .onSuccess {
                    _uiState.value = AddProductUiState(isSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = state.copy(
                        isLoading    = false,
                        errorMessage = error.message ?: "Failed to add product"
                    )
                }
        }
    }

    fun fetchProductByBarcode(barcode: String) {
        viewModelScope.launch {
            _uiStateBarcode.value = AddProductScanUiStates.Loading

            openFoodRepository.getProduct(barcode)
                .onSuccess { productName ->
                    Log.d("BarcodeScan", "SUCCESS: Found product '$productName' for barcode: $barcode")
                    _uiStateBarcode.value = AddProductScanUiStates.Success(itemName = productName)
                }
                .onFailure { error ->
                    Log.e("BarcodeScan", "FAILURE: Failed to fetch barcode: $barcode. Error: ${error.message}", error)
                    _uiStateBarcode.value = AddProductScanUiStates.Error(
                        error.message ?: "Failed to fetch product"
                    )
                }
        }
    }

    fun consumeSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    private fun calculateDaysLeft(date: Date): Int {
        val diff = date.time - System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toDays(diff).toInt()
    }
}