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
import ykvlv.blss.domain.dto.request.search.PagingOptionsDTO;
import ykvlv.blss.domain.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.domain.dto.request.search.SortingOptionsDTO;
import ykvlv.blss.domain.dto.response.PagingResult;
import ykvlv.blss.domain.dto.response.SearchRecipesResponse;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;
import ykvlv.blss.domain.repository.SearchRepository;
import ykvlv.blss.domain.type.RecipeTypeEnum;
import ykvlv.blss.domain.type.TagEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchRecipesTask implements JavaDelegate {

	private final ObjectMapper objectMapper;
	private final RecipeMapper recipeMapper;
	private final SearchRepository searchRepository;
	private final Validator validator;

	@Override
	public void execute(DelegateExecution execution) {
		var title = StringUtils.stripToNull((String) execution.getVariable("title"));
		RecipeTypeEnum recipeTypeEnum;
		Set<TagEnum> tagEnums;
		int pageNumber;
		int pageSize;
		List<SortingOptionsDTO> sortingOptionsDTOs;

		try {
			pageNumber = Math.toIntExact((Long) execution.getVariable("pageNumber"));
		} catch (ArithmeticException e) {
			throw new BEWrapper(BusinessException.INT_EXPECTED_ERROR, "pageNumber");
		}

		try {
			pageSize = Math.toIntExact((Long) execution.getVariable("pageSize"));
		} catch (ArithmeticException e) {
			throw new BEWrapper(BusinessException.INT_EXPECTED_ERROR, "pageSize");
		}

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
			String sortingOptionsJson = StringUtils.stripToNull((String) execution.getVariable("sortingOptions"));
			if (sortingOptionsJson == null) {
				sortingOptionsDTOs = List.of();
			} else {
				sortingOptionsDTOs = objectMapper.readValue(sortingOptionsJson, new TypeReference<>() {});
			}
		} catch (JsonProcessingException e) {
			throw new BEWrapper(BusinessException.JSON_PARSING_ERROR, SortingOptionsDTO.class.getSimpleName());
		}

		var pagingOptionsDTO = new PagingOptionsDTO();
		pagingOptionsDTO.setPageNumber(pageNumber);
		pagingOptionsDTO.setPageSize(pageSize);
		pagingOptionsDTO.setSortingOptionDTOs(sortingOptionsDTOs);

		var searchRecipesDTO = new SearchRecipesDTO();
		searchRecipesDTO.setTitle(title);
		searchRecipesDTO.setRecipeTypeEnum(recipeTypeEnum);
		searchRecipesDTO.setTagEnums(tagEnums);
		searchRecipesDTO.setPagingOptionsDTO(pagingOptionsDTO);

		var violations = validator.validate(searchRecipesDTO);

		if (!violations.isEmpty()) {
			var validationErrors = violations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(", "));
			throw new BEWrapper(BusinessException.VALIDATION_ERROR, validationErrors);
		}

		var slice = searchRepository.searchRecipes(searchRecipesDTO);

		var recipes = slice.getContent().stream()
				.map(recipeMapper::mapWithoutJoins)
				.toList();

		var searchRecipesResponse = SearchRecipesResponse.builder()
				.pagingResult(PagingResult.builder()
						.pageSize(slice.getSize())
						.pageNumber(slice.getNumber())
						.morePagesAvailable(slice.hasNext())
						.build())
				.recipes(recipes)
				.build();

		execution.setVariable("searchRecipesResponse", searchRecipesResponse);
	}
}
