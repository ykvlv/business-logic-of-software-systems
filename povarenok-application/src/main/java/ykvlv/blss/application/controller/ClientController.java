package ykvlv.blss.application.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ykvlv.blss.domain.dto.request.ClientDTO;
import ykvlv.blss.domain.dto.response.ClientResponse;
import ykvlv.blss.domain.dto.response.SearchRecipesResponse;
import ykvlv.blss.application.service.ClientService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/client")
public class ClientController {

	private final ClientService clientService;

	@Operation(summary = "Зарегистрировать нового пользователя")
	@PostMapping
	public ResponseEntity<ClientResponse> create(@Validated @RequestBody ClientDTO clientDTO) {
		var clientResponse = clientService.create(clientDTO);

		return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
	}

	@Operation(summary = "Измениь данные пользователя")
	@PreAuthorize("isAuthenticated()")
	@PutMapping
	public ResponseEntity<ClientResponse> update(@Validated @RequestBody ClientDTO clientDTO,
												Authentication authentication) {
		String login = authentication.getName();

		var clientResponse = clientService.update(login, clientDTO);

		return new ResponseEntity<>(clientResponse, HttpStatus.OK);
	}

	@Operation(summary = "Добавить рецепт в кулинарную книгу")
	@PreAuthorize("hasAuthority('COOKBOOK')")
	@PutMapping("/cookbook/{recipeId}")
	public ResponseEntity<Void> addToCookBook(@PathVariable("recipeId") Long recipeId,
											  Authentication authentication) {
		String login = authentication.getName();

		clientService.addToCookbook(login, recipeId);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Operation(summary = "Удалить рецепт из кулинарной книги")
	@PreAuthorize("hasAuthority('COOKBOOK')")
	@DeleteMapping("/cookbook/{recipeId}")
	public ResponseEntity<Void> removeFromCookbook(@PathVariable("recipeId") Long recipeId,
												   Authentication authentication) {
		String login = authentication.getName();

		clientService.removeFromCookbook(login, recipeId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Получить кулинарную книгу пользователя")
	@PreAuthorize("hasAuthority('COOKBOOK')")
	@GetMapping("/cookbook")
	public ResponseEntity<SearchRecipesResponse> getCookbook(Authentication authentication) {
		String login = authentication.getName();

		return new ResponseEntity<>(
				clientService.getCookbook(login),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Поставить «Нравится» на рецепт")
	@PreAuthorize("hasAuthority('LIKE')")
	@PutMapping("/like/{recipeId}")
	public ResponseEntity<Void> like(@PathVariable("recipeId") Long recipeId, Authentication authentication) {
		String login = authentication.getName();

		clientService.likeRecipe(login, recipeId);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Operation(summary = "Получить список понравившихся рецептов")
	@PreAuthorize("hasAuthority('LIKE')")
	@GetMapping("/like")
	public ResponseEntity<SearchRecipesResponse> getLikes(Authentication authentication) {
		String login = authentication.getName();

		return new ResponseEntity<>(
				clientService.getLikes(login),
				HttpStatus.OK
		);
	}

}
