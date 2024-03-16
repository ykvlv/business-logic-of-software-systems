package ykvlv.blss.data.dto.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PagingOptionsDTO {

	@NotNull(message = "Номер страницы должен быть указан")
	@Min(value = 0, message = "Номер страницы должен быть не менее 0")
	private Integer pageNumber;

	@NotNull(message = "Размер страницы должен быть указан")
	@Min(value = 1, message = "Размер страницы должен быть больше 0")
	@Max(value = 100, message = "Размер страницы должен быть не более 100")
	private Integer pageSize;

	@Valid
	@NotNull(message = "Параметры сортировки должны быть указаны")
	@JsonProperty("sortingOption")
	private List<SortingOptionsDTO> sortingOptionDTOs;

}
