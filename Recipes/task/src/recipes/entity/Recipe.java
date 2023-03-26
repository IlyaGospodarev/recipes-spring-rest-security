package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private long id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String category;

    @Column
    private LocalDateTime date;

    @Column
    @NotBlank
    private String description;

    @Column
    @NotEmpty
    @ElementCollection(targetClass=String.class)
    private List<String> ingredients;

    @Column
    @NotEmpty
    @ElementCollection(targetClass=String.class)
    private List<String> directions;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Recipe() {
    }

    public Recipe(String name,
                  String category, LocalDateTime date, String description,
                  List<String> ingredients,
                  List<String> directions, User user) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.user = user;
    }

    public long getId() {return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe recipe)) return false;

        if (id != recipe.id) return false;
        if (!name.equals(recipe.name)) return false;
        if (!category.equals(recipe.category)) return false;
        if (!date.equals(recipe.date)) return false;
        return description.equals(recipe.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
