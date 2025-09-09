package com.example.wherenow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource

data class InterestCategory(
    val id: String,
    val title: String,
    val iconType: String, // identificador del ícono dibujado
    val color: Color
)

@Composable
fun QuizScreen(navController: NavController) {
    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    val interests = listOf(
        InterestCategory("music",       stringResource(R.string.interest_music),       "music",       Color(0xFF9C27B0)),
        InterestCategory("food",        stringResource(R.string.interest_food),        "food",        Color(0xFFFF9800)),
        InterestCategory("arts",        stringResource(R.string.interest_arts),        "arts",        Color(0xFFE91E63)),
        InterestCategory("gaming",      stringResource(R.string.interest_gaming),      "gaming",      Color(0xFF2196F3)),
        InterestCategory("sports",      stringResource(R.string.interest_sports),      "sports",      Color(0xFF4CAF50)),
        InterestCategory("learning",    stringResource(R.string.interest_learning),    "learning",    Color(0xFF673AB7)),
        InterestCategory("social",      stringResource(R.string.interest_social),      "social",      Color(0xFF009688)),
        InterestCategory("photography", stringResource(R.string.interest_photography), "photography", Color(0xFFFFC107)),
        InterestCategory("business",    stringResource(R.string.interest_business),    "business",    Color(0xFF607D8B)),
        InterestCategory("health",      stringResource(R.string.interest_health),      "health",      Color(0xFFF44336))
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB388FF), // morado suave
                        Color(0xFFFF80AB)  // rosa
                    )
                )
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InterestSelectionScreen(
                    interests = interests,
                    selectedInterests = selectedInterests,
                    onInterestToggle = { interestId ->
                        selectedInterests = if (selectedInterests.contains(interestId)) {
                            selectedInterests - interestId
                        } else {
                            selectedInterests + interestId
                        }
                    },
                    onContinue = {
                        navController.navigate(NavRoutes.CONFIRMATION)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun InterestSelectionScreen(
    interests: List<InterestCategory>,
    selectedInterests: Set<String>,
    onInterestToggle: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.quiz_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.quiz_subtitle),
            fontSize = 16.sp,
            color = Color(0xFF718096),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(interests) { interest ->
                InterestCard(
                    interest = interest,
                    isSelected = selectedInterests.contains(interest.id),
                    onClick = { onInterestToggle(interest.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF718096))
            ) {
                CustomIcon(iconType = "arrow_back", color = Color(0xFF718096), size = 16.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.back)+">")
            }

            Button(
                onClick = onContinue,
                enabled = selectedInterests.size >= 3,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_),
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                CustomIcon(iconType = "arrow_forward", color = Color.White, size = 16.dp)
            }
        }
    }
}

@Composable
fun InterestCard(
    interest: InterestCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        Color(0xFF9C27B0),
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF3E5F5) else Color(0xFFF7FAFC)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomIcon(
                iconType = interest.iconType,
                color = interest.color,
                size = 24.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = interest.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun CustomIcon(
    iconType: String,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        when (iconType) {
            "music" -> drawMusicIcon(color)
            "food" -> drawFoodIcon(color)
            "arts" -> drawArtsIcon(color)
            "gaming" -> drawGamingIcon(color)
            "sports" -> drawSportsIcon(color)
            "learning" -> drawLearningIcon(color)
            "social" -> drawSocialIcon(color)
            "photography" -> drawPhotographyIcon(color)
            "business" -> drawBusinessIcon(color)
            "health" -> drawHealthIcon(color)
            "heart" -> drawHealthIcon(color)
            "arrow_back" -> drawArrowBackIcon(color)
            "arrow_forward" -> drawArrowForwardIcon(color)
        }
    }
}

// ===== Íconos dibujados (reutilizados por las tarjetas) =====

fun DrawScope.drawMusicIcon(color: Color) {
    drawCircle(color = color, radius = size.width * 0.15f, center = Offset(size.width * 0.3f, size.height * 0.7f))
    drawRect(color = color, topLeft = Offset(size.width * 0.42f, size.height * 0.2f), size = Size(size.width * 0.08f, size.height * 0.5f))
    drawCircle(color = color, radius = size.width * 0.15f, center = Offset(size.width * 0.7f, size.height * 0.6f))
    drawRect(color = color, topLeft = Offset(size.width * 0.82f, size.height * 0.1f), size = Size(size.width * 0.08f, size.height * 0.5f))
}

fun DrawScope.drawFoodIcon(color: Color) {
    drawRect(color = color, topLeft = Offset(size.width * 0.2f, size.height * 0.1f), size = Size(size.width * 0.06f, size.height * 0.8f))
    drawRect(color = color, topLeft = Offset(size.width * 0.3f, size.height * 0.1f), size = Size(size.width * 0.06f, size.height * 0.3f))
    drawRect(color = color, topLeft = Offset(size.width * 0.4f, size.height * 0.1f), size = Size(size.width * 0.06f, size.height * 0.3f))
    drawRect(color = color, topLeft = Offset(size.width * 0.7f, size.height * 0.1f), size = Size(size.width * 0.06f, size.height * 0.8f))
    drawOval(color = color, topLeft = Offset(size.width * 0.65f, size.height * 0.1f), size = Size(size.width * 0.16f, size.height * 0.3f))
}

