package ykvlv.blss.data.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ykvlv.blss.data.type.RecipeTypeEnum;

@Getter
@SuperBuilder
public class RecipeResponse {

	@NonNull
	private Long id;

	@NonNull
	private String title;

	private String description;

	@NonNull
	private Long duration;

	@NonNull
	private Long likesCount;

	private String posterUUID;

	@NonNull
	private RecipeTypeEnum recipeTypeEnum;

}
