package com.example.habisin.ui.view.fridge

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import com.example.habisin.R
import com.example.habisin.data.remote.ApiConfig
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.theme.HabisinTheme
import com.example.habisin.ui.viewmodel.MyFridgeViewModel
import com.example.habisin.util.getProductEmoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFridgeScreen(
    viewModel: MyFridgeViewModel = viewModel(),
    onNavigateToAddProduct: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val horizontalScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)) {
                Column {
                    Text(stringResource(R.string.fridge_title), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(stringResource(R.string.fridge_items_available, uiState.products.size), fontSize = 14.sp, color = HabisinTheme.colors.textMuted)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        // Outer AppRouter Scaffold already applies system-bar insets; don't double them.
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔍 Search Bar
            OutlinedTextField(
                value         = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder   = { Text(stringResource(R.string.fridge_search), fontSize = 14.sp, color = HabisinTheme.colors.fieldHint) },
                leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = HabisinTheme.colors.fieldHint) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = Color.Transparent,
                    unfocusedBorderColor    = Color.Transparent,
                    focusedContainerColor   = HabisinTheme.colors.fieldBg,
                    unfocusedContainerColor = HabisinTheme.colors.fieldBg,
                    focusedTextColor        = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
                    cursorColor             = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔄 KATEGORI
            Row(
                modifier              = Modifier.fillMaxWidth().horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Expiring", "Produce", "Dairy", "Meat", "Other").forEach { cat ->
                    val catLabel = when (cat) {
                        "All"      -> stringResource(R.string.filter_all)
                        "Expiring" -> stringResource(R.string.filter_expiring)
                        "Produce"  -> stringResource(R.string.filter_produce)
                        "Dairy"    -> stringResource(R.string.filter_dairy)
                        "Meat"     -> stringResource(R.string.filter_meat)
                        else       -> stringResource(R.string.filter_other)
                    }
                    FilterChip(
                        selected = uiState.selectedCategory == cat,   // key stays English for filtering
                        onClick  = { viewModel.onCategorySelected(cat) },
                        label    = { Text(catLabel) },
                        colors   = FilterChipDefaults.filterChipColors(
                            containerColor         = HabisinTheme.colors.fieldBg,
                            labelColor             = MaterialTheme.colorScheme.onSurface,
                            selectedContainerColor = HabisinTheme.colors.selectedContainer,
                            selectedLabelColor     = HabisinTheme.colors.onSelectedContainer
                        ),
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📜 CONTENT
            when {
                uiState.isLoading -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                }
                uiState.filteredProducts.isEmpty() -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.fridge_no_food),
                            color    = HabisinTheme.colors.textMuted,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier              = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.filteredProducts) { product ->
                            ProductCardItem(product)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCardItem(product: ProductModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.imageUrl.isNullOrEmpty()) {
                    // Resolve relative /uploads paths to a full URL so Coil can load them.
                    AsyncImage(
                        model = ApiConfig.imageUrl(product.imageUrl),
                        contentDescription = "${product.name}",
                        contentScale = ContentScale.Crop, // Agar gambar pas di dalam lingkaran
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(text = getProductEmoji(product.name), fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 👉 KUNCI 1: Beri padding end dan potong teks kepanjangan dengan Ellipsis
            Column(modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1, // Mencegah teks turun baris
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text     = "${product.quantity} ${product.unit}",
                    fontSize = 12.sp,
                    color    = HabisinTheme.colors.textMuted,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }

        val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
        val (badgeColor, textColor) = getBadgeColor(product.computedDaysLeft, isDark)
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(50))
                .background(badgeColor)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${product.computedDaysLeft} DAY${if (product.computedDaysLeft > 1) "S" else ""}",
                color      = textColor,
                fontSize   = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines   = 1,
                softWrap   = false
            )
        }
    }
}

fun getBadgeColor(days: Int, isDark: Boolean): Pair<Color, Color> {
    // Semantic status colors, muted and tuned per theme so they don't look like bright stickers.
    return if (isDark) when {
        days < 0  -> Pair(Color(0xFF4A2420), Color(0xFFFFB4A8))   // expired
        days <= 2 -> Pair(Color(0xFF3E331F), Color(0xFFE8C58A))   // urgent (amber)
        else      -> Pair(Color(0xFF273620), Color(0xFFB8D89A))   // safe (green)
    } else when {
        days < 0  -> Pair(Color(0xFFF4D7D7), Color(0xFF8B0000))
        days <= 2 -> Pair(Color(0xFFF6E2C6), Color(0xFF8D5524))
        else      -> Pair(Color(0xFFDDEAD0), Color(0xFF2E4600))
    }
}

@Preview(showBackground = true)
@Composable
fun MyFridgeScreenPreview() {
    MaterialTheme {
        MyFridgeScreen(onNavigateToAddProduct = {})
    }
}