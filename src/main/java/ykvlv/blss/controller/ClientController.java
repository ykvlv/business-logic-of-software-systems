package ykvlv.blss.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ykvlv.blss.data.dto.response.SearchRecipesResponse;
import ykvlv.blss.service.ClientService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/client/{login}")
public class ClientController {

	private final ClientService clientService;

	@Operation(summary = "Добавить рецепт в кулинарную книгу")
	@PutMapping("/cookbook/{recipeId}")
	public ResponseEntity<Void> addToCookBook(@PathVariable("login") String login,
											  @PathVariable("recipeId") Long recipeId) {
		clientService.addToCookbook(login, recipeId);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Operation(summary = "Удалить рецепт из кулинарной книги")
	@DeleteMapping("/cookbook/{recipeId}")
	public ResponseEntity<Void> removeFromCookbook(@PathVariable("login") String login,
													@PathVariable("recipeId") Long recipeId) {
		clientService.removeFromCookbook(login, recipeId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Получить кулинарную книгу пользователя")
	@GetMapping("/cookbook")
	public ResponseEntity<SearchRecipesResponse> getCookbook(@PathVariable("login") String login) {
		return new ResponseEntity<>(
				clientService.getCookbook(login),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Поставить «Нравится» на рецепт")
	@PutMapping("/like/{recipeId}")
	public ResponseEntity<Void> like(@PathVariable("login") String login,
									 @PathVariable("recipeId") Long recipeId) {
		clientService.likeRecipe(login, recipeId);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Operation(summary = "Получить список понравившихся рецептов")
	@GetMapping("/like")
	public ResponseEntity<SearchRecipesResponse> getLikes(@PathVariable("login") String login) {
		return new ResponseEntity<>(
				clientService.getLikes(login),
				HttpStatus.OK
		);
	}

}
