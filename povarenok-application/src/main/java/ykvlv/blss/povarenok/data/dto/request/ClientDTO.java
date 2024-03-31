package ykvlv.blss.povarenok.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDTO {

	@NotBlank(message = "Логин должен присутствовать")
	@Size(min = 3, message = "Логин должен быть не менее 3 символов")
	@Size(max = 20, message = "Логин должен быть не более 20 символов")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин должен содержать только английские буквы и цифры")
	private String login;

	@NotBlank(message = "Пароль должен присутствовать")
	@Size(min = 8, message = "Пароль должен быть не менее 8 символов")
	@Size(max = 40, message = "Пароль должен быть не более 40 символов")
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_=+]+$",
			message = "Пароль должен содержать только английские буквы и специальные символы: !@#$%^&*()-_=+")
	private String password;

	@NotBlank(message = "Имя должно присутствовать")
	@Size(min = 2, message = "Имя должно быть не менее 2 символов")
	@Size(max = 20, message = "Имя должно быть не более 20 символов")
	@Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно содержать только русские или английские буквы")
	private String name;

}
