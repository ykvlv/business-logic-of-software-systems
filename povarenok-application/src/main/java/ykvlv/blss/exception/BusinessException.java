package ykvlv.blss.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessException {
	RECIPE_NOT_FOUND("Рецепт с id '%s' не найден", HttpStatus.NOT_FOUND),
	POSTER_FILE_TYPE_NOT_SUPPORTED("Тип постера '%s' не поддерживается", HttpStatus.BAD_REQUEST),
	POSTER_NOT_FOUND("Постер с uuid '%s' не найден", HttpStatus.NOT_FOUND),
	INVALID_SORTING_ATTRIBUTE("%s: Недопустимый атрибут сортировки: '%s'", HttpStatus.BAD_REQUEST),
	CLIENT_NOT_FOUND("Клиент с логином '%s' не найден", HttpStatus.NOT_FOUND),
	CLIENT_ALREADY_EXISTS("Клиент с логином '%s' уже существует", HttpStatus.BAD_REQUEST),
	LIKE_ALREADY_EXISTS("Клиент '%s' уже поставил «Нравится» на рецепт с id '%s'", HttpStatus.FORBIDDEN),
	REVIEW_NOT_FOUND("Отзыв с id '%s' не найден", HttpStatus.NOT_FOUND);

	private final String format;
	private final HttpStatus httpStatus;

}
