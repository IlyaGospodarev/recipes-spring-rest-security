package recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.entity.Recipe;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Recipe> getRecipe(long id) {
        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.notFound()
                    .build();
        }

        Optional<Recipe> recipe = recipeRepository.findById(id);

        return recipe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    public ResponseEntity<Map> addRecipe(String username, Recipe recipe) {
        if (isValidRecipeRequest(recipe)) {
            Recipe createRecipe = new Recipe(
                    recipe.getName(),
                    recipe.getCategory(),
                    LocalDateTime.now(),
                    recipe.getDescription(),
                    recipe.getIngredients(),
                    recipe.getDirections(),
                    userRepository.findByEmailIgnoreCase(username));

            recipeRepository.save(createRecipe);

            return ResponseEntity.ok()
                    .body(Map.of("id", createRecipe.getId()));
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    public ResponseEntity<Recipe> deleteRecipe(String username, long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElse(null);

        if (recipe == null) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (!recipe.getUser()
                .getEmail()
                .equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeRepository.deleteById(id);

        return ResponseEntity.noContent()
                .build();
    }

    public ResponseEntity<Recipe> updateRecipe(String username, long id, Recipe recipe) {
        Recipe updateRecipe = recipeRepository.findById(id)
                .orElse(null);

        if (updateRecipe == null) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (!updateRecipe.getUser()
                .getEmail()
                .equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (isValidRecipeRequest(recipe)) {

            updateRecipe.setName(recipe.getName());
            updateRecipe.setCategory(recipe.getCategory());
            updateRecipe.setDescription(recipe.getDescription());
            updateRecipe.setIngredients(recipe.getIngredients());
            updateRecipe.setDirections(recipe.getDirections());

            recipeRepository.save(updateRecipe);

            return ResponseEntity.noContent()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    public ResponseEntity<List> getRecipeByParam(String category, String name) {

        if (category != null && name != null) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (category != null) {
            return ResponseEntity.ok(recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category));
        } else if (name != null) {
            return ResponseEntity.ok(recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name));
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    private boolean isValidRecipeRequest(Recipe recipe) {
        boolean isValidName = recipe.getName() != null
                && !recipe.getName()
                .isEmpty()
                && !recipe.getName()
                .isBlank();
        boolean isValidCategory = recipe.getCategory() != null
                && !recipe.getCategory()
                .isEmpty()
                && !recipe.getCategory()
                .isBlank();
        boolean isValidDescription = recipe.getDescription() != null
                && !recipe.getDescription()
                .isEmpty()
                && !recipe.getDescription()
                .isBlank();
        boolean isValidIngredients = recipe.getIngredients() != null
                && !recipe.getIngredients()
                .isEmpty();
        boolean isValidDirections = recipe.getDirections() != null
                && !recipe.getDirections()
                .isEmpty();
        return isValidName && isValidCategory && isValidDescription && isValidIngredients && isValidDirections;
    }
}

