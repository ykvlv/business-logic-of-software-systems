package ykvlv.blss.data.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;
import ykvlv.blss.data.validator.NullOrNotBlank;

import java.util.Set;

@Data
@NoArgsConstructor
public class SearchMediasDTO {

	@NullOrNotBlank(message = "Заголовок может отсутствовать но не должен быть пустым")
	private String title;

	@Min(value = 1, message = "Продолжительность должна быть положительной")
	@Max(value = 31536000000L, message = "Продолжительность должна быть не больше 31536000000 миллисекунд, ок?")
	private Long durationFrom;

	@Min(value = 1, message = "Продолжительность должна быть положительной")
	@Max(value = 31536000000L, message = "Продолжительность должна быть не больше 31536000000 миллисекунд, ок?")
	private Long durationTo;

	private MediaTypeEnum mediaTypeEnum;

	private Set<GenreEnum> genreEnums;

	@Valid @NotNull(message = "Параметры постраничного поиска должны быть указаны")
	private PagingOptions pagingOptions;

}