fun DrawScope.drawArtsIcon(color: Color) {
    drawOval(color = color, topLeft = Offset(size.width * 0.1f, size.height * 0.2f), size = Size(size.width * 0.8f, size.height * 0.6f), style = Stroke(width = size.width * 0.08f))
    drawCircle(color = color, radius = size.width * 0.08f, center = Offset(size.width * 0.3f, size.height * 0.4f))
    drawCircle(color = color, radius = size.width * 0.08f, center = Offset(size.width * 0.7f, size.height * 0.4f))
}

fun DrawScope.drawGamingIcon(color: Color) {
    drawRoundRect(color = color, topLeft = Offset(size.width * 0.1f, size.height * 0.3f), size = Size(size.width * 0.8f, size.height * 0.4f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.1f))
    drawCircle(color = Color.White, radius = size.width * 0.06f, center = Offset(size.width * 0.3f, size.height * 0.5f))
    drawCircle(color = Color.White, radius = size.width * 0.06f, center = Offset(size.width * 0.7f, size.height * 0.5f))
}

fun DrawScope.drawSportsIcon(color: Color) {
    drawRect(color = color, topLeft = Offset(size.width * 0.1f, size.height * 0.4f), size = Size(size.width * 0.15f, size.height * 0.2f))
    drawRect(color = color, topLeft = Offset(size.width * 0.25f, size.height * 0.45f), size = Size(size.width * 0.5f, size.height * 0.1f))
    drawRect(color = color, topLeft = Offset(size.width * 0.75f, size.height * 0.4f), size = Size(size.width * 0.15f, size.height * 0.2f))
}

fun DrawScope.drawLearningIcon(color: Color) {
    val path = Path().apply {
        moveTo(size.width * 0.1f, size.height * 0.5f)
        lineTo(size.width * 0.5f, size.height * 0.2f)
        lineTo(size.width * 0.9f, size.height * 0.5f)
        lineTo(size.width * 0.5f, size.height * 0.7f)
        close()
    }
    drawPath(path = path, color = color)
    drawRect(color = color, topLeft = Offset(size.width * 0.75f, size.height * 0.5f), size = Size(size.width * 0.1f, size.height * 0.3f))
}

fun DrawScope.drawSocialIcon(color: Color) {
    drawCircle(color = color, radius = size.width * 0.12f, center = Offset(size.width * 0.3f, size.height * 0.3f))
    drawCircle(color = color, radius = size.width * 0.12f, center = Offset(size.width * 0.7f, size.height * 0.3f))
    drawOval(color = color, topLeft = Offset(size.width * 0.18f, size.height * 0.5f), size = Size(size.width * 0.24f, size.height * 0.3f))
    drawOval(color = color, topLeft = Offset(size.width * 0.58f, size.height * 0.5f), size = Size(size.width * 0.24f, size.height * 0.3f))
}

fun DrawScope.drawPhotographyIcon(color: Color) {
    drawRoundRect(color = color, topLeft = Offset(size.width * 0.1f, size.height * 0.3f), size = Size(size.width * 0.8f, size.height * 0.5f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.05f))
    drawCircle(color = Color.White, radius = size.width * 0.15f, center = Offset(size.width * 0.5f, size.height * 0.55f))
    drawRect(color = color, topLeft = Offset(size.width * 0.3f, size.height * 0.2f), size = Size(size.width * 0.4f, size.height * 0.1f))
}

fun DrawScope.drawBusinessIcon(color: Color) {
    drawRoundRect(color = color, topLeft = Offset(size.width * 0.1f, size.height * 0.4f), size = Size(size.width * 0.8f, size.height * 0.4f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.05f))
    drawRect(color = color, topLeft = Offset(size.width * 0.3f, size.height * 0.2f), size = Size(size.width * 0.4f, size.height * 0.2f))
    drawRect(color = Color.White, topLeft = Offset(size.width * 0.45f, size.height * 0.55f), size = Size(size.width * 0.1f, size.height * 0.05f))
}

fun DrawScope.drawHealthIcon(color: Color) {
    val path = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.8f)
        cubicTo(size.width * 0.2f, size.height * 0.6f, size.width * 0.2f, size.height * 0.3f, size.width * 0.35f, size.height * 0.3f)
        cubicTo(size.width * 0.4f, size.height * 0.2f, size.width * 0.6f, size.height * 0.2f, size.width * 0.65f, size.height * 0.3f)
        cubicTo(size.width * 0.8f, size.height * 0.3f, size.width * 0.8f, size.height * 0.6f, size.width * 0.5f, size.height * 0.8f)
        close()
    }
    drawPath(path = path, color = color)
}

fun DrawScope.drawArrowBackIcon(color: Color) {
    val path = Path().apply {
        moveTo(size.width * 0.6f, size.height * 0.2f)
        lineTo(size.width * 0.2f, size.height * 0.5f)
        lineTo(size.width * 0.6f, size.height * 0.8f)
    }
    drawPath(path = path, color = color, style = Stroke(width = size.width * 0.1f, cap = androidx.compose.ui.graphics.StrokeCap.Round))
}

fun DrawScope.drawArrowForwardIcon(color: Color) {
    val path = Path().apply {
        moveTo(size.width * 0.4f, size.height * 0.2f)
        lineTo(size.width * 0.8f, size.height * 0.5f)
        lineTo(size.width * 0.4f, size.height * 0.8f)
    }
    drawPath(path = path, color = color, style = Stroke(width = size.width * 0.1f, cap = androidx.compose.ui.graphics.StrokeCap.Round))
}
