package com.example.habisin.ui.view.scan

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.habisin.ui.viewmodel.AddProductViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Input Products", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 📷 Camera Section
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp).clickable { },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6EDD3)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color(0xFF4B5C28),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Photo Product", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Point at product", fontSize = 12.sp, color = Color.Gray)
                }
            }

            // 📝 Manual Input
            Text("Manual Input", fontWeight = FontWeight.Medium, fontSize = 14.sp)

            TextField(
                value = uiState.itemName,
                onValueChange = { viewModel.onItemNameChange(it) },
                placeholder = { Text("e.g. Organic Avocados", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF7F4EE),
                    unfocusedContainerColor = Color(0xFFF7F4EE),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // 🏷️ CATEGORY WITH ICONS
            Text("CATEGORY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CategoryIconChip(
                    title = "PRODUCE",
                    icon = Icons.Default.Yard,
                    isSelected = uiState.category == "PRODUCE",
                    onClick = { viewModel.onCategorySelected("PRODUCE") },
                    modifier = Modifier.weight(1f)
                )
                CategoryIconChip(
                    title = "DAIRY",
                    icon = Icons.Default.LocalDrink,
                    isSelected = uiState.category == "DAIRY",
                    onClick = { viewModel.onCategorySelected("DAIRY") },
                    modifier = Modifier.weight(1f)
                )
                CategoryIconChip(
                    title = "MEAT",
                    icon = Icons.Default.KebabDining,
                    isSelected = uiState.category == "MEAT",
                    onClick = { viewModel.onCategorySelected("MEAT") },
                    modifier = Modifier.weight(1f)
                )
                CategoryIconChip(
                    title = "OTHER",
                    icon = Icons.Default.Inventory,
                    isSelected = uiState.category == "OTHER",
                    onClick = { viewModel.onCategorySelected("OTHER") },
                    modifier = Modifier.weight(1f)
                )
            }

            // 📅 Date & Quantity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("BEST BEFORE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF7F4EE))
                            .clickable { showDatePicker = true }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                uiState.bestBeforeDate?.formatDate() ?: "Oct 28, 2023",
                                fontWeight = FontWeight.Medium
                            )
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("QUANTITY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF7F4EE))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.onQuantityChange(uiState.quantity - 1) }) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }
                        Text(uiState.quantity.toString(), fontWeight = FontWeight.Bold)
                        IconButton(onClick = { viewModel.onQuantityChange(uiState.quantity + 1) }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }

            //  Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.addProduct(); onNavigateBack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2D4B6)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Add to Fridge", color = Color(0xFF8D5524), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2D4B6)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Scan Barcode", color = Color(0xFF8D5524), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onBestBeforeDateChange(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CategoryIconChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFD7E9C5) else Color.White
    val borderColor = if (isSelected) Color(0xFFD7E9C5) else Color(0xFFE0E0E0)
    val contentColor = if (isSelected) Color(0xFF2E4600) else Color.Gray

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = contentColor
            )
        }
    }
}

fun Date.formatDate(): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(this)
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    MaterialTheme {
        AddProductScreen(onNavigateBack = {})
    }
}
