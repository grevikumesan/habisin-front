package com.example.habisin.ui.view.fridge

import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.ui.model.ProductModel
import com.example.habisin.ui.viewmodel.MyFridgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFridgeScreen(
    viewModel: MyFridgeViewModel = viewModel(),
    onNavigateToAddProduct: () -> Unit // Ditambahkan agar sesuai dengan pemanggilan di Preview
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val horizontalScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)) {
                Column {
                    Text("My Fridge", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    // Menggunakan data dinamis dari uiState
                    Text("${uiState.products.size} items available", fontSize = 14.sp, color = Color.Gray)
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔍 Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChange(it)
                },
                placeholder = { Text("Search Ingredients", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔄 KATEGORI: HORIZONTAL SCROLL
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedCategory == "All",
                    onClick = { viewModel.onCategorySelected("All") },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD7E9C5),
                        selectedLabelColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                )
                FilterChip(
                    selected = uiState.selectedCategory == "Expiring",
                    onClick = { viewModel.onCategorySelected("Expiring") },
                    label = { Text("Expiring") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD7E9C5),
                        selectedLabelColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                )
                FilterChip(
                    selected = uiState.selectedCategory == "Produce",
                    onClick = { viewModel.onCategorySelected("Produce") },
                    label = { Text("Produce") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD7E9C5),
                        selectedLabelColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                )
                FilterChip(
                    selected = uiState.selectedCategory == "Dairy",
                    onClick = { viewModel.onCategorySelected("Dairy") },
                    label = { Text("Dairy") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD7E9C5),
                        selectedLabelColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📜 LIST MAKANAN: VERTICAL SCROLL
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredProducts) { product ->
                    ProductCardItem(product)
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
            .background(Color(0xFFF7F4EE))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = getProductEmoji(product.name), fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${product.quantity} ${product.unit}", fontSize = 12.sp, color = Color.Gray)
            }
        }

        val (badgeColor, textColor) = getBadgeColor(product.daysLeft)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(badgeColor)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${product.daysLeft} DAY${if (product.daysLeft > 1) "S" else ""}",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun getBadgeColor(days: Int): Pair<Color, Color> {
    return when (days) {
        1, 2, 3 -> Pair(Color(0xFFF2D4B6), Color(0xFF8D5524))
        else -> Pair(Color(0xFFD7E9C5), Color(0xFF2E4600))
    }
}

fun getProductEmoji(name: String): String {
    return when {
        name.contains("Egg", ignoreCase = true) -> "🥚"
        name.contains("Bread", ignoreCase = true) -> "🥖"
        name.contains("Apple", ignoreCase = true) -> "🍎"
        name.contains("Milk", ignoreCase = true) -> "🥛"
        name.contains("Carrot", ignoreCase = true) -> "🥕"
        name.contains("Spinach", ignoreCase = true) -> "🥬"
        else -> "📦"
    }
}

@Preview(showBackground = true)
@Composable
fun MyFridgeScreenPreview() {
    MaterialTheme {
        MyFridgeScreen(onNavigateToAddProduct = {})
    }
}
