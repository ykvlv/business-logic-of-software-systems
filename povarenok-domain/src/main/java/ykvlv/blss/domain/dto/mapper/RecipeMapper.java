package ykvlv.blss.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ykvlv.blss.domain.entity.Client;
import ykvlv.blss.domain.entity.CookingStep;
import ykvlv.blss.domain.entity.Recipe;
import ykvlv.blss.domain.dto.request.CookingStepDTO;
import ykvlv.blss.domain.dto.request.RecipeDTO;
import ykvlv.blss.domain.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.domain.dto.response.RecipeResponse;

@Mapper(componentModel = "spring")
public abstract class RecipeMapper {

	// Мапит DTO в ENTITY

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "likesCount", ignore = true)
	@Mapping(target = "cookingSteps", ignore = true)
	@Mapping(target = "reviews", ignore = true)
	@Mapping(target = "likes", ignore = true)
	@Mapping(target = "client", source = "client")
	public abstract Recipe map(RecipeDTO dto, Client client);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "description", source = "dto.description")
	public abstract CookingStep map(CookingStepDTO dto, Recipe recipe, Integer orderNumber);

	// Мапит ENTITY в RESPONSE

	@Mapping(target = "cookingStepResponses", source = "cookingSteps")
	@Mapping(target = "clientResponse", source = "client")
	public abstract ExtendedRecipeResponse map(Recipe entity);

	// Мапит ENTITY в RESPONSE без транзакций спокойно

	public abstract RecipeResponse mapWithoutJoins(Recipe entity);

	// Домапливает DTO в ENTITY

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "likesCount", ignore = true)
	@Mapping(target = "cookingSteps", ignore = true)
	@Mapping(target = "reviews", ignore = true)
	@Mapping(target = "client", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract void map(@MappingTarget Recipe entity, RecipeDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "description", source = "dto.description")
	public abstract void map(@MappingTarget CookingStep entity, CookingStepDTO dto, Recipe recipe, Integer orderNumber);

}
