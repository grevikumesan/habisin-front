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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.habisin.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORT KOMPONEN BUATANMU:
import com.example.habisin.ui.view.component.HabisinTextField
import com.example.habisin.ui.view.component.CategoryItem
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.theme.HabisinTheme
import com.example.habisin.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,
    onSavedRecipeClick: (Int) -> Unit = {},
    onNavigateToSubscription: () -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSaved by remember { mutableStateOf(false) }
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
    // Load saved recipes lazily — only when the user opens "Tersimpan" (keeps tab entry fast).
    LaunchedEffect(showSaved) {
        if (showSaved) viewModel.loadSavedRecipes()
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

    val detailUiState by viewModel.detailUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (!detailUiState.isGenerating) onGenerateClick() },
                containerColor = HabisinTheme.colors.action,
                contentColor   = HabisinTheme.colors.onAction,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (detailUiState.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = HabisinTheme.colors.onAction,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Restaurant, contentDescription = null)
                    }
                    Text(
                        stringResource(if (detailUiState.isGenerating) R.string.recipe_generating else R.string.recipe_generate),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        // Outer AppRouter Scaffold already applies system-bar insets; don't double them.
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(innerPadding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else if (uiState.errorMessage != null) {
                Text(uiState.errorMessage ?: "", modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 100.dp)
                ) {
                    Text(stringResource(R.string.recipe_title), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(16.dp))

                    HabisinTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = stringResource(R.string.recipe_search),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HabisinTheme.colors.fieldHint) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Katalog / Tersimpan toggle
                    RecipeModeToggle(showSaved = showSaved, onChange = { showSaved = it })
                    Spacer(modifier = Modifier.height(16.dp))

                  if (!showSaved) {
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
                        Text(stringResource(R.string.recipe_recommended), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(recommendedRecipes) { recipe ->
                                RecipeGridCard(recipe = recipe, width = 300.dp, imageHeight = 160.dp, onClick = { onRecipeClick(recipe.id) })
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (otherRecipes.isNotEmpty()) {
                        Text(stringResource(R.string.recipe_other), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.height(400.dp)) {
                            LazyHorizontalGrid(
                                rows = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(otherRecipes) { recipe ->
                                    RecipeGridCard(recipe = recipe, width = 165.dp, imageHeight = 110.dp, onClick = { onRecipeClick(recipe.id) })
                                }
                            }
                        }
                    }
                  } else {
                    // ── Saved recipes (generated / manually saved) ──
                    val saved = uiState.savedRecipes.filter {
                        it.resepName.contains(uiState.searchQuery, ignoreCase = true)
                    }
                    if (saved.isEmpty()) {
                        Text(
                            stringResource(R.string.recipe_no_saved),
                            color = HabisinTheme.colors.textMuted,
                            fontSize = 14.sp
                        )
                    } else {
                        saved.forEach { recipe ->
                            SavedRecipeCard(recipe = recipe, onClick = { onSavedRecipeClick(recipe.id) })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                  }
                }
            }

            // Generate can take a few seconds (Gemini latency) — show a clear blocking overlay.
            if (detailUiState.isGenerating) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.45f))
                        .clickable {},                       // swallow taps while generating
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = HabisinTheme.colors.action)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                stringResource(R.string.recipe_generating_message),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedRecipeCard(recipe: RecipeModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)).background(HabisinTheme.colors.onLimeCard),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = HabisinTheme.colors.limeCard)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(recipe.resepName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(recipe.resepDescription, fontSize = 12.sp, color = HabisinTheme.colors.textMuted, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun RecipeModeToggle(showSaved: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToggleChip(stringResource(R.string.recipe_catalog), selected = !showSaved, modifier = Modifier.weight(1f)) { onChange(false) }
        ToggleChip(stringResource(R.string.recipe_saved), selected = showSaved, modifier = Modifier.weight(1f)) { onChange(true) }
    }
}

@Composable
private fun ToggleChip(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) HabisinTheme.colors.selectedContainer else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) HabisinTheme.colors.onSelectedContainer else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

// ─── PRIVATE COMPOSABLE UNTUK CARD (Langsung taruh di sini saja) ───────────

/** Vertical recipe card: image on top, then name + one-line (…) description, left-aligned. */
@Composable
private fun RecipeGridCard(recipe: RecipeModel, width: Dp, imageHeight: Dp, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.width(width).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Food image (rounded, inset). Real photo when present, else a placeholder.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder behind, so a failed/absent image gracefully shows the icon.
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(34.dp)
                )
                if (!recipe.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = com.example.habisin.data.remote.ApiConfig.imageUrl(recipe.imageUrl),
                        contentDescription = recipe.resepName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
                Text(
                    recipe.resepName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    recipe.resepDescription,
                    fontSize = 12.sp,
                    color = HabisinTheme.colors.textMuted,
                    maxLines = 1,                       // one line; "…" shows it's longer
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SubscriptionRequiredCard(onSubscribe: () -> Unit) {
    // (Isi SubscriptionRequiredCard disamakan dengan sebelumnya)
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = HabisinTheme.colors.action, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.recipe_premium_title), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.recipe_premium_subtitle), fontSize = 14.sp, color = HabisinTheme.colors.textMuted, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onSubscribe,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HabisinTheme.colors.action,
                    contentColor   = HabisinTheme.colors.onAction
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(stringResource(R.string.sub_subscribe), fontWeight = FontWeight.Bold)
            }
        }
    }
}