# 🍽️ DinnerSpinner

An Android app that takes the stress out of deciding what to cook. Spin a slot machine to get a random meal suggestion, manage your personal meal library, and generate a full weekly dinner plan — all stored locally on your device.

## Features

### 🎰 Spinner
- Slot machine reel animation with physics-based deceleration
- Swipe down or tap the spin button to spin
- Haptic feedback on each tick and a strong pulse on landing
- Filter by quick meals, protein type, and more before spinning
- "Not this one" re-roll button that excludes already-seen results this session
- Favorite meals get 2× weighting in randomisation
- Optionally exclude meals eaten within the last N days

### 🍳 My Meals
- Searchable and filterable meal list
- Swipe to delete (with undo) or swipe to favourite
- Tap any meal to edit it
- Sort by name, date added, or last eaten

### ✏️ Add / Edit Meal
- Name field with autocomplete from a built-in database of ~80 common dishes
- Tapping a suggestion auto-fills all known attributes (complexity, protein, staple, etc.)
- Chip selectors for every attribute — all optional except the name
- Recipe field that makes URLs tappable
- Quick-add flow: type, tap suggestion, save in two taps

### 📅 Meal Plan
- Generates a 7-day (Mon–Sun) dinner plan with one tap
- Filter by complexity, price, protein type, staple, nutrition, and cooking time
- Tap any day to re-roll just that slot
- Long-press to lock a day so re-generation skips it
- Share the week's plan as plain text via the Android share sheet

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| Local database | Room |
| Preferences | DataStore |
| Navigation | Navigation Compose (bottom bar) |
| Animation | Compose `Animatable` with spring physics |
| Language | Kotlin |

## Data Model

```
Meal          – name, complexity, price, protein, staple, nutrition,
                cookingTime, recipe, isFavorite, createdAt, lastEatenAt
SpinHistory   – mealId (FK), timestamp
MealPlan      – createdAt, list of (dayOfWeek → mealId) pairs
```

Attribute enums: `Complexity` · `Price` · `Protein` · `Staple` · `Nutrition` · `CookingTime`

## Project Structure

```
com.dinnerspinner.app/
  data/
    local/          Room database, DAO, type converters
    model/          Entities and enums
    repository/     MealRepository (single source of truth)
    static/         CommonDishes – built-in autocomplete dataset
  ui/
    navigation/     NavHost and bottom bar
    spinner/        Slot machine screen and reel composable
    meals/          My Meals list and Add/Edit form
    plan/           Weekly plan screen
    components/     Shared composables (AttributeChips, MealCard)
    theme/          Color, Type, Theme
  MainActivity.kt
```

## Getting Started

1. Clone the repo
2. Open in Android Studio (Hedgehog or later)
3. Sync Gradle and run on a device or emulator (API 26+)

No API keys, no accounts, no internet required — everything lives on-device.

## License

MIT
