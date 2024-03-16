package ykvlv.blss.service;

import lombok.NonNull;
import ykvlv.blss.data.dto.request.RecipeDTO;
import ykvlv.blss.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;

public interface RecipeService {
	@NonNull
	SearchRecipesResponse search(@NonNull SearchRecipesDTO searchRecipesDTO);

	@NonNull
	ExtendedRecipeResponse create(@NonNull RecipeDTO recipeDTO);

	@NonNull
	ExtendedRecipeResponse read(@NonNull Long id);

	@NonNull
	ExtendedRecipeResponse update(@NonNull Long id, @NonNull RecipeDTO recipeDTO);

	void delete(@NonNull Long id);

}
