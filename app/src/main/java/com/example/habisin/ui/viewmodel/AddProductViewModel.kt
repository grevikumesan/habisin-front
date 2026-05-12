package com.example.habisin.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.ui.uistate.AddProductScanUiStates
import com.example.habisin.ui.uistate.AddProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.Locale

// 👇 Import wajib untuk merakit Multipart (Gambar) dan RequestBody (Teks)
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.habisin.util.uriToFile

class AddProductViewModel(app: Application) : AndroidViewModel(app) {

    private val container = AppContainer(app)
    private val openFoodRepository = container.openFoodRepository

    private val _uiState = MutableStateFlow(AddProductUiState())
    private val _uiStateBarcode = MutableStateFlow<AddProductScanUiStates>(AddProductScanUiStates.Idle)

    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()
    val uiStateBarcode: StateFlow<AddProductScanUiStates> = _uiStateBarcode.asStateFlow()

    // 👇 Fungsi untuk menyimpan gambar saat user memilih dari galeri
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
        val daysLeft = calculateDaysLeft(date)
        _uiState.value = _uiState.value.copy(
            bestBeforeDate = date,
            daysLeft       = daysLeft,
            errorMessage   = null
        )
    }

    fun onQuantityChange(qty: Int) {
        if (qty >= 0) {
            _uiState.value = _uiState.value.copy(quantity = qty)
        }
    }

    // 👇 Proses upload dirombak! AddFoodRequest dihapus dan diganti pembungkus Multipart
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

            try {
                // 1. Dapatkan Context untuk akses file gambar
                val context = getApplication<Application>()

                // 2. Bungkus Gambar jadi MultipartBody.Part (Kalau ada gambarnya)
                val imageFile = state.imageUri?.let { uriToFile(context, it) }
                val imagePart = imageFile?.let { file ->
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                // 3. Bungkus Teks biasa menjadi RequestBody
                val nameBody = state.itemName.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                val descBody = "".toRequestBody("text/plain".toMediaTypeOrNull()) // Dikosongkan sesuai desain backend
                val catBody = state.category.uppercase().toRequestBody("text/plain".toMediaTypeOrNull())
                val qtyBody = state.quantity.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // 4. Format Tanggal (Menjadi bentuk "2026-10-28") lalu dibungkus
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateStr = sdf.format(state.bestBeforeDate)
                val bestBeforeBody = dateStr.toRequestBody("text/plain".toMediaTypeOrNull())

                // 5. Tembak langsung ke Repository yang udah nungguin pecahan parameter ini!
                container.foodRepository.create(
                    foodName = nameBody,
                    descriptionFood = descBody,
                    category = catBody,
                    bestBefore = bestBeforeBody,
                    quantity = qtyBody,
                    image = imagePart
                ).onSuccess {
                    _uiState.value = AddProductUiState(isSuccess = true)
                }.onFailure { error ->
                    _uiState.value = state.copy(
                        isLoading    = false,
                        errorMessage = error.message ?: "Failed to add product"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan sistem"
                )
            }
        }
    }

    // Fitur Scan Barcode tidak diubah
    fun fetchProductByBarcode(barcode: String) {
        viewModelScope.launch {
            _uiStateBarcode.value = AddProductScanUiStates.Loading

            openFoodRepository.getProduct(barcode)
                .onSuccess { result ->
                    val productName = result

                    _uiStateBarcode.value =
                        AddProductScanUiStates.Success(
                            itemName = productName
                        )
                }
                .onFailure { error ->
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