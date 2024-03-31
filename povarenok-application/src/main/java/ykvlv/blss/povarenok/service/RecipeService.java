package ykvlv.blss.povarenok.service;

import lombok.NonNull;
import ykvlv.blss.povarenok.data.dto.request.RecipeDTO;
import ykvlv.blss.povarenok.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.povarenok.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.povarenok.data.dto.response.SearchRecipesResponse;

public interface RecipeService {
	@NonNull
	SearchRecipesResponse search(@NonNull SearchRecipesDTO searchRecipesDTO);

	@NonNull
	ExtendedRecipeResponse create(@NonNull RecipeDTO recipeDTO, String login);

	@NonNull
	ExtendedRecipeResponse read(@NonNull Long id);

	@NonNull
	ExtendedRecipeResponse update(@NonNull Long id, @NonNull RecipeDTO recipeDTO);

	boolean isEligibleTo(@NonNull Long recipeId, @NonNull String login);

	void delete(@NonNull Long id);

}
