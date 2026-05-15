package com.example.habisin.ui.view.recipe

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.habisin.ui.viewmodel.RecipeViewModel

private val HabisinDarkGreen = Color(0xFF1B4332)
private val HabisinMidGreen = Color(0xFF2D6A4F)
private val HabisinAccentGreen = Color(0xFFB7E4C7)
private val HabisinLightCream = Color(0xFFFFF3D6)

// Enum biar type-safe (lebih baik daripada String)
private enum class DetailTab { Ingredients, Directions }

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    viewModel: RecipeViewModel,
    onBack: () -> Unit
) {
    val detailState by viewModel.detailUiState.collectAsState()

    // Fetch detail saat masuk screen (atau saat id berubah)
    LaunchedEffect(recipeId) { viewModel.getResepById(recipeId) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // ── Background image placeholder (karena belum ada imageUrl) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(HabisinMidGreen),
            contentAlignment = Alignment.Center
        ) {
            Text("🍜", fontSize = 100.sp)
        }

        // ── Back button (top-left) ──
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        when {
            detailState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            detailState.errorMessage != null -> {
                Text(
                    detailState.errorMessage ?: "",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            detailState.recipe != null -> {
                val recipe = detailState.recipe!!

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.65f)
                        .align(Alignment.BottomCenter),
                    color = HabisinMidGreen,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = recipe.resepName,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Description",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = HabisinAccentGreen
                        )
                        Text(
                            text = recipe.resepDescription,
                            fontSize = 14.sp,
                            color = Color.White,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(Modifier.height(20.dp))

                        // ── Tab Switcher dengan pill indicator ──
                        var activeTab by remember { mutableStateOf(DetailTab.Ingredients) }
                        AnimatedTabSwitcher(
                            activeTab = activeTab,
                            onTabChange = { activeTab = it }
                        )

                        Spacer(Modifier.height(20.dp))

                        // ── Content dengan animasi slide + fade ──
                        AnimatedContent(
                            targetState = activeTab,
                            transitionSpec = {
                                // Arah slide tergantung pindah ke kiri atau kanan
                                val direction =
                                    if (targetState.ordinal > initialState.ordinal) 1 else -1
                                (slideInHorizontally(
                                    animationSpec = tween(300)
                                ) { it * direction } + fadeIn(tween(300)))
                                    .togetherWith(
                                        slideOutHorizontally(
                                            animationSpec = tween(300)
                                        ) { -it * direction } + fadeOut(tween(300))
                                    )
                            },
                            label = "tab-content",
                            modifier = Modifier.animateContentSize()
                        ) { tab ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                when (tab) {
                                    DetailTab.Ingredients -> {
                                        recipe.resepIngredients.forEachIndexed { idx, item ->
                                            IngredientRow(index = idx + 1, text = item)
                                        }
                                    }
                                    DetailTab.Directions -> {
                                        recipe.resepDirections.forEachIndexed { idx, step ->
                                            DirectionRow(index = idx + 1, text = step)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}

// ── Pill-style tab switcher dengan animasi background ──
@Composable
private fun AnimatedTabSwitcher(
    activeTab: DetailTab,
    onTabChange: (DetailTab) -> Unit
) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            DetailTab.values().forEach { tab ->
                val isSelected = activeTab == tab
                // Animasi warna background pill saat berpindah
                val animatedBg by animateColorAsState(
                    targetValue = if (isSelected) HabisinAccentGreen else Color.Transparent,
                    animationSpec = tween(300),
                    label = "tab-bg"
                )
                val animatedTextColor by animateColorAsState(
                    targetValue = if (isSelected) HabisinDarkGreen else Color.Gray,
                    animationSpec = tween(300),
                    label = "tab-text"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(animatedBg)
                        .clickable { onTabChange(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = animatedTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun IngredientRow(index: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(HabisinLightCream),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$index",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = HabisinDarkGreen
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(text = text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
private fun DirectionRow(index: Int, text: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp)) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(HabisinLightCream),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$index",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = HabisinDarkGreen
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(text = text, color = Color.White, fontSize = 14.sp, lineHeight = 20.sp)
    }
}