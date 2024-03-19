package ykvlv.blss.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ykvlv.blss.data.dto.mapper.ClientMapper;
import ykvlv.blss.data.dto.mapper.RecipeMapper;
import ykvlv.blss.data.dto.request.ClientDTO;
import ykvlv.blss.data.dto.response.ClientResponse;
import ykvlv.blss.data.dto.response.PagingResult;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;
import ykvlv.blss.data.entity.Client;
import ykvlv.blss.data.entity.Recipe;
import ykvlv.blss.data.repository.ClientRepository;
import ykvlv.blss.data.repository.RecipeRepository;
import ykvlv.blss.data.type.RoleEnum;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final RecipeRepository recipeRepository;
	private final ClientMapper clientMapper;
	private final RecipeMapper recipeMapper;

	@NonNull
	@Override
	public ClientResponse create(@NonNull ClientDTO clientDTO) {
		if (clientRepository.existsByLogin(clientDTO.getLogin())) {
			throw new BEWrapper(BusinessException.CLIENT_ALREADY_EXISTS, clientDTO.getLogin());
		}

		Client client = clientMapper.map(clientDTO, RoleEnum.USER);

		client = clientRepository.save(client);

		return clientMapper.map(client);
	}

	@NonNull
	@Override
	public ClientResponse read(@NonNull String login) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		return clientMapper.map(client);
	}

	@NonNull
	@Override
	public ClientResponse update(@NonNull String login, @NonNull ClientDTO clientDTO) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		if (clientRepository.existsByLogin(clientDTO.getLogin())) {
			throw new BEWrapper(BusinessException.CLIENT_ALREADY_EXISTS, clientDTO.getLogin());
		}

		clientMapper.map(client, clientDTO);

		client = clientRepository.save(client);

		return clientMapper.map(client);
	}

	@Override
	public void delete(@NonNull String login) {
		if (!clientRepository.existsByLogin(login)) {
			throw new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login);
		}

		clientRepository.deleteByLogin(login);
	}

	@Override
	@Transactional // Для транзакционности
	public void addToCookbook(@NonNull String login, @NonNull Long recipeId) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		if (!recipeRepository.existsById(recipeId)) {
			throw new BEWrapper(BusinessException.RECIPE_NOT_FOUND, recipeId);
		}

		// подтверждаем успехом если рецепт уже в книге
		if (clientRepository.checkIfRecipeInCookbook(client.getId(), recipeId)) {
			return;
		}

		clientRepository.addToCookbook(client.getId(), recipeId);
	}

	@Override
	@Transactional // Для транзакционности
	public void removeFromCookbook(@NonNull String login, @NonNull Long recipeId) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		if (!recipeRepository.existsById(recipeId)) {
			throw new BEWrapper(BusinessException.RECIPE_NOT_FOUND, recipeId);
		}

		// подтверждаем успехом если рецепта нет в книге
		if (!clientRepository.checkIfRecipeInCookbook(client.getId(), recipeId)) {
			return;
		}

		clientRepository.removeFromCookbook(client.getId(), recipeId);
	}

	@NonNull
	@Override
	@Transactional(readOnly = true) // Для автоматической инициализации
	public SearchRecipesResponse getCookbook(@NonNull String login) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		Set<Recipe> cookbook = client.getCookbook();

		// TODO ну тут можно сделать пагинацию, но пока не надо

		return SearchRecipesResponse.builder()
				.recipes(cookbook.stream()
						.map(recipeMapper::mapWithoutJoins)
						.toList())
				.pagingResult(PagingResult.builder()
						.pageSize(cookbook.size())
						.pageNumber(0)
						.morePagesAvailable(false)
						.build())
				.build();
	}

	@Override
	@Transactional // Требует одновременного добавления лайка и обновления счетчика
	public void likeRecipe(@NonNull String login, @NonNull Long recipeId) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

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

	@NonNull
	@Override
	@Transactional(readOnly = true) // Для автоматической инициализации
	public SearchRecipesResponse getLikes(@NonNull String login) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		Set<Recipe> likedRecipes = client.getLikes();

		// TODO ну тут можно сделать пагинацию, но пока не надо

		return SearchRecipesResponse.builder()
				.recipes(likedRecipes.stream()
						.map(recipeMapper::mapWithoutJoins)
						.toList())
				.pagingResult(PagingResult.builder()
						.pageSize(likedRecipes.size())
						.pageNumber(0)
						.morePagesAvailable(false)
						.build())
				.build();
	}

}
