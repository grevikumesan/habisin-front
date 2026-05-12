package com.example.habisin.ui.view.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.habisin.ui.component.RecipeTabButton
import com.example.habisin.ui.model.RecipeModel
import com.example.habisin.ui.viewmodel.RecipeViewModel

@Composable
fun RecipeDetailScreen(viewModel: RecipeViewModel) {
    val detailState by viewModel.detailUiState.collectAsState()
    val recipe = detailState.recipe

    var activeTab by remember { mutableStateOf("Ingredients") }
    val mediumGreen = Color(0xFF2D6A4F)

    recipe?.let { data ->
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = data.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .align(Alignment.BottomCenter),
                color = mediumGreen,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = data.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB7E4C7)
                    )

                    Text(
                        text = data.description,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Surface(
                        color = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                RecipeTabButton(
                                    label = "Ingredients",
                                    isSelected = activeTab == "Ingredients",
                                    onClick = { activeTab = "Ingredients" }
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                RecipeTabButton(
                                    label = "Directions",
                                    isSelected = activeTab == "Directions",
                                    onClick = { activeTab = "Directions" }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (activeTab == "Ingredients") {
                        data.ingredients.forEach { item ->
                            Text(
                                text = "• $item",
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        data.instructions.forEachIndexed { index, step ->
                            Row(modifier = Modifier.padding(vertical = 6.dp)) {
                                Text(
                                    text = "${index + 1}.",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFF9C4)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = step, color = Color.White, fontSize = 15.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    val dummyRecipe = RecipeModel(
        id = "1",
        title = "Gudeg Jogja",
        cookTime = "120 mins",
        difficulty = "Hard",
        imageUrl = "https://example.com/gudeg.jpg",
        description = "Gudeg adalah makanan khas Yogyakarta dan Jawa Tengah yang terbuat dari nangka muda yang dimasak dengan santan. Perlu waktu berjam-jam untuk membuat warna cokelat yang sempurna.",
        ingredients = listOf("Nangka Muda", "Santan Kelapa", "Gula Jawa", "Daun Jati"),
        instructions = listOf("Rebus nangka muda", "Masukkan santan dan bumbu", "Tunggu hingga meresap", "Sajikan dengan krecek"),
        category = "Traditional"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f).align(Alignment.BottomCenter),
            color = Color(0xFF2D6A4F),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(dummyRecipe.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Description", color = Color(0xFFB7E4C7), fontWeight = FontWeight.Bold)
                Text(dummyRecipe.description, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.White, CircleShape)) {
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFFFF9C4), CircleShape), contentAlignment = Alignment.Center) {
                        Text("Ingredients", color = Color(0xFF1B4332), fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text("Directions", color = Color.Black)
                    }
                }
            }
        }
    }
}