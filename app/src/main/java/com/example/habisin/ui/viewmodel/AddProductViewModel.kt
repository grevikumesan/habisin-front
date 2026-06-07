package com.example.habisin.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.data.remote.dto.AddFoodRequest
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.uistate.AddProductScanUiStates
import com.example.habisin.ui.uistate.AddProductUiState
import com.example.habisin.util.uriToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AddProductViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val openFoodRepository = container.openFoodRepository

    private val _uiState = MutableStateFlow(AddProductUiState())
    private val _uiStateBarcode = MutableStateFlow<AddProductScanUiStates>(AddProductScanUiStates.Idle)

    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()
    val uiStateBarcode: StateFlow<AddProductScanUiStates> = _uiStateBarcode.asStateFlow()

    fun onImageSelected(uri: Uri?) {
        // A locally-picked photo replaces any external (barcode) image.
        _uiState.value = _uiState.value.copy(imageUri = uri, imageUrl = null)
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

            // Local gallery photo → multipart upload; otherwise JSON (carries the
            // barcode imageUrl if present). Previously the local photo was never sent,
            // so it never appeared on the fridge item.
            val result = if (state.imageUri != null) {
                uploadWithImage(state)
            } else {
                container.foodRepository.create(
                    AddFoodRequest(
                        foodName        = state.itemName.trim(),
                        descriptionFood = "",
                        bestBefore      = state.bestBeforeDate,
                        quantity        = state.quantity,
                        category        = state.category.uppercase(),
                        imageUrl        = state.imageUrl
                    )
                )
            }

            result
                .onSuccess { _uiState.value = AddProductUiState(isSuccess = true) }
                .onFailure { error ->
                    _uiState.value = state.copy(
                        isLoading    = false,
                        errorMessage = error.message ?: "Failed to add product"
                    )
                }
        }
    }

    private suspend fun uploadWithImage(state: AddProductUiState): Result<ProductModel> {
        val ctx = getApplication<Application>()
        val file = uriToFile(ctx, state.imageUri!!)
            ?: return Result.failure(Exception("Couldn't read the selected image"))

        fun textPart(value: String) = value.toRequestBody("text/plain".toMediaTypeOrNull())
        // Match the date format the JSON path (Gson) uses, so the BE parses it the same way.
        val dateStr = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            .format(state.bestBeforeDate!!)
        val imagePart = MultipartBody.Part.createFormData(
            "image", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        return container.foodRepository.createWithImage(
            foodName        = textPart(state.itemName.trim()),
            descriptionFood = textPart(""),
            category        = textPart(state.category.uppercase()),
            bestBefore      = textPart(dateStr),
            quantity        = textPart(state.quantity.toString()),
            image           = imagePart
        )
    }

    fun fetchProductByBarcode(barcode: String) {
        viewModelScope.launch {
            _uiStateBarcode.value = AddProductScanUiStates.Loading

            openFoodRepository.getProduct(barcode)
                .onSuccess { product ->
                    Log.d("BarcodeScan", "SUCCESS: '${product.name}' cat=${product.category} img=${product.imageUrl}")
                    // Prefill the form: name + best-effort category + product image.
                    _uiState.value = _uiState.value.copy(
                        itemName     = product.name.ifBlank { _uiState.value.itemName },
                        category     = product.category,
                        imageUrl     = product.imageUrl,
                        imageUri     = null,
                        errorMessage = null
                    )
                    _uiStateBarcode.value = AddProductScanUiStates.Success(itemName = product.name)
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