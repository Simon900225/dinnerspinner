package com.dinnerspinner.app.ui.meals

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dinnerspinner.app.data.local.MealDatabase
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple
import com.dinnerspinner.app.data.repository.MealRepository
import com.dinnerspinner.app.data.static.CommonDish
import com.dinnerspinner.app.data.static.CommonDishes
import com.dinnerspinner.app.ui.components.MealFilter
import com.dinnerspinner.app.ui.components.MealFilterPanel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMealPickerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MealRepository(MealDatabase.getInstance(application).mealDao())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filter = MutableStateFlow(MealFilter())
    val filter: StateFlow<MealFilter> = _filter

    private val _isFilterExpanded = MutableStateFlow(false)
    val isFilterExpanded: StateFlow<Boolean> = _isFilterExpanded

    private val _addedNames = MutableStateFlow<Set<String>>(emptySet())
    val addedNames: StateFlow<Set<String>> = _addedNames

    val filteredDishes: StateFlow<List<CommonDish>> = combine(_searchQuery, _filter) { query, filter ->
        val queryFiltered = if (query.isBlank()) CommonDishes.all
        else CommonDishes.all.filter { it.name.contains(query, ignoreCase = true) }
        queryFiltered.filter { filter.matches(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CommonDishes.all)

    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun toggleFilterExpanded() { _isFilterExpanded.update { !it } }

    fun setMaxComplexity(value: Complexity?) { _filter.update { it.copy(maxComplexity = value) } }
    fun setMaxPrice(value: Price?) { _filter.update { it.copy(maxPrice = value) } }
    fun toggleProtein(protein: Protein) {
        _filter.update {
            val updated = if (protein in it.proteins) it.proteins - protein else it.proteins + protein
            it.copy(proteins = updated)
        }
    }
    fun toggleStaple(staple: Staple) {
        _filter.update {
            val updated = if (staple in it.staples) it.staples - staple else it.staples + staple
            it.copy(staples = updated)
        }
    }
    fun setMaxNutrition(value: Nutrition?) { _filter.update { it.copy(maxNutrition = value) } }
    fun setMaxCookingTime(value: CookingTime?) { _filter.update { it.copy(maxCookingTime = value) } }

    fun addDish(dish: CommonDish) {
        viewModelScope.launch {
            repository.insertMeal(dish.toMeal())
            _addedNames.update { it + dish.name }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealPickerScreen(
    onNavigateBack: () -> Unit,
    onAddCustomMeal: () -> Unit,
    viewModel: AddMealPickerViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val isFilterExpanded by viewModel.isFilterExpanded.collectAsState()
    val dishes by viewModel.filteredDishes.collectAsState()
    val addedNames by viewModel.addedNames.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a Meal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search common dishes...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                MealFilterPanel(
                    filter = filter,
                    isExpanded = isFilterExpanded,
                    onToggleExpand = { viewModel.toggleFilterExpanded() },
                    onSetMaxComplexity = { viewModel.setMaxComplexity(it) },
                    onSetMaxPrice = { viewModel.setMaxPrice(it) },
                    onToggleProtein = { viewModel.toggleProtein(it) },
                    onToggleStaple = { viewModel.toggleStaple(it) },
                    onSetMaxNutrition = { viewModel.setMaxNutrition(it) },
                    onSetMaxCookingTime = { viewModel.setMaxCookingTime(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            item {
                Button(
                    onClick = onAddCustomMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Add a custom meal manually")
                }
            }

            item { HorizontalDivider() }

            if (dishes.isEmpty()) {
                item {
                    Text(
                        text = "No dishes match your filters",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                items(dishes, key = { it.name }) { dish ->
                    CommonDishPickerRow(
                        dish = dish,
                        isAdded = dish.name in addedNames,
                        onAdd = {
                            viewModel.addDish(dish)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${dish.name} added to your meals",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun CommonDishPickerRow(
    dish: CommonDish,
    isAdded: Boolean,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dish.name,
                style = MaterialTheme.typography.bodyLarge
            )
            val meta = listOfNotNull(
                dish.protein?.label,
                dish.complexity?.label,
                dish.cookingTime?.label
            ).joinToString(" · ")
            if (meta.isNotEmpty()) {
                Text(
                    text = meta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        IconButton(onClick = onAdd) {
            Icon(
                imageVector = if (isAdded) Icons.Filled.Check else Icons.Filled.Add,
                contentDescription = if (isAdded) "Added" else "Add",
                tint = if (isAdded)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
