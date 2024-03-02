package ykvlv.blss.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessException {
	MEDIA_NOT_FOUND("Медиа с id '%s' не найдено", HttpStatus.NOT_FOUND),
	POSTER_FILE_TYPE_NOT_SUPPORTED("Тип постера '%s' не поддерживается", HttpStatus.BAD_REQUEST),
	POSTER_NOT_FOUND("Постер с uuid '%s' не найден", HttpStatus.NOT_FOUND);

	private final String format;
	private final HttpStatus httpStatus;

}
