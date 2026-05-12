package com.example.habisin.ui.view.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    recipes: List<RecipeModel>,
    onRecipeClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Title
        item {
            Text(
                text = "Recipe",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp),
                color = Color(0xFFFF1B43)
            )
        }

        // 2. Search Bar
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

        // 3. Category Scroll Bar (HORIZONTAL SCROLL)
        item {
            val categories = listOf("All", "Rice", "Chicken", "Egg", "Fish", "Dessert", "Meat")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { cat ->
                    CategoryItem(
                        label = cat,
                        isSelected = cat == selectedCategory,
                        onClick = { selectedCategory = cat }
                    )
                }
            }
        }

        // 4. Sub-title: Recommended
        if (recipes.isNotEmpty()) {
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
                    modifier = Modifier.padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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

        // 7. Grid Layout untuk All Recipes
        if (recipes.isNotEmpty()) {
            items(recipes.chunked(2)) { rowItems ->
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
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            // Empty State
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 64.dp)
                    ) {
                        Text(
                            text = "😔",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tidak ada resep yang cocok",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tambahkan bahan ke kulkas untuk melihat resep",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { /* Navigate to fridge */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF1B43)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Tambah Bahan", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    val dummy = listOf(
        RecipeModel(
            id = "1", 
            title = "Nasi Goreng UC", 
            cookTime = "15m", 
            difficulty = "Easy", 
            imageUrl = "", 
            description = "Nasi goreng spesial ala mahasiswa Informatika yang praktis.",
            ingredients = listOf("Nasi", "Telur"),
            category = "Rice"
        ),
        RecipeModel(
            id = "2", 
            title = "Ayam Geprek", 
            cookTime = "20m", 
            difficulty = "Easy", 
            imageUrl = "", 
            description = "Ayam krispi dengan sambal bawang yang nendang bumbunya.",
            ingredients = listOf("Ayam", "Cabai"),
            category = "Chicken"
        ),
        RecipeModel(
            id = "3", 
            title = "Omelet Sayur", 
            cookTime = "10m", 
            difficulty = "Easy", 
            imageUrl = "", 
            description = "Menu sehat pagi hari untuk energi kuliah seharian.",
            ingredients = listOf("Telur", "Bayam"),
            category = "Egg"
        ),
        RecipeModel(
            id = "4", 
            title = "Es Teh Manis", 
            cookTime = "5m", 
            difficulty = "Easy", 
            imageUrl = "", 
            description = "Minuman segar pendamping makanan pedas.",
            ingredients = listOf("Teh", "Gula"),
            category = "Dessert"
        )
    )

    RecipeScreen(
        recipes = dummy,
        onRecipeClick = {}
    )
}