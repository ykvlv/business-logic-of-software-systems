package ykvlv.blss.data.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ykvlv.blss.data.type.TagEnum;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder
public class ExtendedRecipeResponse extends RecipeResponse {

	private Set<TagEnum> tagEnums;

	private List<CookingStepResponse> cookingStepResponses;

}
