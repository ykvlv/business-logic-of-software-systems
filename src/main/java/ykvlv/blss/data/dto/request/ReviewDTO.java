package ykvlv.blss.data.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {

	@NotNull(message = "ИД рецепта должен быть указан")
	private Long recipeId;

	@NotNull(message = "Логин должен быть указан")
	private String login;

	@NotNull(message = "Оценка должна быть указана")
	@Min(value = 1, message = "Минимальная оценка - 1")
	@Max(value = 5, message = "Максимальная оценка - 5")
	private Short score;

	@NotBlank(message = "Текст отзыва не может быть пустым")
	@Size(min = 5, message = "Текст отзыва должен быть длиннее 5 символов")
	@Size(max = 1000, message = "Текст отзыва должен быть короче 1000 символов")
	private String text;

	@Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", message = "UUID автора должен соответствовать формату UUID v4")
	private String posterUUID;

}
