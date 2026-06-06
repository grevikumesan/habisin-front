package com.example.habisin.ui.view.scan

import android.app.Application
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habisin.ui.uistate.AddProductScanUiStates
import coil.compose.AsyncImage
import com.example.habisin.ui.theme.HabisinTheme
import com.example.habisin.ui.viewmodel.AddProductViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFridge: () -> Unit, // <-- 1. Tambahin jalur ini aja
    onNavigateToScanner: () -> Unit,
    viewModel: AddProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val barcodeState by viewModel.uiStateBarcode.collectAsState()
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d("Upload", "Picker returned uri=$uri")  // ← add this
        viewModel.onImageSelected(uri)
    }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.consumeSuccess() // Reset state
            onNavigateToFridge()       // Pindah ke layar Kulkas
        }
    }

    LaunchedEffect(barcodeState) {
        when(barcodeState) {
            is AddProductScanUiStates.Success -> {
                val name = (barcodeState as AddProductScanUiStates.Success).itemName
                viewModel.onItemNameChange(name)
            }
            else -> Unit
        }
    }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Input Products", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
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
                modifier = Modifier.fillMaxWidth().height(200.dp).clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = CardDefaults.cardColors(containerColor = HabisinTheme.colors.limeCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                if (uiState.imageUri != null) {
                    AsyncImage(
                        model = uiState.imageUri,
                        contentDescription = "Selected product image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = HabisinTheme.colors.onLimeCard,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Photo Product", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HabisinTheme.colors.onLimeCard)
                        Text("Upload Photo from Gallery", fontSize = 12.sp, color = HabisinTheme.colors.onLimeCard.copy(alpha = 0.7f))
                    }
                }
            }

            // 📝 Manual Input
            Text("Manual Input", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)

            TextField(
                value = uiState.itemName,
                onValueChange = { viewModel.onItemNameChange(it) },
                placeholder = { Text("e.g. Organic Avocados", color = HabisinTheme.colors.fieldHint) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = HabisinTheme.colors.fieldBg,
                    unfocusedContainerColor = HabisinTheme.colors.fieldBg,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // 🏷️ CATEGORY WITH ICONS
            Text("CATEGORY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HabisinTheme.colors.textMuted)
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
                    Text("BEST BEFORE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HabisinTheme.colors.textMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(HabisinTheme.colors.fieldBg)
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
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = HabisinTheme.colors.fieldHint)
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("QUANTITY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HabisinTheme.colors.textMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(HabisinTheme.colors.fieldBg)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.onQuantityChange(uiState.quantity - 1) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(uiState.quantity.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        IconButton(onClick = { viewModel.onQuantityChange(uiState.quantity + 1) }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            //  Buttons — both are primary actions, so both use the single coral action color
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.addProduct() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HabisinTheme.colors.action,
                        contentColor   = HabisinTheme.colors.onAction
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Add to Fridge", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { onNavigateToScanner() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HabisinTheme.colors.action),
                    border = BorderStroke(1.5.dp, HabisinTheme.colors.action),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Scan Barcode", fontWeight = FontWeight.Bold)
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
    val backgroundColor = if (isSelected) HabisinTheme.colors.limeCard else MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) HabisinTheme.colors.limeCard else MaterialTheme.colorScheme.outline
    val contentColor = if (isSelected) HabisinTheme.colors.onLimeCard else MaterialTheme.colorScheme.onSurfaceVariant

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
        AddProductScreen(
            onNavigateBack = {},
            onNavigateToFridge = {}, // <-- Cukup tambahin kurung kurawal kosong
            onNavigateToScanner = {},
        )
    }
}
