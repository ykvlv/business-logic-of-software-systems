package ykvlv.blss.povarenok.data.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class SearchRecipesResponse {

	@NonNull
	private PagingResult pagingResult;

	@NonNull
	private List<RecipeResponse> recipes;

}
