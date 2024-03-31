package ykvlv.blss.data.dto.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.type.TagEnum;
import ykvlv.blss.data.type.RecipeTypeEnum;
import ykvlv.blss.data.validator.NullOrNotBlank;

import java.util.Set;

@Data
@NoArgsConstructor
public class SearchRecipesDTO {

	@NullOrNotBlank(message = "Заголовок может отсутствовать но не должен быть пустым")
	private String title;

	private RecipeTypeEnum recipeTypeEnum;

	private Set<TagEnum> tagEnums;

	@Valid
	@NotNull(message = "Параметры постраничного поиска должны быть указаны")
	@JsonProperty("pagingOptions")
	private PagingOptionsDTO pagingOptionsDTO;

}
