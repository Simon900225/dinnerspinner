package com.dinnerspinner.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple
import com.dinnerspinner.app.data.static.CommonDish

data class MealFilter(
    val maxComplexity: Complexity? = null,
    val maxPrice: Price? = null,
    val proteins: Set<Protein> = emptySet(),
    val staples: Set<Staple> = emptySet(),
    val maxNutrition: Nutrition? = null,
    val maxCookingTime: CookingTime? = null,
    val avoidRepeats: Boolean = true,
    val excludeRecentlyEaten: Boolean = false,
    val excludeWithinDays: Int = 7
) {
    val isActive: Boolean get() = maxComplexity != null || maxPrice != null ||
        proteins.isNotEmpty() || staples.isNotEmpty() ||
        maxNutrition != null || maxCookingTime != null

    fun matches(dish: CommonDish): Boolean {
        maxComplexity?.let { max ->
            if (dish.complexity != null && dish.complexity.ordinal > max.ordinal) return false
        }
        maxPrice?.let { max ->
            if (dish.price != null && dish.price.ordinal > max.ordinal) return false
        }
        if (proteins.isNotEmpty()) {
            if (dish.protein == null || dish.protein !in proteins) return false
        }
        if (staples.isNotEmpty()) {
            if (dish.staple == null || dish.staple !in staples) return false
        }
        maxNutrition?.let { max ->
            if (dish.nutrition != null && dish.nutrition.ordinal > max.ordinal) return false
        }
        maxCookingTime?.let { max ->
            if (dish.cookingTime != null && dish.cookingTime.ordinal > max.ordinal) return false
        }
        return true
    }

    fun matches(meal: Meal): Boolean {
        maxComplexity?.let { max ->
            if (meal.complexity != null && meal.complexity.ordinal > max.ordinal) return false
        }
        maxPrice?.let { max ->
            if (meal.price != null && meal.price.ordinal > max.ordinal) return false
        }
        if (proteins.isNotEmpty()) {
            if (meal.protein == null || meal.protein !in proteins) return false
        }
        if (staples.isNotEmpty()) {
            if (meal.staple == null || meal.staple !in staples) return false
        }
        maxNutrition?.let { max ->
            if (meal.nutrition != null && meal.nutrition.ordinal > max.ordinal) return false
        }
        maxCookingTime?.let { max ->
            if (meal.cookingTime != null && meal.cookingTime.ordinal > max.ordinal) return false
        }
        return true
    }
}

/**
 * Collapsible filter panel card. Plan-specific options (avoidRepeats, excludeRecentlyEaten)
 * are only rendered when their respective callbacks are provided.
 */
@Composable
fun MealFilterPanel(
    filter: MealFilter,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSetMaxComplexity: (Complexity?) -> Unit,
    onSetMaxPrice: (Price?) -> Unit,
    onToggleProtein: (Protein) -> Unit,
    onToggleStaple: (Staple) -> Unit,
    onSetMaxNutrition: (Nutrition?) -> Unit,
    onSetMaxCookingTime: (CookingTime?) -> Unit,
    onSetAvoidRepeats: ((Boolean) -> Unit)? = null,
    onSetExcludeRecentlyEaten: ((Boolean) -> Unit)? = null,
    onSetExcludeWithinDays: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (filter.isActive) {
                        Text(
                            "●",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                IconButton(onClick = onToggleExpand) {
                    Icon(
                        if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    HorizontalDivider()

                    SingleSelectChips(
                        label = "Max complexity",
                        options = Complexity.entries,
                        selected = filter.maxComplexity,
                        labelFor = { it.label },
                        onSelect = onSetMaxComplexity
                    )

                    SingleSelectChips(
                        label = "Max price",
                        options = Price.entries,
                        selected = filter.maxPrice,
                        labelFor = { it.label },
                        onSelect = onSetMaxPrice
                    )

                    MultiSelectChips(
                        label = "Protein (any = no filter)",
                        options = Protein.entries,
                        selected = filter.proteins,
                        labelFor = { it.label },
                        onToggle = onToggleProtein
                    )

                    MultiSelectChips(
                        label = "Staple (any = no filter)",
                        options = Staple.entries,
                        selected = filter.staples,
                        labelFor = { it.label },
                        onToggle = onToggleStaple
                    )

                    SingleSelectChips(
                        label = "Max nutrition level",
                        options = Nutrition.entries,
                        selected = filter.maxNutrition,
                        labelFor = { it.label },
                        onSelect = onSetMaxNutrition
                    )

                    SingleSelectChips(
                        label = "Max cooking time",
                        options = CookingTime.entries,
                        selected = filter.maxCookingTime,
                        labelFor = { it.label },
                        onSelect = onSetMaxCookingTime
                    )

                    if (onSetAvoidRepeats != null || onSetExcludeRecentlyEaten != null) {
                        HorizontalDivider()
                    }

                    if (onSetAvoidRepeats != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Avoid repeats", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = filter.avoidRepeats,
                                onCheckedChange = onSetAvoidRepeats
                            )
                        }
                    }

                    if (onSetExcludeRecentlyEaten != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Exclude recently eaten", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = filter.excludeRecentlyEaten,
                                onCheckedChange = onSetExcludeRecentlyEaten
                            )
                        }

                        if (filter.excludeRecentlyEaten && onSetExcludeWithinDays != null) {
                            Column {
                                Text(
                                    "Within last ${filter.excludeWithinDays} days",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Slider(
                                    value = filter.excludeWithinDays.toFloat(),
                                    onValueChange = { onSetExcludeWithinDays(it.toInt()) },
                                    valueRange = 1f..30f,
                                    steps = 28
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
