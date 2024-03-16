package ykvlv.blss.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ykvlv.blss.data.dto.mapper.RecipeMapper;
import ykvlv.blss.data.dto.request.CookingStepDTO;
import ykvlv.blss.data.dto.request.RecipeDTO;
import ykvlv.blss.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.data.dto.response.ExtendedRecipeResponse;
import ykvlv.blss.data.dto.response.PagingResult;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;
import ykvlv.blss.data.entity.CookingStep;
import ykvlv.blss.data.entity.Recipe;
import ykvlv.blss.data.repository.RecipeRepository;
import ykvlv.blss.data.repository.SearchRepository;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final SearchRepository searchRepository;
    private final RecipeMapper recipeMapper;

    @NonNull
    @Override
    public SearchRecipesResponse search(@NonNull SearchRecipesDTO searchRecipesDTO) {
        var slice = searchRepository.searchRecipes(searchRecipesDTO);

        var recipes = slice.getContent().stream()
                .map(recipeMapper::mapWithoutJoins)
                .toList();

        return SearchRecipesResponse.builder()
                .pagingResult(PagingResult.builder()
                        .pageSize(slice.getSize())
                        .pageNumber(slice.getNumber())
                        .morePagesAvailable(slice.hasNext())
                        .build())
                .recipes(recipes)
                .build();
    }

    @NonNull
    @Override
    @Transactional // для атомарности добавления рецепта и шагов приготовления
    public ExtendedRecipeResponse create(@NonNull RecipeDTO recipeDTO) {
        Recipe recipe = recipeMapper.map(recipeDTO);
        recipe = recipeRepository.save(recipe);

        int orderNumber = 0;
        List<CookingStep> cookingSteps = new ArrayList<>();
        for (CookingStepDTO stepDTO : recipeDTO.getCookingStepsDTO()) {
            CookingStep step = recipeMapper.map(stepDTO, recipe, orderNumber++);
            cookingSteps.add(step);
        }

        recipe.setCookingSteps(cookingSteps);

		return recipeMapper.map(recipeRepository.save(recipe));
    }

    @NonNull
    @Override
    @Transactional(readOnly = true) // для автоматической инициализации
    public ExtendedRecipeResponse read(@NonNull Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new BEWrapper(BusinessException.RECIPE_NOT_FOUND, id));

		return recipeMapper.map(recipe);
    }

    @NonNull
    @Override
    @Transactional // для автоматической инициализации
    public ExtendedRecipeResponse update(@NonNull Long id, @NonNull RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new BEWrapper(BusinessException.RECIPE_NOT_FOUND, id));

        recipeMapper.map(recipe, recipeDTO);

        // Обновляем шаги
        var cookingSteps = recipe.getCookingSteps();
        var cookingStepsDTO = recipeDTO.getCookingStepsDTO();
        for (int i = 0; i < cookingStepsDTO.size(); i++) {

            var stepDTO = cookingStepsDTO.get(i);
            if (i < cookingSteps.size()) {

                // Обновляем существующий шаг
                var existingStep = cookingSteps.get(i);
                existingStep.setDescription(stepDTO.getDescription());
            } else {

                // Добавляем новый шаг
                var newStep = recipeMapper.map(stepDTO, recipe, i);
                cookingSteps.add(newStep);
            }
        }

        // Удаляем лишние шаги, если есть
        if (cookingSteps.size() > cookingStepsDTO.size()) {
            List<CookingStep> stepsToRemove = cookingSteps.subList(cookingStepsDTO.size(), cookingSteps.size());
            cookingSteps.removeAll(stepsToRemove);
        }

        recipe = recipeRepository.save(recipe);

        return recipeMapper.map(recipe);
    }

    @Override
    public void delete(@NonNull Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new BEWrapper(BusinessException.RECIPE_NOT_FOUND, id);
        }

        recipeRepository.deleteById(id);
    }

}
