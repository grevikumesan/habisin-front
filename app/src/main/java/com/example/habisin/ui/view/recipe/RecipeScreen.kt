package com.example.habisin.ui.view.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORT KOMPONEN BUATANMU:
import com.example.habisin.ui.view.component.HabisinTextField
import com.example.habisin.ui.view.component.CategoryItem
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.viewmodel.RecipeViewModel

private val HabisinDarkGreen = Color(0xFF1B4332)
private val HabisinMidGreen = Color(0xFF2D6A4F)
private val HabisinLightCream = Color(0xFFFFFDF6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,
    onNavigateToSubscription: () -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val categories = listOf(
        "All",
        "Makanan Utama",
        "Sayur & Nabati",
        "Camilan & Jajanan Pasar",
        "Minuman",
        "Sambal & Bumbu Dasar",
        "Lainnya"
    )
    var selectedCategory by remember { mutableStateOf("All") }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    if (uiState.needsSubscription) {
        SubscriptionRequiredCard(onSubscribe = onNavigateToSubscription)
        return
    }

    val filteredRecipes = remember(uiState.recipes, uiState.searchQuery, selectedCategory) {
        uiState.recipes.filter { recipe ->
            val matchSearch = recipe.resepName.contains(uiState.searchQuery, ignoreCase = true)
            val matchCategory = selectedCategory == "All" || recipe.resepCategory == selectedCategory
            matchSearch && matchCategory
        }
    }

    val recommendedRecipes = filteredRecipes.take(3)
    val otherRecipes = filteredRecipes.drop(3).take(20)

    val detailUiState by viewModel.detailUiState.collectAsState()  // ← add this

    ExtendedFloatingActionButton(
        onClick = { if (!detailUiState.isGenerating) onGenerateClick() },  // ← guard
        containerColor = if (detailUiState.isGenerating) Color.Gray else HabisinDarkGreen,
        contentColor = Color.White,
        icon = {
            if (detailUiState.isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Restaurant, contentDescription = null)
            }
        },
        text = {
            Text(
                if (detailUiState.isGenerating) "Generating..." else "Generate Resep",
                fontWeight = FontWeight.Bold
            )
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGenerateClick,
                containerColor = HabisinDarkGreen,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        "Generate Resep",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().background(Color.White).padding(innerPadding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = HabisinDarkGreen)
            } else if (uiState.errorMessage != null) {
                Text(uiState.errorMessage ?: "", modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 100.dp)
                ) {
                    Text("Recipe", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = HabisinDarkGreen)
                    Spacer(modifier = Modifier.height(16.dp))

                    HabisinTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = "Search Recipes",
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // MENGGUNAKAN CategoryItem DARI FOLDER COMPONENT
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryItem(
                                label = category,
                                isSelected = category == selectedCategory,
                                modifier = Modifier.clickable { selectedCategory = category } // Ini yang bikin tombolnya bisa diklik!
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    if (recommendedRecipes.isNotEmpty()) {
                        Text("Recommended", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HabisinDarkGreen)
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(recommendedRecipes) { recipe ->
                                RecommendedRecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (otherRecipes.isNotEmpty()) {
                        Text("Recipe Others", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HabisinDarkGreen)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.height(260.dp)) {
                            LazyHorizontalGrid(
                                rows = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(otherRecipes) { recipe ->
                                    OtherRecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── PRIVATE COMPOSABLE UNTUK CARD (Langsung taruh di sini saja) ───────────

@Composable
private fun RecommendedRecipeCard(recipe: RecipeModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HabisinLightCream),
        modifier = Modifier.width(260.dp).height(140.dp).clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(70.dp).clip(CircleShape).background(HabisinDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(recipe.resepName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HabisinDarkGreen, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(recipe.resepDescription, fontSize = 12.sp, color = Color.Gray, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun OtherRecipeCard(recipe: RecipeModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.width(220.dp).height(110.dp).clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(HabisinMidGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(recipe.resepName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HabisinDarkGreen, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(recipe.resepDescription, fontSize = 11.sp, color = Color.DarkGray, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun SubscriptionRequiredCard(onSubscribe: () -> Unit) {
    // (Isi SubscriptionRequiredCard disamakan dengan sebelumnya)
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1B4332), modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Fitur Premium", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B4332))
            Spacer(Modifier.height(8.dp))
            Text("Berlangganan untuk mengakses ribuan resep AI", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onSubscribe,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Langganan Sekarang", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}