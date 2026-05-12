package com.example.habisin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.uistate.RecipeDetailUiState
import com.example.habisin.ui.uistate.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(RecipeDetailUiState())
    val detailUiState: StateFlow<RecipeDetailUiState> = _detailUiState.asStateFlow()

    // Simpan semua recipes yang ada
    private var allRecipes: List<RecipeModel> = emptyList()

    // Simpan data ingredients dari kulkas
    private var fridgeIngredients: List<ProductModel> = emptyList()

    /**
     * Load recipes dari API atau local database
     */
    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Simulasi load data - GANTI INI dengan API call atau database query
                val dummyRecipes = listOf(
                    RecipeModel(
                        id = "1",
                        title = "Nasi Goreng Spesial",
                        cookTime = "15m",
                        difficulty = "Easy",
                        imageUrl = "",
                        description = "Nasi goreng spesial dengan telur dan sayuran",
                        ingredients = listOf("nasi", "telur", "bawang merah", "bawang putih", "kecap", "sayuran"),
                        category = "Rice",
                        instructions = listOf(
                            "Tumis bawang merah dan bawang putih hingga harum",
                            "Masukkan nasi dan aduk rata",
                            "Tambahkan telur, orak-arik",
                            "Tuang kecap dan bumbu lainnya",
                            "Sajikan dengan sayuran"
                        ),
                        servings = 2,
                        calories = 350,
                        tags = listOf("Indonesian", "Quick", "Easy"),
                        rating = 4.5f,
                        reviewCount = 128,
                        author = "Chef Indonesia",
                        datePublished = "2024-01-15"
                    ),
                    RecipeModel(
                        id = "2",
                        title = "Ayam Geprek Sambal Bawang",
                        cookTime = "20m",
                        difficulty = "Easy",
                        imageUrl = "",
                        description = "Ayam goreng crispy dengan sambal bawang pedas",
                        ingredients = listOf("ayam", "tepung", "bawang putih", "cabai", "minyak"),
                        category = "Chicken",
                        instructions = listOf(
                            "Goreng ayam dengan tepung crispy",
                            "Ulek bawang putih dan cabai",
                            "Geprek ayam dengan sambal",
                            "Sajikan panas"
                        ),
                        servings = 1,
                        calories = 450,
                        tags = listOf("Spicy", "Indonesian"),
                        rating = 4.7f,
                        reviewCount = 256,
                        author = "Warung Geprek",
                        datePublished = "2024-02-10"
                    ),
                    RecipeModel(
                        id = "3",
                        title = "Telur Dadar Padang",
                        cookTime = "10m",
                        difficulty = "Easy",
                        imageUrl = "",
                        description = "Telur dadar tebal khas Padang dengan daun bawang",
                        ingredients = listOf("telur", "daun bawang", "bawang merah", "cabai", "garam"),
                        category = "Egg",
                        instructions = listOf(
                            "Kocok telur dengan bumbu",
                            "Tambahkan daun bawang iris",
                            "Goreng dalam minyak panas",
                            "Balik hingga matang"
                        ),
                        servings = 2,
                        calories = 200,
                        tags = listOf("Breakfast", "Quick"),
                        rating = 4.3f,
                        reviewCount = 89,
                        author = "Rumah Makan Padang",
                        datePublished = "2024-01-20"
                    ),
                    RecipeModel(
                        id = "4",
                        title = "Es Teler Segar",
                        cookTime = "15m",
                        difficulty = "Medium",
                        imageUrl = "",
                        description = "Minuman segar buah-buahan dengan santan",
                        ingredients = listOf("alpukat", "kelapa muda", "susu", "gula", "es batu"),
                        category = "Dessert",
                        instructions = listOf(
                            "Potong buah-buahan",
                            "Campur dengan santan dan susu",
                            "Tambahkan gula secukupnya",
                            "Sajikan dengan es batu"
                        ),
                        servings = 2,
                        calories = 180,
                        tags = listOf("Drink", "Sweet", "Cold"),
                        rating = 4.6f,
                        reviewCount = 145,
                        author = "Es Teler 77",
                        datePublished = "2024-03-05"
                    ),
                    RecipeModel(
                        id = "5",
                        title = "Rendang Daging Sapi",
                        cookTime = "3 jam",
                        difficulty = "Hard",
                        imageUrl = "",
                        description = "Rendang daging sapi khas Minang yang legit",
                        ingredients = listOf("daging sapi", "santan", "bumbu rendang", "serai", "daun jeruk"),
                        category = "Meat",
                        instructions = listOf(
                            "Tumis bumbu hingga harum",
                            "Masukkan daging dan aduk",
                            "Tuang santan perlahan",
                            "Masak dengan api kecil hingga kering"
                        ),
                        servings = 4,
                        calories = 550,
                        tags = listOf("Indonesian", "Traditional", "Spicy"),
                        rating = 4.9f,
                        reviewCount = 432,
                        author = "Bundo Kanduang",
                        datePublished = "2024-01-01"
                    ),
                    RecipeModel(
                        id = "6",
                        title = "Nasi Uduk Komplit",
                        cookTime = "30m",
                        difficulty = "Medium",
                        imageUrl = "",
                        description = "Nasi uduk dengan lauk pauk lengkap",
                        ingredients = listOf("beras", "santan", "telur", "tempe", "ayam", "bumbu"),
                        category = "Rice",
                        instructions = listOf(
                            "Masak beras dengan santan",
                            "Siapkan lauk pauk",
                            "Goreng tempe dan telur",
                            "Sajikan dengan sambal"
                        ),
                        servings = 3,
                        calories = 480,
                        tags = listOf("Breakfast", "Indonesian", "Complete"),
                        rating = 4.4f,
                        reviewCount = 198,
                        author = "Nasi Uduk Kebon Kacang",
                        datePublished = "2024-02-20"
                    )
                )

                allRecipes = dummyRecipes

                // Setelah load recipes, langsung filter berdasarkan ingredients di kulkas
                filterRecipesByIngredients()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    /**
     * Set recipes dari sumber eksternal (API/Database)
     */
    fun setRecipes(recipes: List<RecipeModel>) {
        allRecipes = recipes
        filterRecipesByIngredients()
    }

    /**
     * Set ingredients dari kulkas
     */
    fun setFridgeIngredients(ingredients: List<ProductModel>) {
        fridgeIngredients = ingredients
        // Auto filter ketika ingredients berubah
        filterRecipesByIngredients()
    }

    /**
     * Handle search query change
     */
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterRecipesByIngredients()
    }

    /**
     * Handle category selection
     */
    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        filterRecipesByIngredients()
    }

    /**
     * Filter recipes berdasarkan:
     * 1. Ingredients yang ada di kulkas
     * 2. Search query
     * 3. Selected category
     */
    private fun filterRecipesByIngredients() {
        var filtered = allRecipes

        // Filter 1: Hanya tampilkan recipe yang bahan-bahannya ada di kulkas
        if (fridgeIngredients.isNotEmpty()) {
            val ingredientNames = fridgeIngredients.map { it.name.lowercase() }

            filtered = filtered.filter { recipe ->
                // Cek apakah ada minimal 1 bahan dari recipe yang ada di kulkas
                val recipeIngredients = recipe.ingredients.map { it.lowercase() }
                recipeIngredients.any { recipeIngredient ->
                    ingredientNames.any { fridgeItem ->
                        fridgeItem.contains(recipeIngredient, ignoreCase = true) ||
                                recipeIngredient.contains(fridgeItem, ignoreCase = true)
                    }
                }
            }
        } else {
            // Jika kulkas kosong, tampilkan semua recipes
            filtered = allRecipes
        }

        // Filter 2: Search query
        val searchQuery = _uiState.value.searchQuery
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true) ||
                        it.ingredients.any { ingredient ->
                            ingredient.contains(searchQuery, ignoreCase = true)
                        }
            }
        }

        // Filter 3: Category
        val category = _uiState.value.selectedCategory
        if (category != "All" && category.isNotBlank()) {
            filtered = filtered.filter {
                it.category.equals(category, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            recipes = filtered,
            isLoading = false,
            error = null
        )
    }

    /**
     * Get recipe by ID untuk detail screen
     */
    fun getRecipeById(id: String) {
        _detailUiState.value = _detailUiState.value.copy(isLoading = true)

        val recipe = allRecipes.find { it.id == id }
        if (recipe != null) {
            // Hitung matched ingredients
            val matchedCount = getMatchedIngredientsCount(recipe.ingredients)

            _detailUiState.value = _detailUiState.value.copy(
                isLoading = false,
                recipe = recipe,
                matchedIngredients = matchedCount,
                error = null
            )
        } else {
            _detailUiState.value = _detailUiState.value.copy(
                isLoading = false,
                error = "Recipe not found",
                matchedIngredients = Pair(0, 0)
            )
        }
    }

    /**
     * Hitung berapa banyak bahan yang cocok untuk recipe ini
     * Returns Pair<matchedCount, totalCount>
     */
    private fun getMatchedIngredientsCount(recipeIngredients: List<String>): Pair<Int, Int> {
        if (fridgeIngredients.isEmpty()) {
            return Pair(0, recipeIngredients.size)
        }

        val ingredientNames = fridgeIngredients.map { it.name.lowercase() }
        val matchedCount = recipeIngredients.count { recipeIngredient ->
            ingredientNames.any { fridgeItem ->
                fridgeItem.contains(recipeIngredient.lowercase(), ignoreCase = true) ||
                        recipeIngredient.lowercase().contains(fridgeItem, ignoreCase = true)
            }
        }
        return Pair(matchedCount, recipeIngredients.size)
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _detailUiState.value = _detailUiState.value.copy(error = null)
    }

    /**
     * Refresh recipes
     */
    fun refreshRecipes() {
        loadRecipes()
    }
}