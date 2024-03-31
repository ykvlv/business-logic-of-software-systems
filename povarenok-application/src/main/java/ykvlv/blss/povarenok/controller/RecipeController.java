package ykvlv.blss.povarenok.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ykvlv.blss.povarenok.data.dto.request.RecipeDTO;
import ykvlv.blss.povarenok.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.povarenok.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.povarenok.data.dto.response.SearchRecipesResponse;
import ykvlv.blss.povarenok.service.RecipeService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(summary = "Создать новый рецепт")
    @PreAuthorize("hasAuthority('CREATOR')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExtendedRecipeResponse> create(@Validated @RequestBody RecipeDTO recipeDTO,
                                                         Authentication authentication) {
        String login = authentication.getName();

        return new ResponseEntity<>(
                recipeService.create(recipeDTO, login),
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
    @PreAuthorize("hasAuthority('MAINTAINER') " +
            "|| hasAuthority('CREATOR') && @recipeServiceImpl.isEligibleTo(#id, authentication.name)")
    @PutMapping("/{id}")
    public ResponseEntity<ExtendedRecipeResponse> update(@PathVariable("id") Long id,
                                                         @Validated @RequestBody RecipeDTO recipeDTO) {
        return new ResponseEntity<>(
                recipeService.update(id, recipeDTO),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Удалить рецепт по id")
    @PreAuthorize("hasAuthority('MAINTAINER') " +
            "|| hasAuthority('CREATOR') && @recipeServiceImpl.isEligibleTo(#id, authentication.name)")
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
