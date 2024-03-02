package ykvlv.blss.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ykvlv.blss.BLSSProperties;
import ykvlv.blss.service.PosterService;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/poster")
public class PosterController {

	private final PosterService posterService;

	@Operation(summary = "Получить все доступные постеры")
	@GetMapping("/all")
	public ResponseEntity<List<String>> all(@RequestParam(defaultValue = BLSSProperties.DEFAULT_PAGE_NUMBER) int page,
											@RequestParam(defaultValue = BLSSProperties.DEFAULT_PAGE_SIZE) int size) {
		return new ResponseEntity<>(
				posterService.all(page, size),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Загрузить новый постер")
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
		Optional<byte[]> poster = posterService.read(uuid);

		return poster.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());

	}

	@Operation(summary = "Обновить постер по uuid")
	@PutMapping(value = "/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> update(@PathVariable String uuid,
										 @RequestPart MultipartFile file) {
		return new ResponseEntity<>(
				posterService.update(uuid, file),
				HttpStatus.OK
		);
	}

	@Operation(summary = "Удалить постер по uuid")
	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<Void> delete(@PathVariable String uuid) {
		posterService.delete(uuid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
