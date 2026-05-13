package com.example.habisin.ui.view.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habisin.ui.view.component.HabisinTextField
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.viewmodel.RecipeViewModel

private val HabisinDarkGreen = Color(0xFF1B4332)
private val HabisinMidGreen = Color(0xFF2D6A4F)
private val HabisinLightGreen = Color(0xFFD8F3DC)

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load saat pertama dibuka
    LaunchedEffect(Unit) { viewModel.loadRecipes() }

    if (uiState.needsSubscription) {
        SubscriptionRequiredCard(onSubscribe = onNavigateToSubscription)
        return
    }

    // Filter berdasarkan search query (client-side filtering)
    val filteredRecipes = remember(uiState.recipes, uiState.searchQuery) {
        if (uiState.searchQuery.isBlank()) uiState.recipes
        else uiState.recipes.filter {
            it.resepName.contains(uiState.searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = HabisinDarkGreen
                )
            }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 24.dp, bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // ── Title "Recipe" (full width) ──
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Recipe",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HabisinDarkGreen
                        )
                    }

                    // ── Search Bar (full width) ──
                    item(span = { GridItemSpan(2) }) {
                        HabisinTextField(
                            value = uiState.searchQuery,
                            onValueChange = viewModel::onSearchQueryChange,
                            placeholder = "Search Recipes",
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        )
                    }

                    // ── Section: Recommended (full width) ──
                    if (filteredRecipes.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Text(
                                    text = "Recommended",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HabisinDarkGreen,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredRecipes.take(5).size) { idx ->
                                        val recipe = filteredRecipes[idx]
                                        RecommendedCard(
                                            recipe = recipe,
                                            onClick = { onRecipeClick(recipe.id) }
                                        )
                                    }
                                }
                            }
                        }

                        // ── Section: All Recipes header (full width) ──
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = "All Recipes",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = HabisinDarkGreen,
                                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                            )
                        }
                    }

                    // ── Grid items (2 columns) ──
                    items(filteredRecipes.size) { idx ->
                        val recipe = filteredRecipes[idx]
                        GridRecipeCard(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe.id) }
                        )
                    }

                    if (filteredRecipes.isEmpty() && !uiState.isLoading) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Belum ada resep",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Card horizontal (Recommended) ──
@Composable
private fun RecommendedCard(
    recipe: RecipeModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HabisinLightGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Placeholder image area (karena backend belum kasih imageUrl)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(HabisinMidGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 40.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                recipe.resepName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = HabisinDarkGreen,
                maxLines = 1
            )
            Text(
                recipe.resepDescription,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// ── Card grid (All Recipes) ──
@Composable
private fun GridRecipeCard(
    recipe: RecipeModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(HabisinLightGreen),
                contentAlignment = Alignment.Center
            ) {
                Text("🍲", fontSize = 36.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                recipe.resepName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = HabisinDarkGreen,
                maxLines = 1
            )
            Text(
                recipe.resepDescription,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun SubscriptionRequiredCard(onSubscribe: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFF1B4332),
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Fitur Premium",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B4332)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Berlangganan untuk mengakses ribuan resep AI",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onSubscribe,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Subscribe Sekarang", fontWeight = FontWeight.Bold)
            }
        }
    }
}