package ykvlv.blss.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessException {
	RECIPE_NOT_FOUND("Рецепт с id '%s' не найден"),
	POSTER_FILE_TYPE_NOT_SUPPORTED("Поддерживается только формат jpeg"),
	INVALID_SORTING_ATTRIBUTE("%s: Недопустимый атрибут сортировки: '%s'"),
	CLIENT_NOT_FOUND("Клиент с логином '%s' не найден"),
	CLIENT_ALREADY_EXISTS("Клиент с логином '%s' уже существует"),
	INVALID_PASSWORD("Неверный пароль для клиента с логином '%s'"),
	JSON_PARSING_ERROR("Ошибка парсинга json для '%s'"),
	INT_EXPECTED_ERROR("Ожидается Integer число для '%s'"),
	INVALID_ENUM_VALUE("Недопустимое значение для перечисления '%s', выберите из предложенных: %s"),
	VALIDATION_ERROR("Ошибка валидации: %s"),
	LIKE_ALREADY_EXISTS("Клиент '%s' уже поставил «Нравится» на рецепт с id '%s'");

	private final String format;

	public String format(Object... args) {
		return String.format(getFormat(), args);
	}

}
