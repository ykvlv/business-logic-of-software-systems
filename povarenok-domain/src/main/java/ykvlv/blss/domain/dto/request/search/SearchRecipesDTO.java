package ykvlv.blss.domain.dto.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.domain.type.RecipeTypeEnum;
import ykvlv.blss.domain.type.TagEnum;
import ykvlv.blss.domain.validator.NullOrNotBlank;

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
