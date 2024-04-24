package ykvlv.blss.application.task.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.domain.dto.mapper.RecipeMapper;
import ykvlv.blss.domain.dto.request.CookingStepDTO;
import ykvlv.blss.domain.dto.request.RecipeDTO;
import ykvlv.blss.domain.entity.Client;
import ykvlv.blss.domain.entity.CookingStep;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;
import ykvlv.blss.domain.repository.RecipeRepository;
import ykvlv.blss.domain.type.RecipeTypeEnum;
import ykvlv.blss.domain.type.TagEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateRecipeTask implements JavaDelegate {

	private final ObjectMapper objectMapper;
	private final RecipeMapper recipeMapper;
	private final RecipeRepository recipeRepository;
	private final Validator validator;

	@Override
	public void execute(DelegateExecution execution) {
		var client = (Client) execution.getVariable("client");

		var title = StringUtils.stripToNull((String) execution.getVariable("title"));
		var description = StringUtils.stripToNull((String) execution.getVariable("description"));
		var duration = (Long) execution.getVariable("duration");
		var posterUUID = StringUtils.stripToNull((String) execution.getVariable("posterUUID"));
		RecipeTypeEnum recipeTypeEnum;
		Set<TagEnum> tagEnums;
		List<CookingStepDTO> cookingStepsDTO;

		try {
			String recipeTypeEnumString = StringUtils.stripToNull((String) execution.getVariable("recipeTypeEnum"));
			recipeTypeEnum = RecipeTypeEnum.valueOf(recipeTypeEnumString);
		} catch (IllegalArgumentException e) {
			throw new BEWrapper(BusinessException.INVALID_ENUM_VALUE,
					RecipeTypeEnum.class.getSimpleName(), Arrays.toString(RecipeTypeEnum.values()));
		}

		Set<String> tagEnumsString;
		try {
			String tagEnumsJson = StringUtils.stripToNull((String) execution.getVariable("tagEnums"));
			if (tagEnumsJson == null) {
				tagEnumsString = Set.of();
			} else {
				tagEnumsString = objectMapper.readValue(tagEnumsJson, new TypeReference<>() {});
			}
		} catch (JsonProcessingException e) {
			throw new BEWrapper(BusinessException.JSON_PARSING_ERROR, TagEnum.class.getSimpleName());
		}

		try {
			tagEnums = tagEnumsString.stream().map(TagEnum::valueOf).collect(Collectors.toSet());
		} catch (IllegalArgumentException e) {
			throw new BEWrapper(BusinessException.INVALID_ENUM_VALUE,
					TagEnum.class.getSimpleName(), Arrays.toString(TagEnum.values()));
		}

		try {
			String cookingStepsJson = StringUtils.stripToNull((String) execution.getVariable("cookingSteps"));
			cookingStepsDTO = objectMapper.readValue(cookingStepsJson, new TypeReference<>() {});
		} catch (JsonProcessingException | IllegalArgumentException e) {
			throw new BEWrapper(BusinessException.JSON_PARSING_ERROR, CookingStepDTO.class.getSimpleName());
		}

		var recipeDTO = new RecipeDTO();
		recipeDTO.setTitle(title);
		recipeDTO.setDescription(description);
		recipeDTO.setDuration(duration);
		recipeDTO.setPosterUUID(posterUUID);
		recipeDTO.setRecipeTypeEnum(recipeTypeEnum);
		recipeDTO.setTagEnums(tagEnums);
		recipeDTO.setCookingStepsDTO(cookingStepsDTO);

		var violations = validator.validate(recipeDTO);

		if (!violations.isEmpty()) {
			var validationErrors = violations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(", "));
			throw new BEWrapper(BusinessException.VALIDATION_ERROR, validationErrors);
		}

		var recipe = recipeMapper.map(recipeDTO, client);
		recipe = recipeRepository.save(recipe);

		int orderNumber = 0;
		List<CookingStep> cookingSteps = new ArrayList<>();
		for (CookingStepDTO stepDTO : recipeDTO.getCookingStepsDTO()) {
			CookingStep step = recipeMapper.map(stepDTO, recipe, orderNumber++);
			cookingSteps.add(step);
		}

		recipe.setCookingSteps(cookingSteps);

		var extendedRecipeResponse = recipeMapper.map(recipeRepository.save(recipe));

		execution.setVariable("extendedRecipeResponse", extendedRecipeResponse);
	}
}
