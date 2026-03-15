package com.dinnerspinner.app.ui.spinner

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinnerspinner.app.ui.theme.CasinoGold
import com.dinnerspinner.app.ui.theme.CasinoReelBg
import com.dinnerspinner.app.ui.theme.CasinoReelDim
import com.dinnerspinner.app.ui.theme.CasinoReelNear
import kotlin.math.abs
import kotlin.math.roundToInt

private const val ITEM_HEIGHT_DP = 56
private const val VISIBLE_ITEMS = 3

@Composable
fun SlotMachineReel(
    meals: List<String>,
    isSpinning: Boolean,
    targetIndex: Int,
    onSpinComplete: () -> Unit,
    onSwipeDown: () -> Unit,
    onTickHaptic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalHeightDp = (ITEM_HEIGHT_DP * VISIBLE_ITEMS).dp

    val offsetAnim = remember { Animatable(0f) }
    var currentOffset by remember { mutableStateOf(0f) }
    var lastTickIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(isSpinning, targetIndex) {
        if (!isSpinning || meals.isEmpty()) return@LaunchedEffect

        val extraSpins = (3..6).random() * meals.size
        val targetOffset = (targetIndex + extraSpins).toFloat()
        val spinDurationMs = (2500..3500).random()

        offsetAnim.animateTo(
            targetValue = targetOffset,
            animationSpec = tween(
                durationMillis = spinDurationMs,
                easing = FastOutSlowInEasing
            )
        ) {
            currentOffset = value
            val idx = (value.roundToInt().mod(meals.size) + meals.size).mod(meals.size)
            if (idx != lastTickIndex) {
                lastTickIndex = idx
                onTickHaptic()
            }
        }

        onSpinComplete()
    }

    val displayOffset = if (isSpinning) currentOffset else targetIndex.toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeightDp)
            .pointerInput(isSpinning) {
                detectVerticalDragGestures(
                    onDragEnd = { if (!isSpinning) onSwipeDown() }
                ) { _, _ -> }
            }
            .drawBehind {
                // Background fill
                drawRect(color = CasinoReelBg)

                val itemHeightPx = ITEM_HEIGHT_DP.dp.toPx()
                val centerY = size.height / 2f

                // Center highlight band
                drawRect(
                    color = CasinoGold.copy(alpha = 0.05f),
                    topLeft = Offset(0f, centerY - itemHeightPx / 2f),
                    size = androidx.compose.ui.geometry.Size(size.width, itemHeightPx)
                )

                // Center band border lines
                drawLine(
                    color = CasinoGold.copy(alpha = 0.3f),
                    start = Offset(0f, centerY - itemHeightPx / 2f),
                    end = Offset(size.width, centerY - itemHeightPx / 2f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = CasinoGold.copy(alpha = 0.3f),
                    start = Offset(0f, centerY + itemHeightPx / 2f),
                    end = Offset(size.width, centerY + itemHeightPx / 2f),
                    strokeWidth = 1.dp.toPx()
                )

                // Top gradient overlay
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to CasinoReelBg,
                        0.35f to Color.Transparent
                    ),
                    size = androidx.compose.ui.geometry.Size(size.width, size.height / 2f)
                )

                // Bottom gradient overlay
                drawRect(
                    brush = Brush.verticalGradient(
                        0.65f to Color.Transparent,
                        1f to CasinoReelBg
                    )
                )
            }
    ) {
        if (meals.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(totalHeightDp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    "Add some meals first!",
                    style = TextStyle(
                        color = CasinoReelDim,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Default
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val centerItemIndex = VISIBLE_ITEMS / 2
            repeat(VISIBLE_ITEMS) { slot ->
                val relativeIndex = slot - centerItemIndex
                val rawIndex = (displayOffset.roundToInt() + relativeIndex).mod(meals.size)
                val index = ((rawIndex % meals.size) + meals.size) % meals.size
                val name = meals[index]

                val distFromCenter = abs(relativeIndex)
                val textColor = when (distFromCenter) {
                    0 -> CasinoGold
                    1 -> CasinoReelNear
                    else -> CasinoReelDim
                }
                val alpha = when (distFromCenter) {
                    0 -> 1f
                    1 -> 0.75f
                    else -> 0.35f
                }
                val fontSize = when (distFromCenter) {
                    0 -> 18.sp
                    1 -> 14.sp
                    else -> 12.sp
                }
                val fontWeight = if (distFromCenter == 0) FontWeight.Bold else FontWeight.SemiBold
                val letterSpacing = if (distFromCenter == 0) 1.5.sp else 1.sp

                val yOffset = (slot * ITEM_HEIGHT_DP).dp

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ITEM_HEIGHT_DP.dp)
                        .offset { IntOffset(0, yOffset.roundToPx()) }
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = name,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = fontWeight,
                            fontSize = fontSize,
                            letterSpacing = letterSpacing,
                            color = textColor
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}
