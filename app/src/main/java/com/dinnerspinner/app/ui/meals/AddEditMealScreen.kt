package com.dinnerspinner.app.ui.meals

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple
import com.dinnerspinner.app.ui.components.SingleSelectChips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMealScreen(
    mealId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddEditMealViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val isEdit = mealId != null
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mealId) {
        if (mealId != null) viewModel.loadMeal(mealId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onNavigateBack()
    }

    LaunchedEffect(state.isAddedAndContinue) {
        if (state.isAddedAndContinue) {
            viewModel.resetContinueFlag()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Meal added!",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Meal" else "Add Meal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconToggleButton(
                        checked = state.isFavorite,
                        onCheckedChange = { viewModel.onFavoriteChange(it) }
                    ) {
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (state.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (isEdit) {
                FloatingActionButton(onClick = { viewModel.save() }) {
                    Icon(Icons.Filled.Check, contentDescription = "Save")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name field with autocomplete dropdown
            var nameFocused by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = state.suggestions.isNotEmpty() && nameFocused,
                onExpandedChange = {}
            ) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Meal name *") },
                    isError = state.nameError,
                    supportingText = if (state.nameError) {
                        { Text("Name is required") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .onFocusChanged { nameFocused = it.isFocused }
                )
                ExposedDropdownMenu(
                    expanded = state.suggestions.isNotEmpty() && nameFocused,
                    onDismissRequest = { viewModel.dismissSuggestions() }
                ) {
                    state.suggestions.forEach { dish ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(dish.name, style = MaterialTheme.typography.bodyMedium)
                                        val meta = listOfNotNull(
                                            dish.protein?.label,
                                            dish.complexity?.label,
                                            dish.cookingTime?.label
                                        ).joinToString(" · ")
                                        if (meta.isNotEmpty()) {
                                            Text(
                                                meta,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = { viewModel.applySuggestion(dish) }
                        )
                    }
                }
            }

            HorizontalDivider()

            SingleSelectChips(
                label = "Complexity",
                options = Complexity.entries,
                selected = state.complexity,
                labelFor = { it.label },
                onSelect = { viewModel.onComplexityChange(it) }
            )

            SingleSelectChips(
                label = "Cooking Time",
                options = CookingTime.entries,
                selected = state.cookingTime,
                labelFor = { it.label },
                onSelect = { viewModel.onCookingTimeChange(it) }
            )

            SingleSelectChips(
                label = "Price",
                options = Price.entries,
                selected = state.price,
                labelFor = { it.label },
                onSelect = { viewModel.onPriceChange(it) }
            )

            SingleSelectChips(
                label = "Protein",
                options = Protein.entries,
                selected = state.protein,
                labelFor = { it.label },
                onSelect = { viewModel.onProteinChange(it) }
            )

            SingleSelectChips(
                label = "Staple",
                options = Staple.entries,
                selected = state.staple,
                labelFor = { it.label },
                onSelect = { viewModel.onStapleChange(it) }
            )

            SingleSelectChips(
                label = "Nutrition",
                options = Nutrition.entries,
                selected = state.nutrition,
                labelFor = { it.label },
                onSelect = { viewModel.onNutritionChange(it) }
            )

            HorizontalDivider()

            val isUrl = state.recipe.startsWith("http://") || state.recipe.startsWith("https://")
            OutlinedTextField(
                value = state.recipe,
                onValueChange = { viewModel.onRecipeChange(it) },
                label = { Text("Recipe (optional)") },
                placeholder = { Text("Paste a URL or write notes...") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = if (isUrl) {
                    {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.recipe))
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.Filled.OpenInNew, contentDescription = "Open URL")
                        }
                    }
                } else null
            )

            if (isEdit) {
                Spacer(modifier = Modifier.height(80.dp))
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.saveAndAddAnother() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add & Create Another")
                    }
                    Button(
                        onClick = { viewModel.save() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}
