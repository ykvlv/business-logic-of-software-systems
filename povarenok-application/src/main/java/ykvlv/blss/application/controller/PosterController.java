package ykvlv.blss.application.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.application.service.PosterService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/poster")
public class PosterController {

	private final PosterService posterService;

	@Operation(summary = "Получить все доступные постеры")
	@PreAuthorize("hasAuthority('MAINTAINER')")
	@GetMapping
	public ResponseEntity<List<String>> all(@RequestParam(defaultValue = PovarenokProperties.DEFAULT_PAGE_NUMBER) int page,
											@RequestParam(defaultValue = PovarenokProperties.DEFAULT_PAGE_SIZE) int size) {
		return new ResponseEntity<>(
				posterService.all(page, size),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Загрузить новый постер")
	@PreAuthorize("hasAuthority('MAINTAINER') || hasAuthority('CREATOR')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> create(@RequestPart MultipartFile file) {
		return new ResponseEntity<>(
				posterService.create(file),
				HttpStatus.CREATED
		);
	}

	@Operation(summary = "Получить постер по uuid")
	@GetMapping(value = "/{uuid}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> read(@PathVariable String uuid) {
		var readResult = posterService.read(uuid);

		if (!readResult.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (!readResult.isFound()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(
				readResult.getData(),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Удалить постер по uuid")
	@PreAuthorize("hasAuthority('MAINTAINER')")
	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<Void> delete(@PathVariable String uuid) {
		posterService.delete(uuid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
