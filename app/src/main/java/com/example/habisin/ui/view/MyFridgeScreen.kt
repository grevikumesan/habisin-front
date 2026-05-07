package com.example.habisin.ui.view

//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.habisin.ui.viewmodel.MyFridgeViewModel
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyFridgeScreen(
//    onNavigateToAddProduct: () -> Unit,
//    viewModel: MyFridgeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    var searchQuery by remember { mutableStateOf("") }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Column {
//                        Text("My Fridge", fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                        Text("7 items available", fontSize = 12.sp, color = Color.Gray)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface
//                )
//            )
//        },
//        containerColor = MaterialTheme.colorScheme.surface
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 16.dp)
//        ) {
//            // Search Bar
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = {
//                    searchQuery = it
//                    viewModel.onSearchQueryChange(it)
//                },
//                placeholder = { Text("Search Ingredients") },
//                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = PrimaryGreen,
//                    unfocusedBorderColor = Color.LightGray
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Filter Chips
//            FilterChips(
//                selectedCategory = uiState.selectedCategory,
//                onCategorySelected = { viewModel.onCategorySelected(it) }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Attention Required Section
//            if (uiState.selectedCategory == "Expiring" || uiState.selectedCategory == "All") {
//                AttentionRequiredSection(products = uiState.expiringProducts)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Product List
//            LazyColumn(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(uiState.filteredProducts) { product ->
//                    ProductListItem(
//                        product = product,
//                        onClick = { /* Handle product click */ }
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // At a Glance Section
//            AtAGlanceSection(totalItems = uiState.products.size, expiringCount = uiState.expiringProducts.size)
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//fun FilterChips(
//    selectedCategory: String,
//    onCategorySelected: (String) -> Unit
//) {
//    val categories = listOf("All", "Expiring", "Produce", "Dairy", "Meat", "Other")
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        categories.forEach { category ->
//            val isSelected = selectedCategory == category
//            Surface(
//                modifier = Modifier.clickable { onCategorySelected(category) },
//                shape = RoundedCornerShape(20.dp),
//                color = if (isSelected) PrimaryGreen else Color.LightGray.copy(alpha = 0.3f),
//                tonalElevation = if (isSelected) 4.dp else 0.dp
//            ) {
//                Text(
//                    text = category,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                    color = if (isSelected) Color.White else Color.DarkGray,
//                    fontSize = 14.sp,
//                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun AttentionRequiredSection(products: List<ProductModel>) {
//    if (products.isEmpty()) return
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = OrangeBackground),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        "ATTENTION REQUIRED",
//                        fontSize = 12.sp,
//                        color = Color.Gray,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        "${products.size} items expiring soon",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = DarkOrange
//                    )
//                }
//                Icon(
//                    Icons.Default.Notifications,
//                    contentDescription = "Notification",
//                    tint = DarkOrange
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            products.forEach { product ->
//                ExpiringProductRow(product = product)
//            }
//        }
//    }
//}
//
//@Composable
//fun ExpiringProductRow(product: ProductModel) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { }
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Box(
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(Color.White),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("🥚", fontSize = 20.sp)
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(product.name, fontWeight = FontWeight.Medium)
//                Text("${product.quantity} PCS", fontSize = 12.sp, color = Color.Gray)
//            }
//        }
//        BadgeDaysLeft(daysLeft = product.daysLeft)
//    }
//}
//
//@Composable
//fun ProductListItem(
//    product: ProductModel,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(RoundedCornerShape(10.dp))
//                        .background(LightGreen),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(getProductIcon(product.category), fontSize = 24.sp)
//                }
//                Spacer(modifier = Modifier.width(12.dp))
//                Column {
//                    Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
//                    Text("${product.quantity} ${product.unit}", fontSize = 12.sp, color = Color.Gray)
//                }
//            }
//            BadgeDaysLeft(daysLeft = product.daysLeft)
//        }
//    }
//}
//
//@Composable
//fun BadgeDaysLeft(daysLeft: Int) {
//    val backgroundColor = when {
//        daysLeft <= 1 -> Color(0xFFFFE0E0)
//        daysLeft <= 3 -> Color(0xFFFFF4E0)
//        else -> Color(0xFFE8F5E9)
//    }
//    val textColor = when {
//        daysLeft <= 1 -> Color(0xFFD32F2F)
//        daysLeft <= 3 -> Color(0xFFF57C00)
//        else -> Color(0xFF388E3C)
//    }
//    val dayText = when {
//        daysLeft == 0 -> "TODAY"
//        daysLeft == 1 -> "1 DAY"
//        else -> "$daysLeft DAYS"
//    }
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(8.dp))
//            .background(backgroundColor)
//            .padding(horizontal = 12.dp, vertical = 6.dp)
//    ) {
//        Text(
//            text = dayText,
//            color = textColor,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//@Composable
//fun AtAGlanceSection(totalItems: Int, expiringCount: Int) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        Card(
//            modifier = Modifier.weight(1f),
//            colors = CardDefaults.cardColors(containerColor = LightGreen),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    Icons.Default.Inventory,
//                    contentDescription = "Total Items",
//                    tint = PrimaryGreen,
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    totalItems.toString(),
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = PrimaryGreen
//                )
//                Text("TOTAL ITEMS", fontSize = 12.sp, color = Color.Gray)
//            }
//        }
//
//        Card(
//            modifier = Modifier.weight(1f),
//            colors = CardDefaults.cardColors(containerColor = OrangeBackground),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    Icons.Default.Warning,
//                    contentDescription = "Expiring",
//                    tint = DarkOrange,
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    expiringCount.toString(),
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = DarkOrange
//                )
//                Text("EXPIRING", fontSize = 12.sp, color = Color.Gray)
//            }
//        }
//    }
//}
//
//fun getProductIcon(category: String): String {
//    return when (category.lowercase()) {
//        "produce" -> "🥬"
//        "dairy" -> "🥛"
//        "meat" -> "🥩"
//        "other" -> "📦"
//        else -> "🥘"
//    }
//}