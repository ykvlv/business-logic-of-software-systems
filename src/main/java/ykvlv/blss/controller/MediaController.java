package ykvlv.blss.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ykvlv.blss.data.dto.request.*;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.dto.response.SearchMediasResponse;
import ykvlv.blss.service.MediaServiceImpl;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/media")
public class MediaController {

    private final MediaServiceImpl mediaService;

    @Operation(summary = "Создать новое медиа")
    @PostMapping
    public ResponseEntity<MediaResponse> create(@Valid @RequestBody MediaDTO mediaDTO) {
        return new ResponseEntity<>(
                mediaService.create(mediaDTO),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Получить медиа по id")
    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> read(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                mediaService.read(id),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Обновить медиа по id")
    @PutMapping("/{id}")
    public ResponseEntity<MediaResponse> update(@PathVariable("id") Long id,
                                                @Valid @RequestBody MediaDTO mediaDTO) {
        return new ResponseEntity<>(
                mediaService.update(id, mediaDTO),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Удалить медиа по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        mediaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Поиск медиа")
    @PostMapping("/search")
    public ResponseEntity<SearchMediasResponse> search(@Valid @RequestBody SearchMediasDTO searchMediasDTO) {
        return new ResponseEntity<>(
                mediaService.search(searchMediasDTO),
                HttpStatus.OK
        );
    }

}
