package ykvlv.blss.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CookingStepDTO {

	@NotBlank(message = "Описание шага должно присутствовать")
	@Size(min = 3, message = "Описание шага должно быть не менее 3 символов")
	@Size(max = 1000, message = "Описание шага должно быть не более 1000 символов")
	@Pattern(regexp = "^\\S(.*\\S)?$", message = "Описание шага не должно начинаться или заканчиваться пробельным символом")
	@Pattern(regexp = "^(\\S+\\s?)+$", message = "Описание шага не должно содержать пробельные символы идущие подряд")
	private String description;

}
