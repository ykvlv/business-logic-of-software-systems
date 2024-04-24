package ykvlv.blss.application.task.recipe;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.domain.entity.Client;
import ykvlv.blss.domain.entity.Recipe;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;
import ykvlv.blss.domain.repository.ClientRepository;
import ykvlv.blss.domain.repository.RecipeRepository;

@Component
@RequiredArgsConstructor
public class LikeRecipeTask implements JavaDelegate {

	private final RecipeRepository recipeRepository;
	private final ClientRepository clientRepository;

	@Override
	public void execute(DelegateExecution execution) {
		var client = (Client) execution.getVariable("client");

		var recipeId = (Long) execution.getVariable("recipeId");

		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new BEWrapper(BusinessException.RECIPE_NOT_FOUND, recipeId));

		// отклоняем запрос если лайк уже стоит
		if (clientRepository.checkIfRecipeLiked(client.getId(), recipe.getId())) {
			throw new BEWrapper(BusinessException.LIKE_ALREADY_EXISTS, client.getLogin(), recipe.getId());
		}

		// Создаем лайк, сохраняем и обновляем счетчик у рецепта
		clientRepository.likeRecipe(client.getId(), recipe.getId());

		recipe.setLikesCount(recipe.getLikesCount() + 1);
		recipeRepository.save(recipe);
	}
}
