package com.example.habisin.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.habisin.front.data.model.ProductModel
import java.util.*
import java.util.concurrent.TimeUnit

class AddProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun onItemNameChange(name: String) {
        _uiState.value = _uiState.value.copy(itemName = name)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun onBestBeforeDateChange(date: Date) {
        val daysLeft = calculateDaysLeft(date)
        _uiState.value = _uiState.value.copy(
            bestBeforeDate = date,
            daysLeft = daysLeft
        )
    }

    fun onQuantityChange(quantity: Int) {
        _uiState.value = _uiState.value.copy(quantity = quantity)
    }

    fun addProduct() {
        val state = _uiState.value
        if (state.itemName.isBlank() || state.bestBeforeDate == null) {
            return
        }

        val product = ProductModel(
            id = UUID.randomUUID().toString(),
            name = state.itemName,
            category = state.category,
            bestBeforeDate = state.bestBeforeDate!!,
            daysLeft = state.daysLeft,
            quantity = state.quantity,
            unit = "PCS",
            imageUrl = ""
        )

        // TODO: Save to database or state management
        // For now, just reset the form
        resetForm()
    }

    private fun calculateDaysLeft(date: Date): Int {
        val diff = date.time - System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toDays(diff).toInt()
    }

    private fun resetForm() {
        _uiState.value = AddProductUiState()
    }
}