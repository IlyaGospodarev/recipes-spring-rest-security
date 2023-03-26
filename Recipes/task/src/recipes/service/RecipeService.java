package recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.entity.Recipe;
import recipes.entity.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Recipe> getRecipe(long id) {
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Map<String, Long> addRecipe(String username, Recipe recipe) {
        User user = userRepository.findByEmailIgnoreCase(username);
        Recipe createRecipe = new Recipe(
                recipe.getName(),
                recipe.getCategory(),
                LocalDateTime.now(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections(),
                user);

        Recipe save = recipeRepository.save(createRecipe);

        return Map.of("id", save.getId());
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

        updateRecipe.setName(recipe.getName());
        updateRecipe.setCategory(recipe.getCategory());
        updateRecipe.setDescription(recipe.getDescription());
        updateRecipe.setIngredients(recipe.getIngredients());
        updateRecipe.setDirections(recipe.getDirections());

        recipeRepository.save(updateRecipe);

        return ResponseEntity.noContent()
                .build();
    }

    public ResponseEntity<List<Recipe>> getRecipeByParam(String category, String name) {

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
}

