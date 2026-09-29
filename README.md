# Smart Pantry Manager

Smart Pantry Manager is an Android application I developed in Java for my Mobile App Development 700 assignment.

The idea behind the app is simple: users can keep track of the ingredients they already have at home, and the app suggests recipes they can make using those ingredients. I wanted the app to be useful for reducing food waste and also help users decide what they can cook without having to buy extra ingredients.

## Main Features

The app allows the user to:

- Add ingredients to their pantry
- View all ingredients currently in the pantry
- Edit existing ingredients
- Delete ingredients
- Enter the quantity, unit and expiry date of an ingredient
- View recipes they can make with the ingredients they have
- Open a recipe to see its ingredients and preparation instructions
- Change settings such as expiry alerts and unit preferences

The pantry information is saved, so the ingredients are still available when the app is closed and opened again.

## Recipe Matching

One of the main parts of this project is the recipe matching feature.

A recipe is only shown under Suggested Recipes when the user has all the ingredients required for that recipe in the correct quantities.

For example, if a recipe needs four ingredients but the user only has three of them, that recipe will not be suggested.

This logic is handled by the `IngredientMatcher` class.

The matching also deals with some simple differences in ingredient names and compatible measurement units. This helps prevent small differences in the way an ingredient is entered from stopping a recipe from matching.

## Recipes

The app has 18 recipes that are automatically added to the database.

Each recipe contains:

- A recipe name
- Required ingredients
- Required quantities and units
- Preparation instructions

The recipes are added through `RecipeSeeder.java`.

## Database

I chose SQLite for this project and implemented it using `SQLiteOpenHelper`.

I chose SQLite because the app does not need an internet connection or an online account. Pantry information can be stored directly on the user's device and can still be accessed when the user is offline.

The database contains three main tables:

- `pantry_items` for the user's pantry ingredients
- `recipes` for recipe information
- `recipe_ingredients` for the ingredients required by each recipe

SQLite also allows the app to perform the full CRUD operations required for the assignment: Create, Read, Update and Delete.

## Main Screens

The application includes the following screens:

1. Pantry
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

The bottom navigation bar is used to move between the main sections of the application.

## Technologies Used

This project was developed using:

- Java
- Android Studio
- SQLite
- SQLiteOpenHelper
- RecyclerView
- Activities and Fragments
- Intents
- Gradle
- Android Material components

## Project Structure

The main Java files are organised into different packages for the database, models, adapters, user interface and recipe matching logic.

Some of the important classes include:

- `MainActivity.java` – manages the main application and navigation
- `AddEditIngredientActivity.java` – handles adding and editing pantry ingredients
- `RecipeDetailActivity.java` – displays the selected recipe
- `DatabaseHelper.java` – manages the SQLite database
- `RecipeSeeder.java` – adds the recipes to the database
- `IngredientMatcher.java` – checks whether the pantry contains everything required for a recipe
- `PantryAdapter.java` – displays pantry items
- `RecipeAdapter.java` – displays recipes

## How to Run the Project

1. Clone or download this repository.
2. Open the Smart Pantry Manager project in Android Studio.
3. Allow Gradle to sync and download the required dependencies.
4. Start an Android emulator or connect an Android device.
5. Run the application from Android Studio.

The database and recipe information are created automatically when the app is first used, so no separate database setup is required.

## Testing

I tested the main functions of the application, including adding, viewing, editing and deleting pantry ingredients.

I also tested the strict recipe matching by adding the ingredients required for a recipe and checking that the recipe appeared under Suggested Recipes. When a required ingredient was removed, the recipe was no longer suggested.

I also tested that pantry information remains saved after closing and reopening the application.

## Author

Natalie Nyabanhi

Mobile App Development 700  
Richfield Graduate Institute of Technology
 
