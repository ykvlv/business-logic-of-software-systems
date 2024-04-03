package ykvlv.blss.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ykvlv.blss.domain.dto.request.ReviewDTO;
import ykvlv.blss.domain.dto.response.ReviewResponse;
import ykvlv.blss.application.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/review")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "Создать новый отзыв")
	@PreAuthorize("hasAuthority('REVIEWER')")
	@PostMapping
	public ResponseEntity<ReviewResponse> create(@Validated @RequestBody ReviewDTO reviewDTO,
												 Authentication authentication) {
		String login = authentication.getName();

		return new ResponseEntity<>(
				reviewService.create(reviewDTO, login),
				HttpStatus.CREATED
		);
	}

	@Operation(summary = "Получить отзыв по id")
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponse> read(@PathVariable("id") Long id) {
		return new ResponseEntity<>(
				reviewService.read(id),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Обновить отзыв по id")
	@PreAuthorize("hasAuthority('MAINTAINER') " +
			"|| hasAuthority('REVIEWER') && @reviewServiceImpl.isEligibleTo(#id, authentication.name)")
	@PutMapping("/{id}")
	public ResponseEntity<ReviewResponse> update(@PathVariable("id") Long id,
												 @Validated @RequestBody ReviewDTO reviewDTO) {
		return new ResponseEntity<>(
				reviewService.update(id, reviewDTO),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Удалить отзыв по id")
	@PreAuthorize("hasAuthority('MAINTAINER') " +
			"|| hasAuthority('REVIEWER') && @reviewServiceImpl.isEligibleTo(#id, authentication.name)")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		reviewService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
