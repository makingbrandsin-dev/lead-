package com.example.ui.components.mascot

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

enum class LionPose {
    WAVING,
    POINTING,
    HOLDING_TABLET,
    MAGNIFYING_GLASS,
    THUMBS_UP,
    CELEBRATING
}

@Composable
fun LionMascot(
    pose: LionPose = LionPose.WAVING,
    modifier: Modifier = Modifier.size(220.dp)
) {
    // Infinite animation for subtle float & arm wave
    val infiniteTransition = rememberInfiniteTransition(label = "lionAnim")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )
    val waveAngle by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f + bounceY

        // Colors matching Making Brands Mascot
        val maneColor = Color(0xFFB45309) // Warm brown lion mane
        val maneLight = Color(0xFFD97706)
        val furColor = Color(0xFFF59E0B)  // Golden lion fur
        val furLight = Color(0xFFFBBF24)
        val snoutColor = Color(0xFFFFFBEB) // Off-white snout
        val tealShirt = Color(0xFF0D9488) // Making Brands teal polo shirt
        val tealDark = Color(0xFF0F766E)
        val khakiPants = Color(0xFFD97706) // Khaki shorts
        val shoeColor = Color(0xFF78350F)
        val darkColor = Color(0xFF1E293B)
        val whiteColor = Color.White

        // 1. Draw Feet / Shoes
        val footY = height * 0.88f
        // Left Shoe
        drawRoundRect(
            color = shoeColor,
            topLeft = Offset(centerX - width * 0.22f, footY),
            size = Size(width * 0.18f, height * 0.08f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = whiteColor,
            topLeft = Offset(centerX - width * 0.22f, footY + height * 0.05f),
            size = Size(width * 0.18f, height * 0.03f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        // Right Shoe
        drawRoundRect(
            color = shoeColor,
            topLeft = Offset(centerX + width * 0.04f, footY),
            size = Size(width * 0.18f, height * 0.08f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = whiteColor,
            topLeft = Offset(centerX + width * 0.04f, footY + height * 0.05f),
            size = Size(width * 0.18f, height * 0.03f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // 2. Draw Legs (Khaki shorts)
        drawRoundRect(
            color = khakiPants,
            topLeft = Offset(centerX - width * 0.22f, centerY + height * 0.22f),
            size = Size(width * 0.44f, height * 0.16f),
            cornerRadius = CornerRadius(14f, 14f)
        )
        // Tail
        drawPath(
            path = Path().apply {
                moveTo(centerX - width * 0.18f, centerY + height * 0.26f)
                cubicTo(
                    centerX - width * 0.35f, centerY + height * 0.28f,
                    centerX - width * 0.38f, centerY + height * 0.38f,
                    centerX - width * 0.30f, centerY + height * 0.42f
                )
            },
            color = furColor,
            style = Stroke(width = 12f, cap = StrokeCap.Round)
        )
        drawCircle(
            color = maneColor,
            radius = 14f,
            center = Offset(centerX - width * 0.30f, centerY + height * 0.42f)
        )

        // 3. Draw Body (Teal Polo T-Shirt)
        val bodyRect = Path().apply {
            moveTo(centerX - width * 0.25f, centerY - height * 0.02f)
            lineTo(centerX + width * 0.25f, centerY - height * 0.02f)
            lineTo(centerX + width * 0.22f, centerY + height * 0.24f)
            lineTo(centerX - width * 0.22f, centerY + height * 0.24f)
            close()
        }
        drawPath(
            path = bodyRect,
            brush = Brush.linearGradient(listOf(tealShirt, tealDark))
        )

        // Shirt Collar V-neck
        drawPath(
            path = Path().apply {
                moveTo(centerX - width * 0.08f, centerY - height * 0.02f)
                lineTo(centerX, centerY + height * 0.06f)
                lineTo(centerX + width * 0.08f, centerY - height * 0.02f)
            },
            color = whiteColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        // Making Brands Logo Emblem on Shirt Chest
        val chestLogoCenter = Offset(centerX - width * 0.08f, centerY + height * 0.08f)
        drawCircle(color = whiteColor.copy(alpha = 0.9f), radius = 10f, center = chestLogoCenter)
        drawCircle(color = tealShirt, radius = 6f, center = chestLogoCenter)

        // 4. Arms & Pose Specific Props
        when (pose) {
            LionPose.WAVING -> {
                // Left Waving Arm
                rotate(degrees = waveAngle, pivot = Offset(centerX - width * 0.22f, centerY + height * 0.02f)) {
                    drawPath(
                        path = Path().apply {
                            moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                            lineTo(centerX - width * 0.38f, centerY - height * 0.12f)
                        },
                        color = furColor,
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )
                    // Paw
                    drawCircle(color = furLight, radius = 16f, center = Offset(centerX - width * 0.38f, centerY - height * 0.12f))
                }
                // Right Arm at hip
                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX + width * 0.30f, centerY + height * 0.12f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
            }
            LionPose.POINTING -> {
                // Right Arm Pointing Up / Right
                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX + width * 0.42f, centerY - height * 0.08f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                // Hand pointing index finger
                drawCircle(color = furLight, radius = 16f, center = Offset(centerX + width * 0.42f, centerY - height * 0.08f))
                // Left arm resting
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX - width * 0.28f, centerY + height * 0.14f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
            }
            LionPose.HOLDING_TABLET -> {
                // Both arms holding a slate tablet
                val tabX = centerX + width * 0.15f
                val tabY = centerY + height * 0.08f
                drawRoundRect(
                    color = Color(0xFF1E293B),
                    topLeft = Offset(tabX - 30f, tabY - 45f),
                    size = Size(75f, 95f),
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawRoundRect(
                    color = Color(0xFF38BDF8),
                    topLeft = Offset(tabX - 25f, tabY - 40f),
                    size = Size(65f, 85f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Arm connecting to tablet
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                        lineTo(tabX - 20f, tabY)
                    },
                    color = furColor,
                    style = Stroke(width = 22f, cap = StrokeCap.Round)
                )
                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(tabX + 40f, tabY)
                    },
                    color = furColor,
                    style = Stroke(width = 22f, cap = StrokeCap.Round)
                )
            }
            LionPose.MAGNIFYING_GLASS -> {
                // Right arm holding magnifying glass
                val magX = centerX + width * 0.30f
                val magY = centerY - height * 0.05f
                drawCircle(color = darkColor, radius = 28f, center = Offset(magX, magY), style = Stroke(width = 8f))
                drawCircle(color = Color(0xFF38BDF8).copy(alpha = 0.5f), radius = 24f, center = Offset(magX, magY))
                drawLine(color = darkColor, start = Offset(magX - 18f, magY + 18f), end = Offset(magX - 35f, magY + 35f), strokeWidth = 10f)

                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(magX - 25f, magY + 25f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX - width * 0.28f, centerY + height * 0.12f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
            }
            LionPose.THUMBS_UP -> {
                // Right Arm Thumbs Up
                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX + width * 0.36f, centerY - height * 0.04f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                // Thumbs up hand
                drawCircle(color = furLight, radius = 16f, center = Offset(centerX + width * 0.36f, centerY - height * 0.04f))
                drawRoundRect(
                    color = furLight,
                    topLeft = Offset(centerX + width * 0.34f, centerY - height * 0.12f),
                    size = Size(10f, 22f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
                // Left arm at waist
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX - width * 0.28f, centerY + height * 0.14f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
            }
            LionPose.CELEBRATING -> {
                // Both arms up high!
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX - width * 0.36f, centerY - height * 0.18f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                drawCircle(color = furLight, radius = 16f, center = Offset(centerX - width * 0.36f, centerY - height * 0.18f))

                drawPath(
                    path = Path().apply {
                        moveTo(centerX + width * 0.22f, centerY + height * 0.02f)
                        lineTo(centerX + width * 0.36f, centerY - height * 0.18f)
                    },
                    color = furColor,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                drawCircle(color = furLight, radius = 16f, center = Offset(centerX + width * 0.36f, centerY - height * 0.18f))
            }
        }

        // 5. Draw Lion Fluffy Mane
        val headCenterY = centerY - height * 0.22f
        val maneRadius = width * 0.32f

        // Layered Mane spikes
        for (i in 0 until 12) {
            val angle = (i * 30f) * (Math.PI / 180f).toFloat()
            val spikeX = centerX + (maneRadius + 12f) * Math.cos(angle.toDouble()).toFloat()
            val spikeY = headCenterY + (maneRadius + 12f) * Math.sin(angle.toDouble()).toFloat()
            drawCircle(color = maneColor, radius = 24f, center = Offset(spikeX, spikeY))
        }
        drawCircle(color = maneLight, radius = maneRadius, center = Offset(centerX, headCenterY))

        // 6. Draw Ears
        val leftEar = Offset(centerX - width * 0.22f, headCenterY - height * 0.15f)
        val rightEar = Offset(centerX + width * 0.22f, headCenterY - height * 0.15f)
        drawCircle(color = maneColor, radius = 24f, center = leftEar)
        drawCircle(color = Color(0xFFFDE68A), radius = 14f, center = leftEar)
        drawCircle(color = maneColor, radius = 24f, center = rightEar)
        drawCircle(color = Color(0xFFFDE68A), radius = 14f, center = rightEar)

        // 7. Head Base (Golden Lion Fur)
        val headRadius = width * 0.24f
        drawCircle(color = furColor, radius = headRadius, center = Offset(centerX, headCenterY))

        // 8. Snout & Nose
        val snoutY = headCenterY + 12f
        drawOval(
            color = snoutColor,
            topLeft = Offset(centerX - width * 0.14f, snoutY - 18f),
            size = Size(width * 0.28f, height * 0.14f)
        )
        // Cute Nose (Dark Brown)
        val nosePath = Path().apply {
            moveTo(centerX - 16f, snoutY - 12f)
            lineTo(centerX + 16f, snoutY - 12f)
            lineTo(centerX, snoutY + 8f)
            close()
        }
        drawPath(path = nosePath, color = Color(0xFF78350F))

        // Cheerful Smile Line
        drawPath(
            path = Path().apply {
                moveTo(centerX - 18f, snoutY + 16f)
                quadraticTo(centerX, snoutY + 28f, centerX + 18f, snoutY + 16f)
            },
            color = darkColor,
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )

        // 9. Big Friendly Cartoon Eyes
        val leftEye = Offset(centerX - width * 0.09f, headCenterY - 14f)
        val rightEye = Offset(centerX + width * 0.09f, headCenterY - 14f)

        // Eye whites
        drawCircle(color = whiteColor, radius = 16f, center = leftEye)
        drawCircle(color = whiteColor, radius = 16f, center = rightEye)
        // Pupils
        drawCircle(color = darkColor, radius = 10f, center = Offset(leftEye.x + 2f, leftEye.y))
        drawCircle(color = darkColor, radius = 10f, center = Offset(rightEye.x + 2f, rightEye.y))
        // Pupil sparkles
        drawCircle(color = whiteColor, radius = 4f, center = Offset(leftEye.x + 5f, leftEye.y - 4f))
        drawCircle(color = whiteColor, radius = 4f, center = Offset(rightEye.x + 5f, rightEye.y - 4f))

        // Eyebrows
        drawPath(
            path = Path().apply {
                moveTo(leftEye.x - 12f, leftEye.y - 24f)
                quadraticTo(leftEye.x, leftEye.y - 30f, leftEye.x + 12f, leftEye.y - 24f)
            },
            color = maneColor,
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )
        drawPath(
            path = Path().apply {
                moveTo(rightEye.x - 12f, rightEye.y - 24f)
                quadraticTo(rightEye.x, rightEye.y - 30f, rightEye.x + 12f, rightEye.y - 24f)
            },
            color = maneColor,
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )
    }
}
