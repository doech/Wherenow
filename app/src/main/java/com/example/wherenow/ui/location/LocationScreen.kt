package com.example.wherenow.ui.location

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.wherenow.R
import com.example.wherenow.navigation.NavRoutes

@Composable
fun LocationScreen(navController: NavController) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB388FF),
            Color(0xFFFF80AB)
        )
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState) // Added vertical scroll capability
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Location Pin Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Color(0xFF9C27B0).copy(alpha = 0.1f),
                            RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(40.dp)) {
                        drawLocationPin(this, Color(0xFF9C27B0))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = stringResource(R.string.enable_location),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = stringResource(R.string.location_full_message),
                    fontSize = 16.sp,
                    color = Color(0xFF718096),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Benefits List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    BenefitItem(
                        icon = { drawTargetIcon(it, Color(0xFFFF9800)) },
                        title = stringResource(R.string.personalized_recommendations),
                        description = stringResource(R.string.personalized_recommendations_desc),
                        iconColor = Color(0xFFFF9800)
                    )

                    BenefitItem(
                        icon = { drawCalendarIcon(it, Color(0xFF9C27B0)) },
                        title = stringResource(R.string.nearby_events),
                        description = stringResource(R.string.nearby_events_desc),
                        iconColor = Color(0xFF9C27B0)
                    )

                    BenefitItem(
                        icon = { drawPeopleIcon(it, Color(0xFFFF9800)) },
                        title = stringResource(R.string.connect_with_others),
                        description = stringResource(R.string.connect_with_others_desc),
                        iconColor = Color(0xFFFF9800)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Privacy Note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawShieldIcon(this, Color(0xFF9C27B0))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stringResource(R.string.privacy_matters),
                        fontSize = 14.sp,
                        color = Color(0xFF718096),
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Allow Location Button
                Button(
                    onClick = {
                        // Handle location permission request
                        navController.navigate(NavRoutes.QUIZ)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9C27B0)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawLocationPin(this, Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Allow Location Access",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Skip Button
                TextButton(
                    onClick = {
                        // Skip location permission
                        navController.navigate(NavRoutes.QUIZ)
                    }
                ) {
                    Text(
                        text = "Skip for now",
                        fontSize = 16.sp,
                        color = Color(0xFF718096)
                    )
                }
            }
        }
    }
}

@Composable
fun BenefitItem(
    icon: (DrawScope) -> Unit,
    title: String,
    description: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    iconColor.copy(alpha = 0.1f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                icon(this)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF718096),
                lineHeight = 20.sp
            )
        }
    }
}

// Custom Icon Drawing Functions
fun drawLocationPin(drawScope: DrawScope, color: Color) {
    with(drawScope) {
        val center = size.center
        val radius = size.minDimension * 0.15f

        // Draw pin shape
        val path = Path().apply {
            moveTo(center.x, center.y + radius * 2)
            lineTo(center.x - radius * 1.2f, center.y - radius * 0.5f)
            arcTo(
                rect = Rect(
                    center.x - radius * 1.2f,
                    center.y - radius * 2.5f,
                    center.x + radius * 1.2f,
                    center.y + radius * 0.5f
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }

        drawPath(path, color)

        // Draw inner circle
        drawCircle(
            color = Color.White,
            radius = radius * 0.6f,
            center = Offset(center.x, center.y - radius * 0.8f)
        )
    }
}

fun drawTargetIcon(drawScope: DrawScope, color: Color) {
    with(drawScope) {
        val center = size.center
        val radius = size.minDimension * 0.3f

        // Draw concentric circles
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )

        drawCircle(
            color = color,
            radius = radius * 0.6f,
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )

        drawCircle(
            color = color,
            radius = radius * 0.2f,
            center = center
        )

        // Draw arrow
        val arrowPath = Path().apply {
            moveTo(center.x + radius * 0.7f, center.y - radius * 0.7f)
            lineTo(center.x + radius * 1.2f, center.y - radius * 1.2f)
            lineTo(center.x + radius * 1.0f, center.y - radius * 1.4f)
            lineTo(center.x + radius * 1.4f, center.y - radius * 1.0f)
            close()
        }

        drawPath(arrowPath, color)
    }
}

fun drawCalendarIcon(drawScope: DrawScope, color: Color) {
    with(drawScope) {
        val center = size.center
        val width = size.width * 0.6f
        val height = size.height * 0.6f

        // Draw calendar body
        drawRoundRect(
            color = color,
            topLeft = Offset(center.x - width/2, center.y - height/2 + height * 0.15f),
            size = Size(width, height * 0.85f),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Draw calendar header
        drawRoundRect(
            color = color.copy(alpha = 0.8f),
            topLeft = Offset(center.x - width/2, center.y - height/2),
            size = Size(width, height * 0.25f),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Draw calendar rings
        drawCircle(
            color = Color.White,
            radius = 2.dp.toPx(),
            center = Offset(center.x - width * 0.25f, center.y - height * 0.35f)
        )

        drawCircle(
            color = Color.White,
            radius = 2.dp.toPx(),
            center = Offset(center.x + width * 0.25f, center.y - height * 0.35f)
        )

        // Draw calendar grid
        for (i in 1..2) {
            for (j in 1..3) {
                drawCircle(
                    color = Color.White,
                    radius = 1.5.dp.toPx(),
                    center = Offset(
                        center.x - width * 0.25f + (j - 1) * width * 0.25f,
                        center.y - height * 0.1f + (i - 1) * height * 0.15f
                    )
                )
            }
        }
    }
}

fun drawPeopleIcon(drawScope: DrawScope, color: Color) {
    with(drawScope) {
        val center = size.center
        val radius = size.minDimension * 0.12f

        // Draw first person
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(center.x - radius * 0.8f, center.y - radius * 0.5f)
        )

        drawCircle(
            color = color,
            radius = radius * 1.2f,
            center = Offset(center.x - radius * 0.8f, center.y + radius * 1.2f),
            style = Stroke(width = radius * 0.4f)
        )

        // Draw second person
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(center.x + radius * 0.8f, center.y - radius * 0.5f)
        )

        drawCircle(
            color = color,
            radius = radius * 1.2f,
            center = Offset(center.x + radius * 0.8f, center.y + radius * 1.2f),
            style = Stroke(width = radius * 0.4f)
        )
    }
}

fun drawShieldIcon(drawScope: DrawScope, color: Color) {
    with(drawScope) {
        val center = size.center
        val width = size.width * 0.6f
        val height = size.height * 0.7f

        val path = Path().apply {
            moveTo(center.x, center.y - height/2)
            lineTo(center.x - width/2, center.y - height/3)
            lineTo(center.x - width/2, center.y + height/6)
            lineTo(center.x, center.y + height/2)
            lineTo(center.x + width/2, center.y + height/6)
            lineTo(center.x + width/2, center.y - height/3)
            close()
        }

        drawPath(path, color)

        // Draw checkmark
        val checkPath = Path().apply {
            moveTo(center.x - width * 0.2f, center.y)
            lineTo(center.x - width * 0.05f, center.y + height * 0.15f)
            lineTo(center.x + width * 0.25f, center.y - height * 0.1f)
        }

        drawPath(
            checkPath,
            Color.White,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}