package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@SuperBuilder
public class SearchRecipesResponse implements Serializable {

	@NonNull
	private PagingResult pagingResult;

	@NonNull
	private List<RecipeResponse> recipes;

}
