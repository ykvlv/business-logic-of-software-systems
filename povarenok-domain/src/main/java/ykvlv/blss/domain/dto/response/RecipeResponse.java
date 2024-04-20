package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ykvlv.blss.domain.type.RecipeTypeEnum;

import java.io.Serializable;

@Getter
@SuperBuilder
public class RecipeResponse implements Serializable {

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
