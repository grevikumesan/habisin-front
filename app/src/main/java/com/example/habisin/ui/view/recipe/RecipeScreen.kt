package com.example.habisin.ui.view.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habisin.ui.component.CategoryItem
import com.example.habisin.ui.component.HabisinTextField
import com.example.habisin.ui.component.RecommendedCard
import com.example.habisin.ui.model.RecipeModel

@Composable
fun RecipeScreen(
    recipes: List<RecipeModel>,
    onRecipeClick: (String) -> Unit
) {
    // State internal untuk search bar
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Title Utama (Left Position)
        item {
            Text(
                text = "Recipe",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp),
                color = Color(0xFF1B4332) // Hijau Tua Habisin
            )
        }

        // 2. Search Bar (Menggunakan HabisinTextField)
        item {
            HabisinTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search Recipe",
                modifier = Modifier.padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            )
        }

        // 3. Category Scroll Bar (Gojek Style - Lingkaran)
        item {
            val categories = listOf("All", "Rice", "Chicken", "Egg", "Dessert", "Meat")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { cat ->
                    CategoryItem(label = cat, isSelected = cat == "All")
                }
            }
        }

        // 4. Sub-title: Recommended
        item {
            Text(
                text = "Recommended",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        // 5. Recommended Horizontal Scroll
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Filter data rekomendasi (Misal 2 item teratas dari database)
                items(recipes.take(2)) { recipe ->
                    RecommendedCard(
                        title = recipe.title,
                        description = recipe.description,
                        imageUrl = recipe.imageUrl,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
            }
        }

        // 6. Sub-title: All Recipe
        item {
            Text(
                text = "All Recipe",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        // 7. Grid Others (2 Columns x N Rows)
        val rows = recipes.chunked(2)
        items(rows) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (recipe in rowItems) {
                    Box(modifier = Modifier.weight(1f)) {
                        RecommendedCard(
                            title = recipe.title,
                            description = recipe.description,
                            imageUrl = recipe.imageUrl,
                            onClick = { onRecipeClick(recipe.id) }
                        )
                    }
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    // Data Dummy Hanya untuk Preview UI di Surabaya
    val dummy = listOf(
        RecipeModel("1", "Nasi Goreng UC", "15m", "Easy", "", "Nasi goreng spesial ala mahasiswa Informatika yang praktis."),
        RecipeModel("2", "Ayam Geprek", "20m", "Easy", "", "Ayam krispi dengan sambal bawang yang nendang bumbunya."),
        RecipeModel("3", "Omelet Sayur", "10m", "Easy", "", "Menu sehat pagi hari untuk energi kuliah seharian."),
        RecipeModel("4", "Es Teh Manis", "5m", "Easy", "", "Minuman segar pendamping makanan pedas.")
    )

    RecipeScreen(
        recipes = dummy,
        onRecipeClick = {}
    )
}