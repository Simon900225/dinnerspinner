package com.dinnerspinner.app.ui.plan

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dinnerspinner.app.ui.components.MealFilterPanel

private val DAY_NAMES = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(viewModel: MealPlanViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meal Plan") },
                actions = {
                    if (uiState.isGenerated) {
                        IconButton(onClick = {
                            val text = viewModel.getShareText()
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                                putExtra(Intent.EXTRA_SUBJECT, "My Dinner Plan")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share meal plan"))
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Filter card
            item {
                MealFilterPanel(
                    filter = uiState.filter,
                    isExpanded = uiState.isFilterExpanded,
                    onToggleExpand = { viewModel.toggleFilterExpanded() },
                    onSetMaxComplexity = { viewModel.setMaxComplexity(it) },
                    onSetMaxPrice = { viewModel.setMaxPrice(it) },
                    onToggleProtein = { viewModel.toggleProtein(it) },
                    onToggleStaple = { viewModel.toggleStaple(it) },
                    onSetMaxNutrition = { viewModel.setMaxNutrition(it) },
                    onSetMaxCookingTime = { viewModel.setMaxCookingTime(it) },
                    onSetAvoidRepeats = { viewModel.setAvoidRepeats(it) },
                    onSetExcludeRecentlyEaten = { viewModel.setExcludeRecentlyEaten(it) },
                    onSetExcludeWithinDays = { viewModel.setExcludeWithinDays(it) }
                )
            }

            // Generate button
            item {
                Button(
                    onClick = { viewModel.generatePlan() },
                    enabled = uiState.allMeals.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (uiState.isGenerated) "Regenerate Plan" else "Generate Plan",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Plan entries
            if (uiState.isGenerated && uiState.planEntries.isNotEmpty()) {
                items(uiState.planEntries) { entry ->
                    PlanDayCard(
                        dayName = DAY_NAMES[entry.dayOfWeek - 1],
                        mealName = entry.mealName,
                        isLocked = entry.isLocked,
                        onReroll = { viewModel.rerollDay(entry.dayOfWeek) },
                        onLockToggle = { viewModel.toggleLockDay(entry.dayOfWeek) }
                    )
                }
            }

            // Empty state
            if (uiState.allMeals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Add meals first",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Add some meals in My Meals to generate a weekly plan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun PlanDayCard(
    dayName: String,
    mealName: String,
    isLocked: Boolean,
    onReroll: () -> Unit,
    onLockToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = if (isLocked)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        else
            CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                dayName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(40.dp)
            )
            Text(
                mealName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            IconButton(
                onClick = onReroll,
                enabled = !isLocked,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Re-roll",
                    modifier = Modifier.size(18.dp),
                    tint = if (isLocked)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(
                onClick = onLockToggle,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = if (isLocked) "Unlock" else "Lock",
                    modifier = Modifier.size(18.dp),
                    tint = if (isLocked)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
