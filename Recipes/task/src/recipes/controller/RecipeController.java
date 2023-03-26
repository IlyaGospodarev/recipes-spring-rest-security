package recipes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.entity.User;
import recipes.entity.UserDetailsImpl;
import recipes.service.RecipeService;
import recipes.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {
    private final RecipeService recipeService;
    private UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<Map> addRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody Recipe recipe) {
        return recipeService.addRecipe(user.getUsername(), recipe);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable long id) {
        return recipeService.deleteRecipe(user.getUsername(), id);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable long id,
                                               @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(user.getUsername(), id, recipe);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<List> getRecipeByParam(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) String name) {
        return recipeService.getRecipeByParam(category, name);
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {
        return userService.register(user);
    }
}
