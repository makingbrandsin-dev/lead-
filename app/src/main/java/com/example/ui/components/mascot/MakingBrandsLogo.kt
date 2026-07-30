package com.example.ui.components.mascot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MakingBrandsLogoHeader(
    modifier: Modifier = Modifier,
    logoSize: Dp = 48.dp,
    textColor: Color = Color.White,
    subtitle: String? = "Smart CRM for Smarter Businesses"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Shield Emblem
        Canvas(modifier = Modifier.size(logoSize)) {
            val w = size.width
            val h = size.height
            val strokeWidth = w * 0.14f

            val shieldPath = Path().apply {
                moveTo(w * 0.5f, h * 0.08f)
                lineTo(w * 0.88f, h * 0.28f)
                lineTo(w * 0.88f, h * 0.62f)
                cubicTo(w * 0.88f, h * 0.82f, w * 0.70f, h * 0.94f, w * 0.5f, h * 0.98f)
                cubicTo(w * 0.30f, h * 0.94f, w * 0.12f, h * 0.82f, w * 0.12f, h * 0.62f)
                lineTo(w * 0.12f, h * 0.28f)
                close()
            }
            drawPath(
                path = shieldPath,
                color = textColor,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Inner "M B" Geometric V
            val vPath = Path().apply {
                moveTo(w * 0.32f, h * 0.40f)
                lineTo(w * 0.5f, h * 0.65f)
                lineTo(w * 0.68f, h * 0.40f)
            }
            drawPath(
                path = vPath,
                color = textColor,
                style = Stroke(width = strokeWidth * 0.9f, cap = StrokeCap.Round)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Text "making brands"
        Text(
            text = "making",
            color = textColor,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "brands",
            color = textColor,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
        )

        subtitle?.let { sub ->
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = sub,
                color = textColor.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
