package ykvlv.blss.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ykvlv.blss.domain.type.TagEnum;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder
public class ExtendedRecipeResponse extends RecipeResponse {

	private Set<TagEnum> tagEnums;

	@JsonProperty("cookingSteps")
	private List<CookingStepResponse> cookingStepResponses;

	@JsonProperty("client")
	private ClientResponse clientResponse;

}
