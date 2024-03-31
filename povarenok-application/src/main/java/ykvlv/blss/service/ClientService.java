package ykvlv.blss.service;

import lombok.NonNull;
import ykvlv.blss.data.dto.request.ClientDTO;
import ykvlv.blss.data.dto.response.ClientResponse;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;

public interface ClientService {

	@NonNull
	ClientResponse create(@NonNull ClientDTO clientDTO);

	@NonNull
	ClientResponse read(@NonNull String login);

	@NonNull
	ClientResponse update(@NonNull String login, @NonNull ClientDTO clientDTO);

	void delete(@NonNull String login);

	void addToCookbook(@NonNull String login, @NonNull Long recipeId);

	void removeFromCookbook(@NonNull String login, @NonNull Long recipeId);

	@NonNull
	SearchRecipesResponse getCookbook(@NonNull String login);

	void likeRecipe(@NonNull String login, @NonNull Long recipeId);

	@NonNull
	SearchRecipesResponse getLikes(@NonNull String login);

}
