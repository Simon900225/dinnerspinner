package com.dinnerspinner.app.data.static

import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple

data class CommonDish(
    val name: String,
    val complexity: Complexity? = null,
    val price: Price? = null,
    val protein: Protein? = null,
    val staple: Staple? = null,
    val nutrition: Nutrition? = null,
    val cookingTime: CookingTime? = null
) {
    fun toMeal() = Meal(
        name = name,
        complexity = complexity,
        price = price,
        protein = protein,
        staple = staple,
        nutrition = nutrition,
        cookingTime = cookingTime
    )
}

object CommonDishes {
    val all: List<CommonDish> = listOf(
        // Pasta
        CommonDish("Spaghetti Bolognese", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Pasta Carbonara", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Penne Arrabbiata", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Lasagna", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.LONG),
        CommonDish("Mac and Cheese", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.JUNK, CookingTime.QUICK),
        CommonDish("Pasta Primavera", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Fettuccine Alfredo", Complexity.MEDIUM, Price.MEDIUM, Protein.VEGETARIAN, Staple.PASTA, Nutrition.JUNK, CookingTime.QUICK),
        CommonDish("Pesto Pasta", Complexity.EASY, Price.MEDIUM, Protein.VEGETARIAN, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Pasta e Fagioli", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Cacio e Pepe", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Pasta alla Norma", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.PASTA, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Orecchiette with Sausage", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Tagliatelle with Mushroom Sauce", Complexity.MEDIUM, Price.MEDIUM, Protein.VEGETARIAN, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Linguine alle Vongole", Complexity.MEDIUM, Price.EXPENSIVE, Protein.FISH, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Smoked Salmon Pasta", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Spaghetti Aglio e Olio", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Gnocchi with Tomato Sauce", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),

        // Rice
        CommonDish("Chicken Fried Rice", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Vegetable Fried Rice", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Chicken Curry", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Beef Stir Fry", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Shrimp Fried Rice", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.RICE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Vegetable Curry", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Risotto", Complexity.HARD, Price.MEDIUM, Protein.VEGETARIAN, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Salmon with Rice", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Teriyaki Chicken", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Biryani", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Nasi Goreng", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Arroz con Pollo", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Pilaf", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Chicken Tikka Masala", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Prawn Curry", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),

        // Potato
        CommonDish("Beef Stew", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Shepherd's Pie", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Fish and Chips", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.POTATO, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("Potato Soup", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Chicken and Potatoes", Complexity.EASY, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Loaded Baked Potato", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Lamb Stew", Complexity.HARD, Price.EXPENSIVE, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Potato Gratin", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Colcannon", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Fish Pie", Complexity.HARD, Price.MEDIUM, Protein.FISH, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Schnitzel with Potato Salad", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),

        // Bread / Wraps
        CommonDish("Fish Tacos", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Chicken Tacos", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Veggie Burger", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Beef Burger", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("BLT Sandwich", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Grilled Cheese", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.BREAD, Nutrition.JUNK, CookingTime.QUICK),
        CommonDish("Club Sandwich", Complexity.EASY, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Chicken Wrap", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Falafel Wrap", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.BREAD, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Hot Dog", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.JUNK, CookingTime.QUICK),
        CommonDish("Shawarma", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Gyros", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Banh Mi", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Pita with Hummus and Veggies", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.BREAD, Nutrition.HEALTHY, CookingTime.QUICK),

        // Salads / Light
        CommonDish("Caesar Salad", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Greek Salad", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Niçoise Salad", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Chicken Salad", Complexity.EASY, Price.MEDIUM, Protein.MEAT, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Caprese Salad", Complexity.EASY, Price.MEDIUM, Protein.VEGETARIAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Waldorf Salad", Complexity.EASY, Price.MEDIUM, Protein.VEGETARIAN, Staple.NONE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Quinoa Salad", Complexity.EASY, Price.MEDIUM, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Tabbouleh", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Asian Slaw", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),

        // Fish
        CommonDish("Grilled Salmon", Complexity.EASY, Price.EXPENSIVE, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Tuna Casserole", Complexity.MEDIUM, Price.CHEAP, Protein.FISH, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Shrimp Scampi", Complexity.MEDIUM, Price.EXPENSIVE, Protein.FISH, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Fish Soup", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Baked Cod", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Seafood Paella", Complexity.HARD, Price.EXPENSIVE, Protein.FISH, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Miso Glazed Salmon", Complexity.EASY, Price.EXPENSIVE, Protein.FISH, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Ceviche", Complexity.MEDIUM, Price.EXPENSIVE, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Bouillabaisse", Complexity.HARD, Price.EXPENSIVE, Protein.FISH, Staple.BREAD, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Pan-Fried Trout", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.POTATO, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Tuna Steak", Complexity.EASY, Price.EXPENSIVE, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),

        // Soups / Stews
        CommonDish("Minestrone Soup", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Tomato Soup", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Chicken Noodle Soup", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.PASTA, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("French Onion Soup", Complexity.MEDIUM, Price.MEDIUM, Protein.VEGETARIAN, Staple.BREAD, Nutrition.OK, CookingTime.LONG),
        CommonDish("Lentil Soup", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Pea Soup", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Clam Chowder", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.NONE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Butternut Squash Soup", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Gazpacho", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Borscht", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.NONE, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Split Pea Soup with Ham", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.NONE, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Tom Yum Soup", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),

        // Meat mains
        CommonDish("Roast Chicken", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Grilled Steak", Complexity.MEDIUM, Price.EXPENSIVE, Protein.MEAT, Staple.NONE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Pork Chops", Complexity.EASY, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Meatballs", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Chicken Stir Fry", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Pulled Pork", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.LONG),
        CommonDish("Beef Tacos", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("BBQ Ribs", Complexity.MEDIUM, Price.EXPENSIVE, Protein.MEAT, Staple.NONE, Nutrition.JUNK, CookingTime.LONG),
        CommonDish("Lamb Chops", Complexity.MEDIUM, Price.EXPENSIVE, Protein.MEAT, Staple.NONE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Chicken Marsala", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Osso Buco", Complexity.HARD, Price.EXPENSIVE, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Turkey Meatloaf", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Chicken Katsu", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Duck Confit", Complexity.HARD, Price.EXPENSIVE, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Beef Wellington", Complexity.HARD, Price.EXPENSIVE, Protein.MEAT, Staple.NONE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Chili con Carne", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.LONG),

        // Vegetarian / Vegan
        CommonDish("Vegetable Stir Fry", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Mushroom Risotto", Complexity.HARD, Price.MEDIUM, Protein.VEGETARIAN, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Veggie Chili", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Shakshuka", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.BREAD, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Lentil Dal", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Stuffed Peppers", Complexity.MEDIUM, Price.MEDIUM, Protein.VEGETARIAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Veggie Quesadilla", Complexity.EASY, Price.CHEAP, Protein.VEGETARIAN, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Bean Burrito", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Tofu Scramble", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Chickpea Curry", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Ratatouille", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Quiche", Complexity.HARD, Price.MEDIUM, Protein.VEGETARIAN, Staple.NONE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Eggplant Parmesan", Complexity.MEDIUM, Price.MEDIUM, Protein.VEGETARIAN, Staple.NONE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Spanakopita", Complexity.HARD, Price.MEDIUM, Protein.VEGETARIAN, Staple.NONE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Lentil Bolognese", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.PASTA, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Black Bean Tacos", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.BREAD, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Cauliflower Curry", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Tofu Stir Fry", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Pumpkin Soup", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Veggie Paella", Complexity.HARD, Price.MEDIUM, Protein.VEGAN, Staple.RICE, Nutrition.HEALTHY, CookingTime.LONG),

        // Asian
        CommonDish("Pad Thai", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Ramen", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Sushi Bowls", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Dumplings", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.NONE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Bibimbap", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.HEALTHY, CookingTime.MEDIUM),
        CommonDish("Pho", Complexity.HARD, Price.MEDIUM, Protein.MEAT, Staple.PASTA, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Kung Pao Chicken", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("General Tso's Chicken", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("Miso Soup with Tofu", Complexity.EASY, Price.CHEAP, Protein.VEGAN, Staple.NONE, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Spring Rolls", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.NONE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Korean BBQ Bowl", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Mapo Tofu", Complexity.MEDIUM, Price.CHEAP, Protein.VEGAN, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Butter Chicken", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.MEDIUM),

        // Pizza
        CommonDish("Margherita Pizza", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.BREAD, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("Pepperoni Pizza", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.BREAD, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("BBQ Chicken Pizza", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.JUNK, CookingTime.MEDIUM),
        CommonDish("Vegetarian Pizza", Complexity.MEDIUM, Price.CHEAP, Protein.VEGETARIAN, Staple.BREAD, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Flatbread with Feta and Olives", Complexity.EASY, Price.MEDIUM, Protein.VEGETARIAN, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),

        // Swedish classics
        CommonDish("Köttbullar med potatis", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Pyttipanna", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Raggmunk med bacon", Complexity.MEDIUM, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Janssons frestelse", Complexity.MEDIUM, Price.CHEAP, Protein.FISH, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Kroppkakor", Complexity.HARD, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Ärtsoppa med fläsk", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.NONE, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Kalops", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Pannbiff med lök", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Kåldolmar", Complexity.HARD, Price.CHEAP, Protein.MEAT, Staple.RICE, Nutrition.OK, CookingTime.LONG),
        CommonDish("Laxpudding", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Stekt strömming", Complexity.EASY, Price.CHEAP, Protein.FISH, Staple.POTATO, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Fiskgratäng", Complexity.MEDIUM, Price.MEDIUM, Protein.FISH, Staple.POTATO, Nutrition.OK, CookingTime.LONG),
        CommonDish("Rotmos med fläsk", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Toast Skagen", Complexity.EASY, Price.EXPENSIVE, Protein.FISH, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Räksmörgås", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.BREAD, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Gravlax med hovmästarsås", Complexity.MEDIUM, Price.EXPENSIVE, Protein.FISH, Staple.BREAD, Nutrition.HEALTHY, CookingTime.QUICK),
        CommonDish("Wallenbergare", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Krämig laxpasta", Complexity.EASY, Price.MEDIUM, Protein.FISH, Staple.PASTA, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Blodpudding med lingon", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.NONE, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Gulaschsoppa", Complexity.MEDIUM, Price.MEDIUM, Protein.MEAT, Staple.BREAD, Nutrition.OK, CookingTime.MEDIUM),
        CommonDish("Falukorv med pasta", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.PASTA, Nutrition.JUNK, CookingTime.QUICK),
        CommonDish("Pytt i panna med ägg", Complexity.EASY, Price.CHEAP, Protein.MEAT, Staple.POTATO, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Sill med kokt potatis", Complexity.EASY, Price.CHEAP, Protein.FISH, Staple.POTATO, Nutrition.OK, CookingTime.QUICK),
        CommonDish("Älggryta", Complexity.HARD, Price.EXPENSIVE, Protein.MEAT, Staple.POTATO, Nutrition.HEALTHY, CookingTime.LONG),
        CommonDish("Smörgåstårta", Complexity.HARD, Price.MEDIUM, Protein.FISH, Staple.BREAD, Nutrition.OK, CookingTime.LONG)
    )

    fun search(query: String): List<CommonDish> {
        if (query.isBlank()) return emptyList()
        val lower = query.lowercase()
        return all.filter { it.name.lowercase().contains(lower) }.take(5)
    }
}
