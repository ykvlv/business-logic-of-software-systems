package ykvlv.blss.data.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ykvlv.blss.data.dto.request.CookingStepDTO;
import ykvlv.blss.data.dto.request.RecipeDTO;
import ykvlv.blss.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.data.dto.response.RecipeResponse;
import ykvlv.blss.data.entity.Recipe;
import ykvlv.blss.data.entity.CookingStep;


@Mapper(componentModel = "spring")
public abstract class RecipeMapper {

	// Мапит DTO в ENTITY

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "likesCount", ignore = true)
	@Mapping(target = "cookingSteps", ignore = true)
	@Mapping(target = "reviews", ignore = true)
	public abstract Recipe map(RecipeDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "description", source = "dto.description")
	public abstract CookingStep map(CookingStepDTO dto, Recipe recipe, Integer orderNumber);

	// Мапит ENTITY в RESPONSE

	@Mapping(target = "cookingStepResponses", source = "cookingSteps")
	public abstract ExtendedRecipeResponse map(Recipe entity);

	// Мапит ENTITY в RESPONSE без транзакций спокойно

	public abstract RecipeResponse mapWithoutJoins(Recipe entity);

	// Домапливает DTO в ENTITY

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "likesCount", ignore = true)
	@Mapping(target = "cookingSteps", ignore = true)
	@Mapping(target = "reviews", ignore = true)
	public abstract void map(@MappingTarget Recipe entity, RecipeDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "description", source = "dto.description")
	public abstract void map(@MappingTarget CookingStep entity, CookingStepDTO dto, Recipe recipe, Integer orderNumber);

}
