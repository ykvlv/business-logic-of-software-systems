package ykvlv.blss.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ykvlv.blss.data.dto.request.MediaDTO;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.service.ClientService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/client")
public class ClientController {

	private final ClientService clientService;

	@Operation(summary = "Переключить избранное медиа")
	@PutMapping("/{login}/favorite/{mediaId}")
	public ResponseEntity<Boolean> update(@PathVariable("login") String login,
												@PathVariable("mediaId") Long mediaId) {
		return new ResponseEntity<>(
				clientService.toggleFavoriteMedia(login, mediaId),
				HttpStatus.OK
		);
	}
}
