//package com.example.habisin.ui.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
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
//import com.example.habisin.ui.viewmodel.AddProductViewModel
//import com.habisin.front.ui.theme.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddProductScreen(
//    onNavigateBack: () -> Unit,
//    viewModel: AddProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    var showDatePicker by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Input Products", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
//                navigationIcon = {
//                    IconButton(onClick = onNavigateBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // Photo Product Section
//            PhotoProductSection(onTakePhoto = {
//                // Handle photo capture
//            })
//
//            // Manual Input Section
//            ManualInputSection(
//                itemName = uiState.itemName,
//                onItemNameChange = { viewModel.onItemNameChange(it) },
//                selectedCategory = uiState.category,
//                onCategorySelected = { viewModel.onCategorySelected(it) },
//                bestBeforeDate = uiState.bestBeforeDate,
//                onDateSelected = { viewModel.onBestBeforeDateChange(it) },
//                quantity = uiState.quantity,
//                onQuantityChange = { viewModel.onQuantityChange(it) },
//                showDatePicker = showDatePicker,
//                onShowDatePickerChange = { showDatePicker = it }
//            )
//
//            // Action Buttons
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Button(
//                    onClick = { /* Handle scan barcode */ },
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(containerColor = LightOrange),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(Icons.Default.QrCodeScanner, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Scan Barcode", color = Color.Black)
//                }
//
//                Button(
//                    onClick = {
//                        viewModel.addProduct()
//                        onNavigateBack()
//                    },
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
//                    shape = RoundedCornerShape(12.dp),
//                    enabled = uiState.itemName.isNotBlank()
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Add to Fridge")
//                }
//            }
//        }
//
//        if (showDatePicker) {
//            DatePickerDialog(
//                onDateSelected = { date ->
//                    viewModel.onBestBeforeDateChange(date)
//                    showDatePicker = false
//                },
//                onDismiss = { showDatePicker = false }
//            )
//        }
//    }
//}
//
//@Composable
//fun PhotoProductSection(onTakePhoto: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .clickable(onClick = onTakePhoto),
//        colors = CardDefaults.cardColors(containerColor = LightGreen),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(RoundedCornerShape(50))
//                    .background(PrimaryGreen.copy(alpha = 0.2f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    Icons.Default.CameraAlt,
//                    contentDescription = "Camera",
//                    tint = PrimaryGreen,
//                    modifier = Modifier.size(40.dp)
//                )
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//            Text("Photo Product", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            Text("Point at product", fontSize = 12.sp, color = Color.Gray)
//        }
//    }
//}
//
//@Composable
//fun ManualInputSection(
//    itemName: String,
//    onItemNameChange: (String) -> Unit,
//    selectedCategory: String,
//    onCategorySelected: (String) -> Unit,
//    bestBeforeDate: Date?,
//    onDateSelected: (Date) -> Unit,
//    quantity: Int,
//    onQuantityChange: (Int) -> Unit,
//    showDatePicker: Boolean,
//    onShowDatePickerChange: (Boolean) -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//        Text("Manual Input", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//
//        // Item Name
//        OutlinedTextField(
//            value = itemName,
//            onValueChange = onItemNameChange,
//            label = { Text("Item Name") },
//            placeholder = { Text("e.g. Organic Avocados") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            leadingIcon = { Icon(Icons.Default.Label, contentDescription = null) }
//        )
//
//        // Category
//        Text("Category", fontWeight = FontWeight.Medium, fontSize = 14.sp)
//        CategorySelection(
//            selectedCategory = selectedCategory,
//            onCategorySelected = onCategorySelected
//        )
//
//        // Best Before Date
//        OutlinedTextField(
//            value = bestBeforeDate?.formatDate() ?: "",
//            onValueChange = {},
//            label = { Text("Best Before") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            readOnly = true,
//            trailingIcon = {
//                Icon(Icons.Default.CalendarToday, contentDescription = "Select date")
//            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = PrimaryGreen,
//                unfocusedBorderColor = Color.LightGray
//            )
//        )
//
//        // Quantity
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text("Quantity", fontWeight = FontWeight.Medium, fontSize = 14.sp)
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                IconButton(
//                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                        .background(LightGray)
//                ) {
//                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
//                }
//
//                Text(
//                    quantity.toString(),
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.width(40.dp),
//                    color = PrimaryGreen
//                )
//
//                IconButton(
//                    onClick = { onQuantityChange(quantity + 1) },
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                        .background(PrimaryGreen)
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategorySelection(
//    selectedCategory: String,
//    onCategorySelected: (String) -> Unit
//) {
//    val categories = listOf(
//        "Produce" to "🥬",
//        "Dairy" to "🥛",
//        "Meat" to "🥩",
//        "Other" to "📦"
//    )
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        categories.forEach { (category, icon) ->
//            val isSelected = selectedCategory == category
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable { onCategorySelected(category) }
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(
//                        if (isSelected) PrimaryGreen.copy(alpha = 0.1f) else Color.Transparent
//                    )
//                    .padding(12.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(icon, fontSize = 24.sp)
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    category,
//                    fontSize = 12.sp,
//                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
//                    color = if (isSelected) PrimaryGreen else Color.Gray
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DatePickerDialog(
//    onDateSelected: (Date) -> Unit,
//    onDismiss: () -> Unit
//) {
//    // Simplified date picker - in production use DatePicker from Material 3
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Select Best Before Date") },
//        text = { Text("Use date picker component here") },
//        confirmButton = {
//            TextButton(onClick = {
//                onDateSelected(Date())
//                onDismiss()
//            }) {
//                Text("OK")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        },
//        shape = RoundedCornerShape(16.dp)
//    )
//}
//
//fun Date.formatDate(): String {
//    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
//    return sdf.format(this)
//}