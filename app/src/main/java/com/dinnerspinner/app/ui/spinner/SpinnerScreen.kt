package com.dinnerspinner.app.ui.spinner

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dinnerspinner.app.ui.theme.CasinoBlack
import com.dinnerspinner.app.ui.theme.CasinoBorder
import com.dinnerspinner.app.ui.theme.CasinoCardBg
import com.dinnerspinner.app.ui.theme.CasinoGold
import com.dinnerspinner.app.ui.theme.CasinoGoldDark
import com.dinnerspinner.app.ui.theme.CasinoGoldDim
import com.dinnerspinner.app.ui.theme.CasinoGoldVeryDark
import com.dinnerspinner.app.ui.theme.CasinoGreen
import com.dinnerspinner.app.ui.theme.CasinoReelBg

private val GoldBorderDim = Color(0xFF5A4010)
private val RerollBorderColor = Color(0xFF5A4010)
private val RerollTextColor = Color(0xFF8A6A30)
private val AttrChipBg = Color(0xFF1A1408)
private val AttrChipBorder = Color(0xFF3A2A08)
private val AttrChipText = Color(0xFF8A6A30)
private val AttrChipHighlightBorder = Color(0xFF6A5020)
private val AttrChipHighlightText = Color(0xFFC09040)
private val SpinGradientEdge = Color(0xFFC8890A)
private val SpinTextColor = Color(0xFF1A0E00)
private val DisabledSpinBg = Color(0xFF2A1E00)
private val DisabledSpinText = Color(0xFF5A4030)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpinnerScreen(viewModel: SpinnerViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService<Vibrator>() }

    var targetIndex by remember { mutableIntStateOf(0) }
    var isSpinning by remember { mutableStateOf(false) }
    val mealNames = uiState.filteredMeals.map { it.name }
    val hasResult = uiState.spinState == SpinState.RESULT && uiState.selectedMeal != null
    val spinEnabled = !isSpinning && mealNames.isNotEmpty()

    fun triggerSpin() {
        if (isSpinning || mealNames.isEmpty()) return
        val meal = viewModel.pickRandomMeal() ?: return
        val idx = mealNames.indexOf(meal.name).coerceAtLeast(0)
        targetIndex = idx
        isSpinning = true
        viewModel.setSpinState(SpinState.SPINNING)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBlack),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        // ── Header ──────────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            // ★ ★ ★  DINNER  ★ ★ ★
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) {
                    Text("★", color = CasinoGold, fontSize = 10.sp)
                    Spacer(Modifier.width(2.dp))
                }
                Text(
                    "DINNER",
                    color = CasinoGoldDim,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                repeat(3) {
                    Spacer(Modifier.width(2.dp))
                    Text("★", color = CasinoGold, fontSize = 10.sp)
                }
            }

            Spacer(Modifier.height(2.dp))

            // SPINNER title
            Text(
                "SPINNER",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp,
                    letterSpacing = 4.sp,
                    color = CasinoGold,
                    shadow = Shadow(
                        color = CasinoGold.copy(alpha = 0.45f),
                        offset = Offset(0f, 0f),
                        blurRadius = 24f
                    )
                )
            )

            // Subtitle
            Text(
                "LAS VEGAS — EST. TONIGHT",
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Light,
                    fontSize = 9.sp,
                    letterSpacing = 2.5.sp,
                    color = CasinoGoldDark
                )
            )
        }

        // Gold gradient divider
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            CasinoGoldDim.copy(alpha = 0.5f),
                            CasinoGold,
                            CasinoGoldDim.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        // ── Filter chips ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CasinoFilterChip("⚡ Quick", uiState.filter.quickOnly) { viewModel.toggleQuickFilter() }
            CasinoFilterChip("🥩 Meat", uiState.filter.meatOnly) { viewModel.toggleMeatFilter() }
            CasinoFilterChip("🐟 Fish", uiState.filter.fishOnly) { viewModel.toggleFishFilter() }
            CasinoFilterChip("💰 Cheap", uiState.filter.cheapOnly) { viewModel.toggleCheapFilter() }
            CasinoFilterChip("🥗 Healthy", uiState.filter.healthyOnly) { viewModel.toggleHealthyFilter() }
        }

        // ── Machine frame ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .border(2.dp, GoldBorderDim, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(CasinoCardBg)
                .padding(10.dp)
        ) {
            Column {
                // Light bulbs row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val bulbColors = listOf(
                        CasinoGold, Color(0xFFE05050), Color(0xFF50C050),
                        CasinoGold, Color(0xFFE05050), Color(0xFF50C050),
                        CasinoGold, Color(0xFFE05050), Color(0xFF50C050),
                        CasinoGold, Color(0xFFE05050), Color(0xFF50C050),
                    )
                    bulbColors.forEach { color ->
                        Box(
                            Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Reel window
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, CasinoBorder, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(CasinoReelBg)
                ) {
                    SlotMachineReel(
                        meals = mealNames,
                        isSpinning = isSpinning,
                        targetIndex = targetIndex,
                        onSpinComplete = {
                            isSpinning = false
                            val meal = uiState.filteredMeals.getOrNull(targetIndex)
                            if (meal != null) {
                                viewModel.onSpinComplete(meal)
                                vibrator?.vibrate(
                                    VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
                                )
                            }
                        },
                        onSwipeDown = { triggerSpin() },
                        onTickHaptic = {
                            vibrator?.vibrate(VibrationEffect.createOneShot(10, 40))
                        }
                    )
                }

                // Result card (inside machine frame)
                AnimatedVisibility(
                    visible = hasResult,
                    enter = fadeIn() + expandVertically(spring()),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    val meal = uiState.selectedMeal
                    if (meal != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .border(1.dp, Color(0xFF4A3810), RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF1E1508), Color(0xFF0E0B04))
                                    )
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Column {
                                // "TONIGHT'S WINNER" badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(CasinoGreen)
                                    )
                                    Text(
                                        "TONIGHT'S WINNER",
                                        style = TextStyle(
                                            fontSize = 11.sp,
                                            letterSpacing = 2.sp,
                                            color = CasinoGoldDim,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                // Meal name
                                Text(
                                    meal.name,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = CasinoGold
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // Attribute chips
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    meal.cookingTime?.let {
                                        val isQuick = it.name == "QUICK"
                                        CasinoAttrChip(
                                            if (isQuick) "⚡ Quick" else it.label,
                                            highlight = isQuick && uiState.filter.quickOnly
                                        )
                                    }
                                    meal.price?.let {
                                        val isCheap = it.name == "CHEAP"
                                        CasinoAttrChip(
                                            if (isCheap) "💰 Cheap" else it.label,
                                            highlight = isCheap && uiState.filter.cheapOnly
                                        )
                                    }
                                    meal.protein?.let {
                                        val icon = when (it.name) {
                                            "FISH" -> "🐟 "
                                            "MEAT" -> "🥩 "
                                            "VEGETARIAN" -> "🥗 "
                                            "VEGAN" -> "🌱 "
                                            else -> ""
                                        }
                                        CasinoAttrChip("$icon${it.label}")
                                    }
                                    meal.staple?.let { CasinoAttrChip(it.label) }
                                    meal.nutrition?.let {
                                        val icon = when (it.name) {
                                            "HEALTHY" -> "🥗 "
                                            "JUNK" -> "🍔 "
                                            else -> ""
                                        }
                                        CasinoAttrChip("$icon${it.label}")
                                    }
                                    if (meal.isFavorite) {
                                        CasinoAttrChip("★ Favorite")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Empty states ──────────────────────────────────────────────────────
        if (uiState.allMeals.isEmpty()) {
            Spacer(Modifier.height(24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    "ADD SOME MEALS FIRST",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        color = CasinoGoldDark,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Go to Meals and add your favourite dishes to spin",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = CasinoGoldVeryDark,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        } else if (uiState.filteredMeals.isEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                "No meals match your filters.\nTry removing some.",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = CasinoGoldVeryDark,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.height(12.dp))

        // ── Action buttons ────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // NOT THIS ONE
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .border(
                        1.dp,
                        if (hasResult) RerollBorderColor else CasinoBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
                    .clickable(enabled = hasResult) { viewModel.reroll() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "NOT THIS ONE",
                    style = TextStyle(
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (hasResult) RerollTextColor else CasinoBorder
                    )
                )
            }

            // SPIN
            Box(
                modifier = Modifier
                    .weight(2.5f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = if (spinEnabled) {
                            Brush.horizontalGradient(
                                listOf(SpinGradientEdge, CasinoGold, SpinGradientEdge)
                            )
                        } else {
                            Brush.horizontalGradient(
                                listOf(DisabledSpinBg, DisabledSpinBg)
                            )
                        }
                    )
                    .clickable(enabled = spinEnabled) { triggerSpin() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isSpinning) "SPINNING..." else "S P I N",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 2.sp,
                        color = if (spinEnabled) SpinTextColor else DisabledSpinText
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CasinoFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                label,
                style = TextStyle(
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFF1A1208),
            labelColor = CasinoGoldDim,
            selectedContainerColor = Color(0xFF3A2A08),
            selectedLabelColor = CasinoGold,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = Color(0xFF6A4E18),
            selectedBorderColor = CasinoGold,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp,
        )
    )
}

@Composable
private fun CasinoAttrChip(label: String, highlight: Boolean = false) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                if (highlight) AttrChipHighlightBorder else AttrChipBorder,
                RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .background(AttrChipBg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = 9.sp,
                letterSpacing = 0.8.sp,
                color = if (highlight) AttrChipHighlightText else AttrChipText
            )
        )
    }
}
