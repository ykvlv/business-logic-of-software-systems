package ykvlv.blss.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ykvlv.blss.data.dto.request.*;
import ykvlv.blss.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;
import ykvlv.blss.service.RecipeServiceImpl;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/recipe")
public class RecipeController {

    private final RecipeServiceImpl recipeService;

    @Operation(summary = "Создать новый рецепт")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExtendedRecipeResponse> create(@Validated @RequestBody RecipeDTO recipeDTO) {
        return new ResponseEntity<>(
                recipeService.create(recipeDTO),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Получить рецепт по id")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedRecipeResponse> read(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                recipeService.read(id),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Обновить рецепт по id")
    @PutMapping("/{id}")
    public ResponseEntity<ExtendedRecipeResponse> update(@PathVariable("id") Long id,
                                                         @Validated @RequestBody RecipeDTO recipeDTO) {
        return new ResponseEntity<>(
                recipeService.update(id, recipeDTO),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Удалить рецепт по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Поиск рецепта")
    @PostMapping("/search")
    public ResponseEntity<SearchRecipesResponse> search(@Validated @RequestBody SearchRecipesDTO searchRecipesDTO) {
        return new ResponseEntity<>(
                recipeService.search(searchRecipesDTO),
                HttpStatus.OK
        );
    }

}
