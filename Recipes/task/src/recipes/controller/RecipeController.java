package recipes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.entity.UserDetailsImpl;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                         @Valid @RequestBody Recipe recipe) {
        return ResponseEntity.ok(recipeService.addRecipe(user.getUsername(), recipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable long id) {
        return recipeService.deleteRecipe(user.getUsername(), id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable long id,
                                               @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(user.getUsername(), id, recipe);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> getRecipeByParam(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) String name) {
        return recipeService.getRecipeByParam(category, name);
    }

}

